package com.xmsmartcity.maintain.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xmsmartcity.orm.Pagination;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.xmsmartcity.maintain.dao.AbnormalRecordsMapper;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.CompanyMapper;
import com.xmsmartcity.maintain.dao.EvaluateMapper;
import com.xmsmartcity.maintain.dao.FaultInfoMapper;
import com.xmsmartcity.maintain.dao.FaultStoreMapper;
import com.xmsmartcity.maintain.dao.IndexUserFaultMapper;
import com.xmsmartcity.maintain.dao.MaintainInfoMapper;
import com.xmsmartcity.maintain.dao.MaintainOperateLogMapper;
import com.xmsmartcity.maintain.dao.MaterialLabourMapper;
import com.xmsmartcity.maintain.dao.ProjectMapper;
import com.xmsmartcity.maintain.dao.ProjectUserMapper;
import com.xmsmartcity.maintain.dao.ServiceTypeMapper;
import com.xmsmartcity.maintain.dao.UserMapper;
import com.xmsmartcity.maintain.dao.WorkOrderMapper;
import com.xmsmartcity.maintain.pojo.AbnormalRecords;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.Evaluate;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.MaintainInfo;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.MaterialLabour;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ProjectUser;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.pojo.WorkOrder;
import com.xmsmartcity.maintain.service.FaultInfoService;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.maintain.service.impl.fault_process.FaultProcessBase;
import com.xmsmartcity.maintain.service.impl.fault_process.FaultProcessPojo;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;
import com.xmsmartcity.util.Util;

@Service
public class FaultInfoServiceImpl extends BaseServiceImpl<FaultInfo> implements FaultInfoService {

	public FaultInfoServiceImpl(BaseDao<FaultInfo> dao) {
		super(dao);
	}

	@Autowired
	public FaultInfoMapper dao;

	@Autowired
	public UserMapper userDao;

	@Autowired
	public MaintainOperateLogMapper maintainOperateLogDao;
	
	@Autowired
	public MaintainInfoMapper maintainInfoDao;

	@Autowired
	public ServiceTypeMapper serviceTypeDao;

	@Autowired
	public MessageService messageService;

	@Autowired
	public ProjectMapper projectDao;

	@Autowired
	public ProjectUserMapper projectUserDao;
	
	@Autowired
	public MaterialLabourMapper materiaDao;
	
	@Autowired
	public WorkOrderMapper workOrderDao;
	
	@Autowired
	public IndexUserFaultMapper indexUserFaultDao; 
	
	@Autowired
	public EvaluateMapper evaluateDao;
	
	@Autowired
	public CompanyMapper companyDao;
	
	@Autowired
	public AbnormalRecordsMapper abnormalDao;
	
	@Autowired
	public FaultStoreMapper faultStoreDao;
	
	Logger logger = Logger.getLogger(FaultInfoServiceImpl.class);

	/**
	 * 获取报障单列表
	 * @author:zhugw
	 * @time:2017年12月1日 上午11:44:30
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getFaultInfoList(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<FaultInfo> getFaultInfoList(Integer state, Integer userId,Integer type, Integer startIndex, Integer pageSize) {
		state = state.equals(0) ? null : state;
		List<FaultInfo> results = null;
		Company company = companyDao.selectByResponserId(userId);
		Integer companyId = company != null ? company.getId() : null;
		if(Constant.APP_TYPE.A.equals(type)){
			results = dao.selectFaultList(companyId,state, userId, type, startIndex, pageSize);
		}else{
			results = dao.selectMaintainList(companyId,state, userId, type, startIndex, pageSize);
		}
		User user = userDao.selectByPrimaryKey(userId);
		for(FaultInfo fault : results){
			String projectPic = (StringUtils.isNotBlank(fault.getProjectPic())? fault.getProjectPic() : Constant.DEFAULT_PROJECT_PIC);
			fault.setProjectPic(projectPic);
			if(user.getPhone().equals(fault.getMaintainUserMobile())){
				if(1 == fault.getFaultState()
						|| 2 == fault.getFaultState()
						|| 3 == fault.getFaultState()){
					fault.setState(Constant.FAULT_INFO_STATE.WAIT_CONDUCT);
				}
			}
		}
		return results;
	}

	/**
	 * 创建报障单
	 * @author:zhugw
	 * @time:2017年4月17日 下午6:22:14
	 * @param faultInfo
	 * @return (non-Javadoc)
	 * @throws IOException
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#saveFaultInfo(com.xmsmartcity.maintain.pojo.FaultInfo)
	 */
	@Override
	@Transactional
	public FaultInfo saveFaultInfo(FaultInfo faultInfo) throws IOException {
		User user = userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (user == null) {
			logger.info("user id error : " + faultInfo.getUserId());
			Assert.state(false, Constant.PARAMS_ERROR);
		}
		Assert.state(StringUtils.isNotBlank(faultInfo.getIntroduce()) && StringUtils.isNotBlank(faultInfo.getPosition()),
				Constant.PARAMS_ERROR);
		//第三方报障判断
		if(Constant.FAULT_INFO_TYPE.OTHER_REPORTED == faultInfo.getFaultType()){
			Assert.state(StringUtils.isNotBlank(faultInfo.getFaultUserMobile())
			&& StringUtils.isNotBlank(faultInfo.getFaultUserName()),Constant.PARAMS_ERROR);
		}else{
			// 直接报障添加填报人为报障负责人
			ProjectUser pu = projectUserDao.selectByParames(faultInfo.getProjectId(), faultInfo.getUserId(),
					Constant.PROJECT_USER_TYPE_INDEX.B_INDEX, null, null);
			Assert.state(pu == null, "您是该项目乙方成员不能进行直接报障。");
//			faultInfo.setFaultUserMobile(user.getPhone());
//			faultInfo.setFaultUserName(user.getName());
		}
		faultInfo.setCreatetime(new Date());
		Assert.state(user.getCompanyId() != 0,"您还未加入团队,请先加入团队！");
		faultInfo.setCompanyId(user.getCompanyId());
		// 生成报障号
		Company company = companyDao.selectByPrimaryKey(faultInfo.getCompanyId());
		Assert.state(company != null,Constant.PARAMS_ERROR);
		String code = dao.selectMaxCode(faultInfo.getCompanyId());
		code = getFaultInfoCode(StringUtils.isNotEmpty(company.getShortName()) ? company.getShortName() : company.getName(),code);
		faultInfo.setCode(code);
		dao.insertSelective(faultInfo);// 保存报障信息
		String name = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
		// 保存维修信息
		MaintainOperateLog maintainOperateLog = new MaintainOperateLog(faultInfo.getId(),"", "提交了报障单.",
				Constant.PROJECT_USER_TYPE_INDEX.A_INDEX,Constant.MAINTAIN_STATE_INDEX.LOOK_DETAIL,faultInfo.getState(), new Date(),name,faultInfo.getUserId());
		maintainOperateLogDao.insertSelective(maintainOperateLog);
		// 新建报障单通知相关人员
		//第三方报障给负责人发信息
		Project project = projectDao.selectByPrimaryKey(faultInfo.getProjectId());
		if(Constant.FAULT_INFO_TYPE.OTHER_REPORTED == faultInfo.getFaultType()){
			if (userDao.selectByPhone(faultInfo.getFaultUserMobile()) != null) {
				// 给甲方负责人发消息
				messageService.sendMessage(faultInfo.getFaultUserMobile(), faultInfo.getUserId(),
						"【报障单 " + faultInfo.getCode() + "】" + name + "提交了报障单",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX, faultInfo.getId(), Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, name, 0);
			}
			// send phone msg
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
					faultInfo.getFaultUserMobile(), user.getId(), faultInfo.getId(), false,null);
		}
		// 如果报障人是甲方负责人就不用给甲方负责人推送消息
		if (!user.getId().equals(project.getOwnerId())) {
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_submit_fault_tpl_id_index,
					project.getOwnerPhone(), user.getId(), faultInfo.getId(), false,null);
		}
		
		ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		// 当前用户不是乙方负责人则发送消息
		User maintainUser = userDao.selectByPhone(serviceType.getMaintainUserMobile());
		if (maintainUser != null) {
			if (!maintainUser.getId().equals(user.getId())) {
				messageService.sendMessage(serviceType.getMaintainUserMobile(), faultInfo.getUserId(),
						"【报障单 " + faultInfo.getCode() + "】" + name + "提交了报障单",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX, faultInfo.getId(), Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, name, 0);
			}
		}
		// 如果报障人是乙方负责人就不用给乙方负责人推送消息
		if (!user.getPhone().equals(serviceType.getMaintainUserMobile())) {			
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_submit_fault_tpl_id_index,
					serviceType.getMaintainUserMobile(), user.getId(), faultInfo.getId(), false,null);
		}
		return faultInfo;
	}
	
	/**
	 * 生成报障单号
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:52:04
	 * @param companyName
	 * @param code
	 * @return
	 */
	public String getFaultInfoCode(String companyName,String code){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		String date_code = sdf.format(now);
		if(!StringUtils.isNotBlank(companyName.trim())){
			companyName = "DD";
		}
		String num = "0";
		if(StringUtils.isNotBlank(code)){
			num = code.substring(code.length()-3,code.length());
		}
		int addNum = Integer.valueOf(num);
		String code_hand = Util.getUpperPinYinHeadChar(companyName);
		if(code_hand.length() > 4){
			code_hand = code_hand.substring(0, 4);
		}
		code = code_hand+date_code+(addNum+1);
		return code;
	}

	/**
	 * 获取报障单详情
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:52:09
	 * @param faultInfoId
	 * @param userId
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getFaultInfoDetailById(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public FaultInfo getFaultInfoDetailById(Integer faultInfoId,Integer userId) {
		FaultInfo res = dao.selectFaultInfoById(faultInfoId);
		IndexUserFault obj = indexUserFaultDao.selectLastObj(faultInfoId);
		if(obj != null){
			res.setFaultState(obj.getType());
		}
		Assert.state(res != null,"获取报账单详情错误，请稍后重试！");
		res.setRole(0);
		res.setFaultCode(res.getCode());
		List<User> maintains = userDao.selectMaintainUsers(res.getId());
		if(maintains != null && maintains.size() > 0){
			res.setMaintainUsers(maintains);
			for(User u : maintains){
				u.setPassword("");
				u.setToken("");
				if(userId.equals(u.getId())){
					res.setRole(3);//维修工程师
				}
			}
		}else{
			List<User> users = new ArrayList<User>();
			ServiceType st = serviceTypeDao.selectByPrimaryKey(res.getServiceTypeId());
			User maintainUser = userDao.selectByPhone(st.getMaintainUserMobile());
			if(maintainUser != null){
				maintainUser.setPassword("");
				maintainUser.setToken("");
				users.add(maintainUser);
			}else{
				User user = new User();
				user.setPhone(st.getMaintainUserMobile());
				user.setName(st.getMaintainUserName());		
				users.add(user);
			}
			res.setMaintainUsers(users);
		}
		User user = userDao.selectByPrimaryKey(userId);
		List<ProjectUser> pus = projectUserDao.selectListByParames(res.getProjectId(), userId, Constant.APP_TYPE.A, null, null);
		Project project  = projectDao.selectByPrimaryKey(res.getProjectId());
		if(user.getPhone().equals(res.getMaintainUserMobile())){
			res.setRole(1);//乙方负责人
			if(1 == res.getFaultState()
					|| 2 == res.getFaultState()
					|| 3 == res.getFaultState()){
				res.setState(Constant.FAULT_INFO_STATE.WAIT_CONDUCT);
			}
		}//甲方负责人或报障单提交人
		else if(user.getPhone().equals(res.getFaultUserMobile()) 
				|| (Constant.ZERO == res.getFaultType()) && res.getUserId().equals(userId) ){
			res.setRole(5);//甲方报障单负责人
		}
		else if(project != null && userId.equals(project.getOwnerId())){
			res.setRole(4);//甲方项目负责人
		}else if(pus != null && pus.size() > 0){
			res.setRole(6);//甲方成员
		}
		Evaluate evaluate = evaluateDao.selectByFaultInfoId(faultInfoId);
		res.setEvaluate(evaluate);
		if(evaluate != null){
			Integer reSpeed = evaluate.getResSpeed();
			Integer maintainQuality = evaluate.getMaintainQuality();
			float aveEvaluate = (reSpeed+maintainQuality)/2;
			res.setAveEvaluate(aveEvaluate);
		}
		return res;
	}

	/**
	 * 获取报障说明列表
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:52:17
	 * @param faultInfoId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getMaintainInfoList(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<MaintainInfo> getMaintainInfoList(Integer faultInfoId,Integer startIndex,Integer pageSize) {
		List<MaintainInfo> list = maintainInfoDao.selectByFaultInfoId(faultInfoId,startIndex,pageSize);
		return list;
	}

	/**
	 * 保存工单及费用
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:52:38
	 * @param workOrder
	 * @throws IOException
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getMaterialLabourAndWorkOrder(com.xmsmartcity.maintain.pojo.WorkOrder)
	 */
	@Override
	@Transactional
	public void getMaterialLabourAndWorkOrder(WorkOrder workOrder) throws IOException {
		Assert.state(workOrder != null,Constant.PARAMS_ERROR);
		if(workOrder.getId()!=null && workOrder.getId() != 0){
			//update
			materiaDao.deleteByFaultInfo(workOrder.getFaultInfoId());
			List<MaterialLabour> materias = workOrder.getMaterias();
			if(materias != null && materias.size() > 0){
				materiaDao.insertBatch(materias);
			}
			workOrderDao.updateByPrimaryKeySelective(workOrder);
		}else{			
			List<MaterialLabour> materias = workOrder.getMaterias();
			if(materias != null && materias.size() > 0){
				materiaDao.insertBatch(materias);
			}
			workOrder.setCreatetime(new Date());
			workOrderDao.insertSelective(workOrder);
		}
		//执行流程
		faultInfoOperate(workOrder.getFaultInfoId(),11,null, null, null,null,null, workOrder.getUserId());
	}

	/**
	 * 操作流程
	 * @author:zhugw
	 * @time:2017年5月3日 下午2:30:13
	 * @param faultInfoId  报障单id
	 * @param type	报障流程操作
	 * 		1待接收	0:乙方负责人接收分派报障单 1:乙方负责人接收报障单  2:乙方负责人退回报障单 
	 * 		3待处理	3:乙方负责人转派报障单  4:乙方处理报障单  5:乙方负责人拒绝转派  6:乙方负责人增援  7:乙方负责人拒绝增援
	 *      2已退回	8:甲方同意乙方退回  9:甲方重新发起报障单  
	 *      4已接收	10:乙方请求转派  11:填写工单 12:无需工单
	 *      5待确认	13:工单不通过 14:工单通过 
	 *      6维修中	15:请求增援 16:维修说明 17:完成维修
	 *      7待验收	18:验收合格 19:验收不合格  
	 *      8待评价	20:工单评价
	 * @param reason  理由
	 * @param reUserId  转派/增援 给谁
	 * @param userId	当前用户id
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public void faultInfoOperate(Integer faultInfoId, Integer type,String appointmentTime, String reason,String reUserId,String reUserPhone,String pics,Integer userId) throws IOException {
		FaultInfo faultInfo = dao.selectByPrimaryKey(faultInfoId);			
		Assert.state(faultInfo != null);
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR);
		Integer[] types = {
				0,1,2,
				3,4,5,6,7,
				10,11,12};
		Integer[] unaccept_types={0,1,2};
		//处理时间异常判断
		if(ArrayUtils.contains(types,type)){
			//判断是否超时
			Date time = faultInfo.getUpdatetime();
			Calendar c = Calendar.getInstance();
			c.setTime(time);
			Long millis = c.getTimeInMillis();
			Integer interval_time = (int)(System.currentTimeMillis() - millis)/1000/60;//超时时间-分钟
			Integer overTime=0;//超时时间
			//待接收状态异常判断 一般 6H 紧急 2H 立刻 30M
			if(ArrayUtils.contains(unaccept_types, type)){
				Integer level = faultInfo.getLevel();
				int sixH = 6*60;
				int twoH = 2*60;
				int fiveM = 30;
				if (level == 0 && interval_time > sixH) {
					overTime=interval_time-sixH;
					AbnormalRecords obj = new AbnormalRecords(faultInfoId,0,faultInfo.getState(),overTime,new Date(),userId,userId,null);
					abnormalDao.insertSelective(obj);
				}
				if (level == 1 && interval_time > twoH) {
					overTime=interval_time-twoH;
					AbnormalRecords obj = new AbnormalRecords(faultInfoId,0,faultInfo.getState(),overTime,new Date(),userId,userId,null);
					abnormalDao.insertSelective(obj);
				}
				if (level == 2 && interval_time > fiveM) {
					overTime=interval_time-fiveM;
					AbnormalRecords obj = new AbnormalRecords(faultInfoId,0,faultInfo.getState(),overTime,new Date(),userId,userId,null);
					abnormalDao.insertSelective(obj);
				}
			}else{
				Integer oneH =60;
				//待处理、已接收状态 处理时间1H
				if(interval_time > oneH){
					overTime=interval_time-oneH;
					AbnormalRecords obj = new AbnormalRecords(faultInfoId,0,faultInfo.getState(),overTime,new Date(),userId,userId,null);
					abnormalDao.insertSelective(obj);
				}
			}
		}
		//执行流程类
		FaultProcessBase faultProcess = FaultProcessBase.getFaultProcess(type);
		FaultProcessPojo param = new FaultProcessPojo(faultInfoId, type, appointmentTime, reason, reUserId, reUserPhone, pics, userId);
		param.setFaultInfoImpl(this);
		faultProcess.executeProcess(param);
	}
	
	/**
	 * 保存维修说明 
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:52:48
	 * @param userId
	 * @param maintainInfo
	 * @throws IOException
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#saveMaintainInfo(java.lang.Integer, com.xmsmartcity.maintain.pojo.MaintainInfo)
	 */
	@Override
	@Transactional
	public void saveMaintainInfo(Integer userId,MaintainInfo maintainInfo) throws IOException {
		User user = userDao.selectByPrimaryKey(userId);
//		MaintainInfo maintainInfo = new MaintainInfo(faultInfoId,content,equipName,
//				Constant.APP_TYPE.B,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,new Date(),user.getName());
		maintainInfo.setType(Constant.APP_TYPE.B);
		maintainInfo.setState(Constant.MAINTAIN_STATE_INDEX.NO_DETAIL);
		maintainInfo.setCreatetime(new Date());
		maintainInfo.setUserName(user.getName());
		maintainInfo.setUpdatetime(new Date());
		maintainInfoDao.insertSelective(maintainInfo);
		//执行流程
		faultInfoOperate(maintainInfo.getFaultInfoId(), 16,null, null,null, null,null,userId);
	}

	/**
	 * 保存评价
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:52:54
	 * @param evaluate
	 * @throws IOException
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#saveEvaluate(com.xmsmartcity.maintain.pojo.Evaluate)
	 */
	@Override
	@Transactional
	public void saveEvaluate(Evaluate evaluate) throws IOException {
		Evaluate res_evaluate = evaluateDao.selectByRecord(evaluate);
		Assert.state(res_evaluate == null,Constant.PARAMS_ERROR);
		//新增评价记录
        evaluate.setCreatetime(new Date());
        evaluateDao.insertSelective(evaluate);
        //执行流程
        faultInfoOperate(evaluate.getFaultInfoId(), 20,null, null,null,null, null, evaluate.getUserId());
	}
	
	/*
	 * 查看现场列表
	 */

	@Override
	public List<MaintainOperateLog> getMaintainOperateLogList(Integer faultInfoId,Integer startIndex,Integer pageSize) {
		List<MaintainOperateLog> logs = maintainOperateLogDao.selectByFaultInfoId(faultInfoId,startIndex,pageSize);
		return logs;
	}

	/*
	 * 设置预约维保时间
	 */
	@Override
	public void setAppointmentTime(String appointmentTime, Integer faultInfoId, Integer userId) {
		Assert.state(StringUtils.isNotBlank(appointmentTime),"未选择预约处理时间,请选择预约处理时间。");
		Long appointment_time = Long.valueOf(appointmentTime);
		Date appointment_date = new Date();
		appointment_date.setTime(appointment_time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String a_time = sdf.format(appointment_date);
		//更新工单类型
		FaultInfo faultInfo = dao.selectByPrimaryKey(faultInfoId);
		faultInfo.setAppointmentTime(appointment_date);
		dao.updateByPrimaryKeySelective(faultInfo);
		
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "设置预约处理时间："+a_time,
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),faultInfo.getUserId());
		maintainOperateLogDao.insertSelective(maintainInfo);
		//给甲方负责人发消息
		messageService.sendMessage(faultInfo.getFaultUserMobile(),userId,
    			"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"设置预约处理时间："+a_time+",快去看看吧~",
    			Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
    			faultInfo.getProjectId(),Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
	}
	
	
	/**
	 * 获取报障列表 (维保超时待接收提醒)
	 */
	@Override
	public List<Map<String, Object>> selectListByToRecive() {
		return dao.selectListByToRecive();
	}
	
	/**
	 * 获取报障列表 (维保超时待处理，已接收提醒)  type进行区分  0 为已接收  1,2分别为请求转派和增援
	 */
	@Override
	public List<Map<String, Object>> selectListByRecive() {
		return dao.selectListByRecive();
	}

	/*
	 * 获取评分信息
	 */
	@Override
	public Evaluate selectEvaluate(Integer faultInfoId) {
		Evaluate obj = evaluateDao.selectByFaultInfoId(faultInfoId);
		return obj;
	}

	/*
	 * 报障详情相关
	 */
	@Override
	public Map<String, Object> selectFaultInfo(Integer faultInfoId, Integer userId) {
		Map<String,Object> res = dao.selectByFaultCount(faultInfoId);
		Map<String,Object> obj = workOrderDao.selectMapByFaultInfoId(faultInfoId);
		if(obj != null){
			FaultInfo faultInfo = dao.selectByPrimaryKey(faultInfoId);
			if(4 == faultInfo.getState()){
				res.put("noPass", true);
			}
			res.put("work_order", obj);
		}else{
			res.put("work_order", new Object());
		}
		return res;
	}

	/*
	 * 获取工单
	 */
	@Override
	public Map<String,Object> selectWorkOrder(Integer faultInfoId, Integer userId) {
		Map<String,Object> resObj = workOrderDao.selectMapByFaultInfoId(faultInfoId);
		List<MaterialLabour> materialPrices = new ArrayList<MaterialLabour>();
		List<MaterialLabour> labourPrices = new ArrayList<MaterialLabour>();
		List<MaterialLabour> otherPrices = new ArrayList<MaterialLabour>();
		Map<String,Object> res = new HashMap<String,Object>();
		if(resObj != null){
			List<MaterialLabour> resArr = materiaDao.selectListByFaultInfoId(faultInfoId);
			for(MaterialLabour obj : resArr){
				//人工费
				if( 1 == obj.getType()){
					labourPrices.add(obj);
				}
				//其他费用
				else if(2 == obj.getType()){
					otherPrices.add(obj);
				}
				//材料费
				else{
					materialPrices.add(obj);
				}
			}
		}else{
			Map<String,Object> work_order = new HashMap<String,Object>();;
			//work_order.put("total_price","");
			resObj = work_order;
		}
		res.put("materialPrices", materialPrices);
		res.put("labourPrices", labourPrices);
		res.put("otherPrices", otherPrices);
		res.put("workOrder", resObj);
		return res;
	}

	/*
	 * 获取报障库列表
	 */
	@Override
	public List<Map<String, Object>> selectFaultStoreList(Integer faultInfoId) {
		List<Map<String,Object>> res = faultStoreDao.selectListByFaultInfoId(faultInfoId);
		return res;
	}

	/*
	 * 获取报障库详情
	 */
	@Override
	public Map<String,Object> selectFaultStoreDetail(Integer faultStoreId) {
		Map<String,Object> res = faultStoreDao.selectMapByprimarkId(faultStoreId);
		return res;
	}

	/*
	 * 根据faultInfoId查询
	 */
	@Override
	public Map<String,Object> getFaultInfoHistoryList(Integer state, Integer userId, Integer faultInfoId,
			Integer startIndex, Integer pageSize) {
		List<FaultInfo> listRes = dao.selectListByServiceTypeId(state,faultInfoId,startIndex,pageSize);
		Map<String,Object> res = new HashMap<String,Object>();
		if(listRes != null && listRes.size() > 0){
			ServiceType st = serviceTypeDao.selectByPrimaryKey(listRes.get(0).getServiceTypeId());
			Assert.state(st != null,Constant.PARAMS_ERROR);
			Project project = projectDao.selectByPrimaryKey(st.getProjectId());
			Assert.state(project != null,Constant.PARAMS_ERROR);
			res.put("begin_time", project.getCreatetime().getTime());
			res.put("results", listRes);
		}
		return res;
	}

	@Override
	public List<FaultInfo> getSumFaultInfoList(Integer faultState,Integer state, Integer userId, Integer type, Integer startIndex,
			Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state( state != null && state >= 1 && state <= 3,Constant.PARAMS_ERROR);
		Assert.state(type != null &&(type == 0 || type == 1),Constant.PARAMS_ERROR);
		faultState = faultState == 0 ? null : faultState;
		List<FaultInfo> resList = dao.selectListByStateType(faultState,state,type,user.getCompanyId(),startIndex,pageSize);
		return resList;
	}

	/**
	 * 首页汇总进入报障维保列表
	 */
	@Override
	public List<FaultInfo> getFaultInfoListByHome(Integer faultState, Integer userId, Integer type, Integer startIndex,
			Integer pageSize) {
		Assert.state(type != null &&(type == 0 || type == 1),Constant.PARAMS_ERROR);
		faultState = faultState == 0 ? null : faultState;
		return dao.selectListByHome(faultState, type, userId, startIndex, pageSize);
	}

	/**
	 * 设备进入报障维保列表
	 */
	@Override
	public List<FaultInfo> getFaultInfoListByEquip(Integer faultState,Integer state, Integer userId, Integer startIndex,
			Integer pageSize,Integer equipId) {
		Assert.state( state != null && state >= 2 && state <= 3,Constant.PARAMS_ERROR);
		Assert.state(equipId != null,Constant.PARAMS_ERROR);
		faultState = faultState == 0 ? null : faultState;
		return dao.selectListByEquip(faultState, state, userId, startIndex, pageSize,equipId);
	}

	/**
	 * 项目进入报障维保列表
	 */
	@Override
	public List<FaultInfo> getFaultInfoListByProject(Integer faultState, Integer state, Integer userId, Integer type,
			Integer startIndex, Integer pageSize, Integer projectId) {
		Assert.state( state != null && state >= 2 && state <= 3,Constant.PARAMS_ERROR);
		Assert.state(type != null &&(type == 0 || type == 1),Constant.PARAMS_ERROR);
		Assert.state(projectId != null,Constant.PARAMS_ERROR);
		faultState = faultState == 0 ? null : faultState;
		return dao.selectListByProject(faultState, state, type, userId, startIndex, pageSize, projectId);
	}

	/**
	 * 获取维保人员列表
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:01:28
	 * @param faultInfoId
	 * @param userId
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getMaintainUserList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<User> getMaintainUserList(Integer faultInfoId, Integer userId) {
		List<User> maintains = userDao.selectMaintainUsers(faultInfoId);
//		if(maintains == null || maintains.size() < 1){
//			FaultInfo faultObj = dao.selectByPrimaryKey(faultInfoId);
//			ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(faultObj.getServiceTypeId());
//			maintains = new ArrayList<User>();
//			User user = new User();
//			user.setPhone(serviceType.getMaintainUserMobile());
//			user.setName(serviceType.getMaintainUserName());
//			maintains.add(user);
//		}
		return maintains;
	}

	/**
	 * 团队负责人获取相关报障单列表
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:01:45
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getcompanyPrincipalfaultList(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Map<String,Object> getcompanyPrincipalfaultList(Integer userId,Integer startIndex,Integer pageSize) {
		Company company = companyDao.selectByResponserId(userId);
		List<FaultInfo> results = new ArrayList<FaultInfo>();
		if(company != null){
			results = dao.selectFaultListByCompanyPrincipal(company.getId(), startIndex, pageSize);
			User user = userDao.selectByPrimaryKey(userId);
			for(FaultInfo fault : results){
				String projectPic = (StringUtils.isNotBlank(fault.getProjectPic())? fault.getProjectPic() : Constant.DEFAULT_PROJECT_PIC);
				fault.setProjectPic(projectPic);
				if(user.getPhone().equals(fault.getMaintainUserMobile())){				
					if(1 == fault.getFaultState()
							|| 2 == fault.getFaultState()
							|| 3 == fault.getFaultState()){
						fault.setState(Constant.FAULT_INFO_STATE.WAIT_CONDUCT);
					}
				}
			}
		}
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("isLeader", company != null ? true : false);
		res.put("results", results);
		return res;
	}
	
	/**
	 * 微信小程序创建报障单
	 * @author:
	 * @time:2017年4月17日 下午6:22:14
	 * @param faultInfo
	 * @return (non-Javadoc)
	 * @throws IOException
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#saveFaultInfo(com.xmsmartcity.maintain.pojo.FaultInfo)
	 */
	@Override
	@Transactional
	public FaultInfo saveXcxFaultInfo(FaultInfo faultInfo) throws IOException {
		User user = userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (user == null) {
			logger.info("user id error : " + faultInfo.getUserId());
			Assert.state(false, Constant.PARAMS_ERROR);
		}
		Assert.state(StringUtils.isNotBlank(faultInfo.getIntroduce()) && StringUtils.isNotBlank(faultInfo.getPosition()),
				Constant.PARAMS_ERROR);
		//获取是否第三方报障
		Integer faultType=projectDao.getFaultType(faultInfo.getProjectId(), user.getId());
		if (faultType==null) {
			faultType=1;
		}
		faultInfo.setFaultType(faultType);
		if(Constant.FAULT_INFO_TYPE.OTHER_REPORTED == faultInfo.getFaultType()){
			Assert.state(StringUtils.isNotBlank(faultInfo.getFaultUserMobile())
			&& StringUtils.isNotBlank(faultInfo.getFaultUserName()),Constant.PARAMS_ERROR);
		}else{
			// 直接报障添加填报人为报障负责人
			ProjectUser pu = projectUserDao.selectByParames(faultInfo.getProjectId(), faultInfo.getUserId(),
					Constant.PROJECT_USER_TYPE_INDEX.B_INDEX, null, null);
			Assert.state(pu == null, "您是该项目乙方成员不能进行直接报障。");
//			faultInfo.setFaultUserMobile(user.getPhone());
//			faultInfo.setFaultUserName(user.getName());
		}
		
		// 生成报障号，当前时间作为单号一部分
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");//日期流水号
		String date_code = sdf.format(now);
		int code = dao.selectFaultCodeMax("XCX"+date_code);
		String newCode="XCX";//小程序单标志
		newCode+=date_code;
		code+=1;
		DecimalFormat df = new DecimalFormat("00000");
		newCode+=df.format(code);
		faultInfo.setCode(newCode);
		faultInfo.setCreatetime(new Date());
		dao.insertSelective(faultInfo);// 保存报障信息
		String name = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
		// 保存维修信息
		MaintainOperateLog maintainOperateLog = new MaintainOperateLog(faultInfo.getId(),"", "提交了报障单",
				Constant.PROJECT_USER_TYPE_INDEX.A_INDEX,Constant.MAINTAIN_STATE_INDEX.LOOK_DETAIL,faultInfo.getState(), new Date(),name,faultInfo.getUserId());
		maintainOperateLogDao.insertSelective(maintainOperateLog);
		// 新建报障单通知相关人员
		//第三方报障给负责人发信息
		Project project = projectDao.selectByPrimaryKey(faultInfo.getProjectId());
		if(Constant.FAULT_INFO_TYPE.OTHER_REPORTED == faultInfo.getFaultType()){
			if (userDao.selectByPhone(faultInfo.getFaultUserMobile()) != null) {
				// 给甲方负责人发消息
				messageService.sendMessage(faultInfo.getFaultUserMobile(), faultInfo.getUserId(),
						"【报障单 " + faultInfo.getCode() + "】" + name + "提交了报障单",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX, faultInfo.getId(), Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, name, 0);
			} else {
				// send phone msg
				messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
						faultInfo.getFaultUserMobile(), user.getId(), faultInfo.getId(), false,null);
			}
		}else{
			// 如果报障人是甲方负责人就不用给甲方负责人推送消息
			if (!user.getId().equals(project.getOwnerId())) {
				if (userDao.selectByPhone(project.getOwnerPhone()) != null) {
					// 给甲方负责人发消息
					messageService.sendMessage(project.getOwnerPhone(), faultInfo.getUserId(),
							"【报障单 " + faultInfo.getCode() + "】" + name + "提交了报障单",
							Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX, faultInfo.getId(), Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
							faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, name, 0);
				} else {
					// send phone msg
					messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
							project.getOwnerPhone(), user.getId(), faultInfo.getId(), false,null);
				}
			}
		}
		ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		// 当前用户不是乙方负责人则发送消息
		User maintainUser = userDao.selectByPhone(serviceType.getMaintainUserMobile());
		if (maintainUser != null) {
			if (!maintainUser.getId().equals(user.getId())) {
				messageService.sendMessage(serviceType.getMaintainUserMobile(), faultInfo.getUserId(),
						"【报障单 " + faultInfo.getCode() + "】" + name + "提交了报障单",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX, faultInfo.getId(), Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, name, 0);
			}
		} else {
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
					serviceType.getMaintainUserMobile(), user.getId(), faultInfo.getId(), false,null);
		}
		// apiHelper.sendConten(template, userIdList, params);
		return faultInfo;
	}

	/**
	 * 获取报障列表(小程序)
	 * @time:2017年11月29日 下午2:01:07
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getFaultInfoListByXcx(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<FaultInfo> getFaultInfoListByXcx(Integer state, Integer userId, Integer type, Integer startIndex,
			Integer pageSize) {
		List<FaultInfo> results = null;
		Assert.state(Constant.APP_TYPE.A.equals(type), Constant.PARAMS_ERROR);
		results = dao.selectFaultListByXcx(state, userId, type, startIndex, pageSize);
		User user = userDao.selectByPrimaryKey(userId);
		for(FaultInfo fault : results){
			String projectPic = (StringUtils.isNotBlank(fault.getProjectPic())? fault.getProjectPic() : Constant.DEFAULT_PROJECT_PIC);
			fault.setProjectPic(projectPic);
			if(user.getPhone().equals(fault.getMaintainUserMobile())){				
				if(1 == fault.getFaultState()
						|| 2 == fault.getFaultState()
						|| 3 == fault.getFaultState()){
					fault.setState(Constant.FAULT_INFO_STATE.WAIT_CONDUCT);
				}
			}
		}
		return results;
	}

	/**
	 * 故障统计（新增故障、维修完成）
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:00:47
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getFaultStatistics(java.lang.Integer, java.util.Date, java.util.Date, java.lang.Integer)
	 */
	@Override
	public List<FaultInfo> getFaultStatistics(Integer userId, Date startTime, Date endTime,
			Integer state) {
		return dao.selectFaultStatistics(userId, startTime, endTime, state);
	}

	/**
	 * PC端打印报修单
	 */
	@Override
	public List<FaultInfo> getFaultListByPC(Integer userId,String time) {
		Company company=companyDao.selectByUserId(userId);
		Assert.state(company!=null,"请使用公司负责人账号登陆");
		Date date=DateUtils.StringToDate(time, DateUtils.formatStr_yyyyMMddHHmmss);
		return dao.selectFaultListByPC(date, company.getId());
	}
	
	/**
	 * 根据报修单ID和用户ID 查询是否可以验收和评价
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:53:19
	 * @param userId
	 * @param id
	 * @param type
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#getIsOperate(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public int getIsOperate(Integer userId, Integer id,Integer type) {
		return dao.getIsOperate(userId, id,type);
	}

	/**
	 * 根据时间查询设备历史报障单
	 * @param startDate
	 * @param endDate
	 * @param equipId
	 * @return
	 */
	@Override
	public List<FaultInfo> selectListByEquipIdAndTime(Date startDate, Date endDate, Integer equipId) {
		List<FaultInfo> results = dao.selectListByTime(startDate,endDate,equipId);
		return results;
	}

	/**
	 * web 报障单列表
	 * @param projectId
	 * @param userId
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectWebFaultList(Integer projectId, Integer userId,String state,Date startTime,Date endTime, Integer pageNo, Integer pageSize) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		startTime = cal.getTime();
		cal.setTime(endTime);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		endTime = cal.getTime();
		Company company = companyDao.selectByResponserId(userId);
		Integer companyId = company != null ? company.getId() : null; 
		List<Map<String, Object>> results = dao.selectWebFaultList(companyId,projectId,userId,state,startTime,endTime,new Pagination<List<Map<String,Object>>>(pageNo,pageSize).getRowBounds());
		return results;
	}

	/**
	 * web 报障单列表(公司权限)
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:00:29
	 * @param projectId
	 * @param equipId
	 * @param userId
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.FaultInfoService#selectWebFaultListByEquip(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> selectWebFaultListByEquip(Integer projectId, Integer equipId, Integer userId, String state, Date startTime, Date endTime, Integer pageNo, Integer pageSize) {
		List<Map<String, Object>> results =dao.selectWebFaultListByEquip(projectId,equipId,userId,state,startTime,endTime,new Pagination<List<Map<String,Object>>>(pageNo,pageSize).getRowBounds());
		return results;
	}

	/**
	 * 报障维修说明
	 * @param faultInfoId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public Object getMaintainInfoByPage(Integer faultInfoId, Integer pageNum, Integer pageSize) {
		List<MaintainInfo> list = maintainInfoDao.selectByFaultInfoPage(faultInfoId,new Pagination<MaintainInfo>(pageNum,pageSize).getRowBounds());
		Pagination<MaintainInfo> pageInfo = new Pagination<MaintainInfo>(list);
		return pageInfo;
	}
}
