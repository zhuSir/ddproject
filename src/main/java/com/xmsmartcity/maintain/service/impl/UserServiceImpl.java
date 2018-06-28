package com.xmsmartcity.maintain.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xmsmartcity.maintain.dao.*;
import com.xmsmartcity.maintain.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.extra.aliyun.OssUtil;
import com.xmsmartcity.extra.push.PushUtil;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.orm.Page;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.MD5;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{

	public UserServiceImpl(BaseDao<User> dao) {
		super(dao);
	}

	@Autowired
	private UserMapper dao;
	
	@Autowired
	private DealEventMapper dealEventDao;
	
	@Autowired
	private UserTokenMapper userTokenDao;
	
	@Autowired
	private CompanyMapper companyDao;
	
	@Autowired
	private IndexUserFaultMapper indexUserFaultDao;
	
	@Autowired
	private EvaluateMapper evaluateDao;
	
	@Autowired
	private MaintainOperateLogMapper maintainOperateLogDao;
	
	@Autowired
	private MessageMapper messageDao;
	
	@Autowired
	private FaultInfoMapper faultInfoDao;
	
	@Autowired
	private ProjectMapper projectDao;
	
	@Autowired
	private ServiceTypeMapper serviceTypeDao;

	@Autowired
    private PatrolSchemeMapper patrolSchemeDao;
	
	@Override
	public User getUser(int id) {
		User user  = dao.selectByPrimaryKey(id);
		if(user != null){
			Company company = companyDao.selectByPrimaryKey(user.getCompanyId());
			if(company != null){
				user.setCompanyName(company.getName());
			}
		}
		return user;
	}
	
	@Override
	@Transactional
	public synchronized User newUser(String phone, String username, String password) {
		Assert.isNull(dao.selectByPhone(phone),Constant.USER_EXIST);//判断手机号是否已经注册过
		User user = new User(phone,password,new Date(),new Date(),OssUtil.USER_IMG+Constant.DEFAULT_HEAD_PIC,username);
		user.setRemark("");
		dao.insertSelective(user);
		user.setPassword(null);
		DealEvent dealEvent = new DealEvent();
		dealEvent.setUserId(user.getId());
		dealEventDao.insertSelective(dealEvent);
		return user;
	}

	@Override
	public boolean checkExist(String phone) {
		User user = dao.selectByPhone(phone);
		if(user != null){
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public User login(String phone, String password, String pushToken,HttpServletRequest request) {
		Assert.isTrue(checkExist(phone),Constant.USER_NO_EXIST);//验证手机号是否注册过
		User user = dao.selectByPhoneAndPsw(phone, password);//登录验证手机号和密码是否正确
		Assert.notNull(user, Constant.USER_INFO_ERROR);
		Assert.state(user.getState().equals(0), Constant.BE_BAN);//用户是否禁用
		user.setLastLogintime(new Date());
		user.setLoginTimes(user.getLoginTimes()+1);
		dao.updateByPrimaryKeySelective(user);//更新登录时间及登录次数
		String sysToken = updatePushToken(pushToken, user.getId(),request);//更新用户pushToken
		Company company = companyDao.selectByPrimaryKey(user.getCompanyId());
		if(company != null){
			user.setCompanyName(company.getName());
		}
		user.setPushToken(pushToken);
		user.setToken(sysToken);
		return user;
	}
	
	/**
	 * 更新用户token
	 * @param pushToken
	 * @param userId
	 * @return
	 */
	@Transactional
	public String updatePushToken(String pushToken,Integer userId,HttpServletRequest request){
		String sysToken = MD5.parseMD5(new Date()+""+userId);
		String clientIdentify = request.getHeader("user-agent");
		boolean isIos = clientIdentify.contains("iPhone");
		boolean isAndroid = clientIdentify.contains("Android");
		Integer loginType = 0;
		Integer[] types = {};
		if(isIos){
			loginType = 1;//IOS
			types = new Integer[]{1,2};
		}else if(isAndroid){
			loginType = 2;//Android
			types = new Integer[]{1,2};
		}else{
			loginType = 0;//WEB
			types = new Integer[]{0};
		}
		JSONObject msgResult = new JSONObject();
		Message message = new Message();
		message.setClassify(3);//设置clssify=3异地登陆异常
		msgResult.put("info", message);
		msgResult.put("type", Constant.ONE);
		//替换掉原来有的
		List<UserToken> uToken = userTokenDao.getTokenInfoByParams(userId,null,types);
		if(uToken != null && uToken.size() > 0){
			UserToken ut = uToken.get(0);
			if(!ut.getPushToken().equals(pushToken)){
				//异地登录推送
				msgResult.put("content", "您的帐号在其他设备上登录");
				//推送信息
				PushUtil.pushSingleMsg(ut.getPushToken(),msgResult.toString());
			}
			//删除上所有token信息
			Integer[] uids = new Integer[uToken.size()];
			
			for(int i=0;i<uToken.size();i++){
				UserToken userT = uToken.get(i);
				uids[i]=userT.getId();
			}
			userTokenDao.deleteForIds(uids);
		}
		UserToken newToken = new UserToken(userId, pushToken,sysToken,loginType);
		userTokenDao.insertSelective(newToken);
		return sysToken;
	}

	
	@Override
	public User loginByCode(String phone,String pushToken,HttpServletRequest request) {
		Assert.isTrue(checkExist(phone),Constant.USER_NO_EXIST);//验证手机号是否注册过
		User user = dao.selectByPhone(phone);//登录验证手机号
		Assert.state(user.getState().equals(0), Constant.BE_BAN);//用户是否禁用
		user.setLastLogintime(new Date());
		user.setLoginTimes(user.getLoginTimes()+1);
		dao.updateByPrimaryKeySelective(user);//更新登录时间及登录次数
		String sysToken = updatePushToken(pushToken, user.getId(),request);//更新用户pushToken
//		user.setHeadPic(OssUtil.IMG_ACCESS_URL+"/"+(StringUtils.isNotBlank(user.getHeadPic())? user.getHeadPic() : Constant.DEFAULT_HEAD_PIC));
//		OssUtil.closeClient();
		Company company = companyDao.selectByPrimaryKey(user.getCompanyId());
		if(company != null){
			user.setCompanyName(company.getName());
		}
		user.setPushToken(pushToken);
		user.setToken(sysToken);
		return user;
	}

	@Override
	public int modifyPassword(User user) {
		return dao.updateByPrimaryKeySelective(user);
	}

	@Override
	public User getUserByPhone(String phone) {
		User user = dao.selectByPhone(phone);
		return user;
	}

	@Override
	@Transactional
	public int modifyUserInfo(User user) {
		user.setPhone(null);
		int res = 0;
		if(StringUtils.isNotBlank(user.getPhone())){
			User oldUser = dao.selectByPrimaryKey(user.getId());
			if(!oldUser.getPhone().equals(user.getPhone())){
				res = updateUserPhone(oldUser.getPhone(),user);
			}
			if(!oldUser.getName().equals(user.getName()));{
                updateUserName(user);
			}
		}
		res = dao.updateByPrimaryKeySelective(user);
		return res;
	}
	
	@Transactional
	public int updateUserPhone(String phone,User user){
		int res = 0;
		List<Company> companys = companyDao.selectByResponserPhone(phone);
		for(Company company : companys){
			if(company != null){
				company.setResponserPhone(user.getPhone());
				res = companyDao.updateByPrimaryKey(company);
			}
		}
		List<FaultInfo> faultInfos = faultInfoDao.selectByFaultUserPhone(phone);
		for(FaultInfo faultInfo : faultInfos){
			if(faultInfo != null){
				faultInfo.setFaultUserMobile(user.getPhone());
				res = faultInfoDao.updateByPrimaryKey(faultInfo);
			}
		}
		List<Message> messages = messageDao.selectByPhone(phone);
		for(Message message : messages){
			if(message != null){
				message.setPhone(user.getPhone());
				res = messageDao.updateByPrimaryKey(message);
			}
		}
		List<Project> projects = projectDao.selectByOwnerPhone(phone);
		for(Project project : projects){
			if(project != null){
				project.setOwnerPhone(user.getPhone());
				res = projectDao.updateByPrimaryKey(project);
			}
		}
		List<ServiceType> serviceTypes = serviceTypeDao.selecyByMaintainUserPhone(phone);
		for(ServiceType serviceType : serviceTypes){
			if(serviceType != null){
				serviceType.setMaintainUserMobile(user.getPhone());
				res = serviceTypeDao.updateByPrimaryKey(serviceType);
			}
		}
		return res;
	}

	@Transactional
	public void updateUserName(User user){
		Company company = companyDao.selectByResponserId(user.getId());
		if (company!=null){
		    company.setResponserName(user.getName());
            companyDao.updateByPrimaryKey(company);
        }
		List<Project> projects = projectDao.selectByOwnerPhone(user.getPhone());
		for(Project project : projects){
			if(project != null){
				project.setOwnerName(user.getName());
				projectDao.updateByPrimaryKey(project);
			}
		}
		List<ServiceType> serviceTypes = serviceTypeDao.selecyByMaintainUserPhone(user.getPhone());
		for(ServiceType serviceType : serviceTypes){
			if(serviceType != null){
				serviceType.setMaintainUserName(user.getName());
				serviceTypeDao.updateByPrimaryKey(serviceType);
			}
		}
		//更新巡检负责人名称
		PatrolScheme selectPatrolScheme=new PatrolScheme();
        selectPatrolScheme.setPatrolUserId(user.getId());
		List<PatrolScheme> patrolSchemes=patrolSchemeDao.selectByRecord(selectPatrolScheme,null,null,null);
		for (PatrolScheme patrolScheme:patrolSchemes){
            patrolScheme.setPatrolUserName(user.getName());
            patrolSchemeDao.updateByPrimaryKey(patrolScheme);
        }
	}

	@Override
	public void validateMail(Integer userId, String mail) {
		int res = dao.selectByUserIdAndMail(userId, mail);
		Assert.state(res == 0,"该邮箱已被绑定，请更换邮箱!");
	}

	@Override
	public User selectByPrimaryKey(Integer userId) {
		return dao.selectByPrimaryKey(userId);
	}

	/**
	 * 获取微信openID
	 */
	@Override
	public List<String> getUserOpenIdsById(List<Integer> userIdList) {
		List<String> results = dao.selectUserOpenIdsByIds(userIdList);
		return results;
	}
	
	@Override
	public List<User> selectUsersByCompanyId(Integer companyId, Integer startIndex, Integer pageSize) {
		return dao.selectUsersByCompanyId(companyId, startIndex, pageSize);
	}

	@Override
	public DealEvent selectUserState(Integer userId) {
		DealEvent obj = null;
		Company company = companyDao.selectByResponserId(userId);
		if(company != null){
			obj = dealEventDao.selectByCompanyPrincipal(company.getId(),userId);
			obj.setIsLeader(1);
		}else{
			obj = dealEventDao.selectByUserId(userId);
			obj.setIsLeader(0);
		}
		User user = dao.selectByPrimaryKey(userId);
		Integer count = messageDao.selectUnReadMessageByPhone(user.getPhone());
		obj.setUnReadNum(count);
		Company userCompany = companyDao.selectByPrimaryKey(user.getCompanyId());
		if(userCompany != null){			
			obj.setCompany_createtime(userCompany.getCreatetime());
		}
		return obj;
	}

	@Override
	public User getUserByOpenId(String openId) {
		User user = dao.selectByOpenId(openId);
		return user;
	}
	
	@Override
	public User getUserByUnionid(String unionid){
		User user = dao.selectByUnionId(unionid);
		return user;
	}

	/*
	 * 用户手机号和密码登录
	 */
	@Override
	public User login(String account, String password) {
		Assert.isTrue(checkExist(account),"用户未注册");//验证手机号是否注册过
		User user = dao.selectByPhoneAndPsw(account,password);//登录验证手机号
		Assert.notNull(user, Constant.USER_INFO_ERROR);
		Assert.state(user.getState().equals(0), Constant.BE_BAN);//用户是否禁用
		user.setLastLogintime(new Date());
		user.setLoginTimes(user.getLoginTimes()+1);
		dao.updateByPrimaryKeySelective(user);//更新登录时间及登录次数
		Company company = companyDao.selectByPrimaryKey(user.getCompanyId());
		if(company != null){
			user.setCompanyName(company.getName());
		}
		return user;
	}

	@Override
	public void update(User user) {
		dao.updateByPrimaryKeySelective(user);
	}

	@Override
	public Integer getUserCount() {
		Integer userCount = dao.selectCount();
		return userCount;
	}

	@Override
	public Map<String,Object> getCountUserInfo(Integer otherUserId) {
		Integer maintainCount = indexUserFaultDao.selectMaintainCount(otherUserId);
		Integer evaluateSum = evaluateDao.selectAverageNumByFaultUserId(otherUserId);
		Integer operateNum = maintainOperateLogDao.selectOperateNumByUserId(otherUserId);
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("maintainCount", maintainCount == null ? 0 : maintainCount);
		obj.put("evaluateCount", evaluateSum == null ? 0 : evaluateSum);
		obj.put("operateCount", operateNum == null ? 0 : operateNum);
		return obj;
	}

	@Override
	public Integer removeUserTokenByParams(Integer userId, String sysToken) {
		return userTokenDao.removeByParams(userId,sysToken);
	}

	@Override
	public Page<User> queryForCompanyUserPage(User user, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		Page<User> page = new Page<User>();
		page.setPageNo(pageNo);
    	page.setPageSize(pageSize);
    	List<User> list=dao.queryForCompanyUserPage(user, (pageNo-1)*pageSize, pageSize);
    	page.setList(list);
    	List<User> allList=dao.queryForCompanyUserPage(new User(),null,null);
    	int total=allList.size();
    	page.setTotal(total);
    	int totalPage = total / pageSize;
		// 不足不页算一页
		if (total % pageSize > 0){
			totalPage++;
		} 
		page.setTotalPage(totalPage);
		return page;
	}

	@Override
	public User selectByPhone(String phone) {
		return dao.selectByPhone(phone);
	}

	@Override
	public User getUserByXcxOpenId(String xcxOpenId) {
		return dao.selectByXcxOpenId(xcxOpenId);
	}

	@Override
	public User getByPhoneAndPsw(String phone, String password) {
		return dao.selectByPhoneAndPsw(phone, password);
	}

	/**
	 * 获取人员动态
	 * @author:zhugw
	 * @time:2017年9月8日 下午2:59:54
	 * @param userId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getUserDynamic(Integer userId,Integer startIndex, Integer pageSize) {
		User user = dao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR+" 用户ID错误!");
		List<Map<String,Object>> results= dao.selectUserDynamic(user.getCompanyId(),startIndex,pageSize);
		return results;
	}

	@Override
	public List<User> selectCompanyMember(Integer userId, Integer companyId, String searchPhone) {
		User user = dao.selectByPrimaryKey(userId);
		Assert.state(user != null && user.getCompanyId().equals(companyId),Constant.PARAMS_ERROR+": 当前用户不是该公司所属员工,userId："+userId+" companyId:"+companyId);
		List<User> resUser = dao.selectCompanyMember(companyId,searchPhone);
		return resUser;
	}

	
	/*
	 * web 页面个人中心
	 */
	@Override
	public Object selectUserInfo(Integer userId) {
		Map<String,Object> result = dao.selectUserInfo(userId);
		return result;
	}

	/**
	 * 根据系统token查询user
	 * @author:zhugw
	 * @time:2017年11月15日 上午9:42:59
	 * @param token
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.UserService#selectBySystemToken(java.lang.String)
	 */
	@Override
	public User selectUserBySystemToken(String token) {
		User user = dao.selectUserBySystemToken(token);
		return user;
	}

}
