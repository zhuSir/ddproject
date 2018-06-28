package com.xmsmartcity.maintain.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.xmsmartcity.extra.aliyun.OssUtil;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.CompanyMapper;
import com.xmsmartcity.maintain.dao.DealEventMapper;
import com.xmsmartcity.maintain.dao.FaultInfoMapper;
import com.xmsmartcity.maintain.dao.IndexUserFaultMapper;
import com.xmsmartcity.maintain.dao.MessageMapper;
import com.xmsmartcity.maintain.dao.ProjectMapper;
import com.xmsmartcity.maintain.dao.ProjectUserMapper;
import com.xmsmartcity.maintain.dao.UserMapper;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ProjectUser;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.orm.Page;
import com.xmsmartcity.orm.Pagination;
import com.xmsmartcity.util.AES;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.StringUtil;

import net.sf.json.JSONObject;

@Service
public class CompanyServiceImpl extends BaseServiceImpl<Company> implements CompanyService {

	public CompanyServiceImpl(BaseDao<Company> dao) {
		super(dao);
	}

	@Autowired
	private CompanyMapper dao;

	@Autowired
	private UserMapper userDao;

	@Autowired
	private DealEventMapper dealEventDao;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ProjectUserMapper projectUserDao;
	
	@Autowired
	private FaultInfoMapper faultInfoDao;
	
	@Autowired
	private ProjectMapper projectDao;
	
	@Autowired
	private IndexUserFaultMapper indexUserFaultDao;
	
	@Autowired
	private MessageMapper messageDao;

	/**
	 * 根据名称搜索团队
	 */
	@Override
	public List<Company> getCompanyList(Integer startIndex, Integer pageSize, String name) {
		List<Company> results = null;
		if(name == null){
			results = dao.selectList(startIndex, pageSize);
		}else{
			results = dao.selectListByName(startIndex, pageSize, name);
		}
		if(results != null){
			//遍历查询团队用户个数
			for(Company company : results ){
				List<User> users = userDao.selectUsersByCompanyId(company.getId(), null, null);
				company.setUserSize(users.size());
			}
		}
		return results;
	}

	/**
	 * 创建团队
	 * @author:zhugw
	 * @time:2017年4月10日 上午11:58:00
	 * @param company
	 * @return (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#createCompany(com.xmsmartcity.maintain.pojo.Company)
	 */
	@Override
	@Transactional
	public Company createCompany(Company company) {
		Date now = new Date();
		company.setCreatetime(now);
		company.setState(1);
		if(!StringUtils.isEmpty(company.getHeadPic())){			
			company.setHeadPic(OssUtil.COMPANY_IMG+Constant.DEFAULT_COMPANY_PIC);// 设置默认头像
		}
		User user = userDao.selectByPrimaryKey(company.getResponserId());// 查询团队负责人
		Assert.state(user.getCompanyId().equals(Constant.ZERO), "您已经创建过团队不可再创建团队！");
		company.setResponserName(user.getName());
		company.setResponserPhone(user.getPhone());
		String companyShortName = company.getShortName();
		if(!StringUtils.isNotEmpty(companyShortName)){
			String shortName = StringUtil.getCompanyKeyword(company.getName());
			company.setShortName(shortName);
		}
		dao.insertSelective(company);// 创建团队
		user.setCompanyId(company.getId());
		user.setRole(Constant.USER_ROLE_INDEX.COMPANY_USER);
		user.setJoinTime(new Date());
		userDao.updateByPrimaryKeySelective(user);// 更新用户信息
		DealEvent obj = dealEventDao.selectByUserId(user.getId());
		if(obj != null && obj.getHasCompany().equals(0)){
			obj.setHasCompany(1);
			dealEventDao.updateByPrimaryKey(obj);
		}
		return company;
	}

	/**
	 * 获取团队信息
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:57:40
	 * @param id
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#getCompanyInfo(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Company getCompanyInfo(Integer id,Integer userId,Integer startIndex,Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR+"user id error");
		Assert.state(user.getCompanyId().equals(id),Constant.PARAMS_ERROR+"获取的团队详细不是当前用户所属团队。");
		Company company = dao.selectByPrimaryKey(id);
		Assert.state(company != null,Constant.PARAMS_ERROR);
		//获取汇总信息
		Company collect_company = dao.selectCollect(id);
		if(collect_company != null){
			company.setFaultIngCount(collect_company.getFaultIngCount());
			company.setFaultMonthCount(collect_company.getFaultMonthCount());
			company.setFaultSumCount(collect_company.getFaultSumCount());
			company.setMaintainIngCount(collect_company.getMaintainIngCount());
			company.setMaintainMonthCount(collect_company.getMaintainMonthCount());
			company.setMaintainSumCount(collect_company.getMaintainSumCount());
		}
		
		Assert.state(company != null,Constant.PARAMS_ERROR);
		List<User> users = userDao.selectUsersByCompanyId(id, startIndex, pageSize);
		for(int i =0;i<users.size();i++){
			User princUser = users.get(i);
			if(company.getResponserId().equals(princUser.getId()) && i != 0){
				users.remove(i);
				users.add(0,princUser);
				break;
			}
		}
		company.setUsers(users);
		//遍历查询团队用户个数
		company.setUserSize(userDao.selectUsersByCompanyId(company.getId(), null, null).size());
		return company;
	}

	/**
	 * 申请加入团队
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:57:35
	 * @param companyId
	 * @param userId
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#applyJoin(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void applyJoin(Integer companyId, Integer userId) {
		// 判断用户是否已经是团队成员
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user.getCompanyId().equals(Constant.ZERO), "您已经有所属团队了哦，请返回重试");
		Company company = dao.selectByPrimaryKey(companyId);
		Assert.state(company != null, "您申请的团队已不存在，请返回重试");
		// 获取团队负责人
		User responseUser = userDao.selectByPrimaryKey(company.getResponserId());
		// 新增消息（企业类）
		messageService.sendMessage(responseUser.getPhone(), userId, "申请加入团队,快去看看吧~",
				Constant.MESSAGE_TYPE_INDEX.APPLY_JOIN_COMPANY_INDEX, companyId,
				Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, companyId, Constant.MESSAGE_STATE_INDEX.PENDING,
				user.getName(), 1);
	}

	/**
	 * 获取团队成员
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:47:18
	 * @param userId
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#selectUserByCompanyId(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<User> selectUserByCompanyId(Integer userId,Integer companyId, Integer startIndex, Integer pageSize) {
		Company company = dao.selectByPrimaryKey(companyId);
		List<User> resList = userDao.selectUsersByCidAndUid(userId,companyId, startIndex, pageSize);
		for(int i=0;i<resList.size();i++){
			Integer resUserId = company.getResponserId();
			if(resUserId.equals(resList.get(i).getId())){
				User tempObj = resList.get(i);
				resList.remove(i);
				resList.add(0, tempObj);
				break;
			}
		}
		return resList; 
	}

	/**
	 * 团队删除成员
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:47:28
	 * @param userId
	 * @param companyId
	 * @param delUserId
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#delUserByUserId(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Transactional
	public int delUserByUserId(Integer userId, Integer companyId, String delUserId) {
		Company company = dao.selectByPrimaryKey(companyId);
		if (company.getResponserId() != Constant.ZERO) {
			Assert.state(company.getResponserId().equals(userId), Constant.NO_COMPANY_POWER);
		}
		String[] users = delUserId.split(",");
		Integer res = 0;
		for (String id : users) {
			// 非项目负责人
			List<ProjectUser> pus = projectUserDao.selectListByParames(null, Integer.valueOf(id), null,Constant.ONE, null);
			Assert.state(pus.size() < 1,Constant.ADVICE_CHANGE_LEADER);
			// 没有未完成的报障
			Integer count = faultInfoDao.checkUnfinished(Integer.valueOf(id), null, null);
			Assert.state(count == 0,Constant.NO_FINISH_FAULTINFO);
			User user = userDao.selectByPrimaryKey(Integer.valueOf(id));
			List<Message> messages = messageDao.selectByPhone(user.getPhone());
			for(Message msg : messages){
				msg.setIsDel(1);
				messageDao.updateByPrimaryKeySelective(msg);
			}
			// 给被删除人员发消息
			messageService.sendMessage(user.getPhone(), userId, "您已经被移出" + company.getName(),
					Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX, companyId, Constant.MESSAGE_CLASSIFY_INDEX.COMPANY,
					companyId, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null, 1);
			// 更新被删人员的团队信息
			user.setCompanyId(Constant.ZERO);
			res = userDao.updateByPrimaryKeySelective(user);
			//修改用户状态
			DealEvent dealEvent = dealEventDao.selectByUserId(user.getId());
			if(dealEvent != null){
				dealEvent.setHasCompany(0);
				dealEvent.setHasProject(0);
				dealEventDao.updateByPrimaryKeySelective(dealEvent);
			}
		}
		return res;
	}

	/**
	 * 邀请成员
	 * TODO
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:47:34
	 * @param userId
	 * @param invitePhone
	 * @throws IOException
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#inviteJoin(java.lang.Integer, java.lang.String)
	 */
	@Override
	public void inviteJoin(Integer userId, String invitePhone) throws IOException {
		User responseUser = userDao.selectByPrimaryKey(userId);
		Assert.state(responseUser != null, "系统错误,请返回重试！");
		Company company = dao.selectByPrimaryKey(responseUser.getCompanyId());
		Assert.state(company != null, "您还未加入团队,不能添加成员！");
		String[] invitePhones = invitePhone.split(",");
		for(String phone : invitePhones){
			String inviteToken = company.getId()+"_"+phone+"_"+userId;
			inviteToken = AES.encrypt(inviteToken);//AES加密后的companyId+phone
			String webPath = "http://"+Constant.hostSite+"/dweibao/join-cm/"+inviteToken;
			// 新增消息（邀请类）
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.COMPANY,Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_JOIN_COMPANY_INDEX, 
					phone, userId, company.getId(), false,webPath);
			
			User user = userDao.selectByPhone(phone);
			if (user != null) {
//				Assert.state(user.getCompanyId().equals(Constant.ZERO), "'"+phone+"'已有所属团队，请重新填写");
				// 新增消息（邀请类）
				messageService.sendMessage(phone, responseUser.getId(), "邀您成为" + company.getName() + "团队成员，请尽快确认噢~",
						Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_JOIN_COMPANY_INDEX, responseUser.getCompanyId(),
						Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, responseUser.getCompanyId(),
						Constant.MESSAGE_STATE_INDEX.PENDING, responseUser.getName(), 1);
			}
		}
	}
	
	/**
	 * 退出公司
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:47:42
	 * @param userId
	 * @param companyId
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#leftCompany(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional
	public int leftCompany(Integer userId, Integer companyId) {
		Company c = dao.selectByUserId(userId);
		Assert.state(c == null,"您是该团队负责人，您不能退出团队,请先移交团队!");
		//项目负责人
		List<ProjectUser> res = projectUserDao.selectListByParames(null, userId, null, 1, null);
		Assert.state( res.size()==0 ,Constant.NOT_CHANGE_LEADER);
		// 没有未完成的报障
		Integer count = faultInfoDao.checkUnfinished(userId, null, null);
		Assert.state(count == 0,Constant.NO_FINISH_FAULTINFO);
		// 给团队负责人员发消息
		Company company = dao.selectByPrimaryKey(companyId);
		User user = userDao.selectByPrimaryKey(userId);
		messageService.sendMessage(company.getResponserPhone(), userId, "退出了团队",
				Constant.MESSAGE_TYPE_INDEX.LEFT_COMPANY, companyId, Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, 0,
				Constant.MESSAGE_STATE_INDEX.NO_HANDLE, user.getName(), 1);
		// 更新被删人员的团队信息
		user.setCompanyId(Constant.ZERO);
		int result = userDao.updateByPrimaryKeySelective(user);
		// 退出所有项目
		List<Project> projectRes = projectDao.selectListByCompanyId(companyId, null, null);
		for(Project p : projectRes){
			if(userId.equals(p.getOwnerId())){
				p.setOwnerId(0);
				p.setOwnerName("");
				p.setOwnerPhone("");
				result = projectDao.updateByPrimaryKeySelective(p);
			}
		}
		List<ProjectUser> pus = projectUserDao.selectListByParames(null, userId, null, null, null);
		for(ProjectUser pu : pus){
			if(pu != null){
				pu.setIsDel(1);
				projectUserDao.updateByPrimaryKeySelective(pu);
			}
		}
		// 退出所有报障单
		List<IndexUserFault> iuFaultRes = indexUserFaultDao.selectListByUserId(userId,null,null);
		for(IndexUserFault iuf : iuFaultRes){
			iuf.setIsDel(1);
			result = indexUserFaultDao.updateByPrimaryKeySelective(iuf);
		}
		// 删除所有消息
		List<Message> messages = messageDao.selectByPhone(user.getPhone());
		for(Message msg : messages){
			msg.setIsDel(1);
			result = messageDao.updateByPrimaryKeySelective(msg);
		}
		//修改用户状态
		DealEvent dealEvent = dealEventDao.selectByUserId(userId);
		if(dealEvent != null){
			dealEvent.setHasCompany(0);
			dealEvent.setHasProject(0);
			dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
		return result;
	}

	@Override
	public String saveCompany(Company company, Integer userId) {
		Company res = dao.selectByPrimaryKey(company.getId());
		Assert.state(res != null, "系统错误，请联系客服人员");
		Assert.state(res.getResponserId().equals(userId), "当前用户不是该团队负责人，请刷新后再访问.");
		int re = dao.updateByPrimaryKeySelective(company);
		return re == 1 ? "保存成功！" : "系统错误，请联系客服人员";
	}

	/**
	 * 移交团队
	 * @author:zhugw
	 * @time:2017年4月10日 下午2:32:47
	 * @param userId
	 * @param companyId
	 * @param reUserId
	 * @return (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#transferCompany(java.lang.Integer,
	 *      java.lang.Integer, java.lang.String)
	 */
	@Override
	public String transferCompany(Integer userId, Integer companyId, Integer reUserId) {
		User user = userDao.selectByPrimaryKey(userId);
		Company company = dao.selectByPrimaryKey(companyId);
		Assert.state(company.getResponserId().equals(userId), "您不是团队的负责人无法移交团队");
		User reUser = userDao.selectByPrimaryKey(reUserId);
		if (null != reUser) {
			Assert.state(reUser.getCompanyId().equals(user.getCompanyId()), "移交人员非本团队成员无法移交");
			Assert.state(!reUser.getId().equals(user.getId()), "无法将团队转移给自己");
		}
		String content = "将" + company.getName() + "将团队负责人移交给您，请尽快确认噢~";
		messageService.setReferMsgUseless(companyId,Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX);//将邀请加入团队的消息置为无效
		messageService.sendMessage(reUser.getPhone(), userId, content,
				Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX, companyId,
				Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, companyId, Constant.MESSAGE_STATE_INDEX.PENDING,
				user.getName(), 1);
		return "移交团队成功，正在等待回复~";
	}

	/**
	 * 移交团队（手机号）
	 * @author:zhugw
	 * @time:2017年4月18日 上午10:55:22
	 * @param userId
	 * @param companyId
	 * @param phone
	 * @return
	 * (non-Javadoc)
	 * @throws IOException 
	 * @see com.xmsmartcity.maintain.service.CompanyService#transferCompany(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public String transferCompany(Integer userId, Integer companyId, String phone) throws IOException {
		User user = userDao.selectByPrimaryKey(userId);
		Company company = dao.selectByPrimaryKey(companyId);
		Assert.state(company.getResponserId().equals(userId), "您不是团队的负责人无法移交团队");
		User reUser = userDao.selectByPhone(phone);
		String content = "将" + company.getName() + "将团队负责人移交给您，请尽快确认噢~";
		if (null != reUser) {
			//Assert.state(reUser.getCompanyId().equals(user.getCompanyId()), "移交人员非本团队成员无法移交");
			Assert.state(!reUser.getId().equals(user.getId()), "无法将团队转移给自己");
			//messageService.setReferMsgUseless(companyId);//将邀请加入团队的消息置为无效
			messageService.sendMessage(reUser.getPhone(), userId, content,
					Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX, companyId,
					Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, companyId, Constant.MESSAGE_STATE_INDEX.PENDING,
					user.getName(), 1);
		}else{
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.COMPANY,Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX,
						phone, userId, companyId, false,null);
		}
		return "移交团队成功，正在等待回复~";
	}

	/**
	 * 获取团队成员，过滤掉项目成员
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:47:56
	 * @param projectId
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#getCompanyMemberFilterProject(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<User> getCompanyMemberFilterProject(Integer projectId, Integer userId, Integer startIndex,
			Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		List<User> users = userDao.selectCompamyMemberFileterProject(user.getCompanyId(),projectId,startIndex,pageSize);
		return users;
	}
	
	@Override
	public Object getCompanyCount() {
		int companyCount = dao.selectCount();
		return companyCount;
	}

	/**
	 * 获取公司成员过滤到项目负责人
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:50:21
	 * @param projectId
	 * @param serviceTypeId
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#getCompanyMemberFilterProjectLeader(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<User> getCompanyMemberFilterProjectLeader(Integer projectId,Integer serviceTypeId, Integer userId, Integer startIndex,
			Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		List<User> users = userDao.selectCompamyMemberFileterProjectLeader(user.getCompanyId(),projectId,serviceTypeId, startIndex, pageSize);
		return users;
	}

	/**
	 * 获取公司详情
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:50:37
	 * @param id
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#getCompanyDetail(java.lang.Integer)
	 */
	@Override
	public Company getCompanyDetail(Integer id) {
		Company company=dao.findNoDel(id);
		if (company.getThreeCertificates()!=null || company.getThreeCertificates()!="") {
			String[] pics=company.getThreeCertificates().split(",");
			for (int i = 0; i < pics.length; i++) {
				if (i==0) {
					company.setCertificatesA(pics[i]);
				}else if (i==1) {
					company.setCertificatesB(pics[i]);
				}else {
					company.setCertificatesC(pics[i]);
				}
			}
		}
		return company;
	}

	@Override
	public Page<Company> queryForCompanyPage(Company company, int pageNo, int pageSize) {
		Page<Company> page = new Page<Company>();
		page.setPageNo(pageNo);
    	page.setPageSize(pageSize);
    	List<Company> list=dao.queryForCompanyPage(company, (pageNo-1)*pageSize, pageSize);
    	page.setList(list);
    	List<Company> allList=dao.queryForCompanyPage(new Company(),null,null);
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

	/**
	 * 加入公司成员
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:49:56
	 * @param phone
	 * @param userId
	 * @param companyId
	 * @param reUserId
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#joinCompanyMember(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void joinCompanyMember(String phone,Integer userId,Integer companyId,Integer reUserId) {
		Message msg = new Message();
		msg.setClassify(Constant.MESSAGE_CLASSIFY_INDEX.COMPANY);
		msg.setPhone(phone);
		msg.setIsDel(0);
		msg.setType(Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_JOIN_COMPANY_INDEX);
		msg.setState(Constant.MESSAGE_STATE_INDEX.PENDING);
		msg.setReferId(companyId);
		msg.setReferUserId(reUserId);
		Message message = messageDao.selectByParames(msg);
		//加入团队
		if(message != null){
			// 更新消息状态_1同意
			message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
			message.setIsRead(Constant.ONE);
			messageService.updateByPrimaryKeySelective(message);			
		}
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user.getCompanyId().equals(Constant.ZERO), Constant.ALREADY_HAS_COMPANY_ME);
		// 更新用户的团队信息
		user.setCompanyId(companyId);
		user.setJoinTime(new Date());
		userDao.updateByPrimaryKeySelective(user);
		
		DealEvent dealEvent = dealEventDao.selectByUserId(userId);
		if (dealEvent != null && dealEvent.getHasCompany().equals(0)) {
			dealEvent.setHasCompany(1);
			dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
	}

	/**
	 * 根据名称获取公司
	 * @author:zhugw
	 * @time:2017年11月21日 下午9:01:59
	 * @param userId
	 * @param name
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#selectCompanyByName(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Object isExist(Integer userId, String name) {
		Company company = dao.selectCompanyByName(name);
		JSONObject result = new JSONObject();
		if(company != null){
			result.put("company_id", company.getId());
			return result;
		}
		result.put("company_id", 0);
		return result;
	}

	/**
	 * web 初始化公司信息分页
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:48:46
	 * @param userId
	 * @param companyId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#selectCompanyInfo(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Object selectCompanyInfo(Integer userId, Integer companyId, Integer pageNo, Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user.getCompanyId().equals(companyId),Constant.PARAMS_ERROR+"，数据错误，请刷新重试！");
        List<User> res = userDao.selectPageCompanyMember(companyId,new Pagination<User>(pageNo,pageSize).getRowBounds());
        Pagination<User> pageInfo = new Pagination<User>(res);
        Company company = dao.selectByPrimaryKey(companyId);
        JSONObject obj = JSONObject.fromObject(company);
        obj.put("users", pageInfo);
		return obj;
	}

	/**
	 * 根据公司名称搜索公司列表
	 * @author:zhugw
	 * @time:2017年11月21日 上午9:39:50
	 * @param userId
	 * @param searchName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.CompanyService#selectListByCompanyName(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Object selectListByCompanyName(String searchName, Integer pageNum, Integer pageSize) {
		List<Company> result = dao.selectListByCompanyName(searchName,new Pagination<Company>(pageNum,pageSize).getRowBounds());
		Pagination<Company> pageInfo = new Pagination<Company>(result);
		return pageInfo;
	}

}
