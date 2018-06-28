package com.xmsmartcity.maintain.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.extra.aliyun.OssUtil;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.CompanyMapper;
import com.xmsmartcity.maintain.dao.DealEventMapper;
import com.xmsmartcity.maintain.dao.FaultInfoMapper;
import com.xmsmartcity.maintain.dao.MessageMapper;
import com.xmsmartcity.maintain.dao.ProjectMapper;
import com.xmsmartcity.maintain.dao.ProjectUserMapper;
import com.xmsmartcity.maintain.dao.ServiceTypeMapper;
import com.xmsmartcity.maintain.dao.UserMapper;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ProjectUser;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.maintain.service.ProjectService;
import com.xmsmartcity.orm.Pagination;
import com.xmsmartcity.util.AES;
import com.xmsmartcity.util.Constant;

@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project> implements ProjectService {

	public ProjectServiceImpl(BaseDao<Project> dao) {
		super(dao);
	}

	@Autowired
	private ProjectMapper dao;

	@Autowired
	private UserMapper userDao;

	@Autowired
	private ServiceTypeMapper serverTypeDao;

	@Autowired
	private ProjectUserMapper projectUserDao;

	@Autowired
	private DealEventMapper dealEventDao;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MessageMapper messageDao;

	@Autowired
	private FaultInfoMapper faultInfoDao;
	
	@Autowired
	private CompanyMapper companyDao;
	
	/**
	 * 根据名称查询项目
	 */
	@Override
	public List<Project> selectProjectList(Integer userId,String projectName, Integer startIndex, Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR);
		projectName = StringUtils.isNotBlank(projectName) ? projectName:null;
		List<Project> resProject = dao.selectCompanyProject(userId,projectName,user.getCompanyId(),startIndex,pageSize);
		if (resProject != null && resProject.size() > 0) {
			// 遍历添加服务内容
			for (Project p : resProject) {
				List<ServiceType> serObj = new ArrayList<ServiceType>();
				if(Constant.APP_TYPE.A.equals(p.getType())){
					serObj = serverTypeDao.selectByProjectId(p.getId());
				}else{
					serObj = serverTypeDao.selectListByUnEqUser(p.getId(),userId,user.getCompanyId());
				}
				if (serObj != null && serObj.size() > 0) {
					p.setServices(serObj);
				}
			}
		}
		return resProject;
	}

	/**
	 * 创建项目
	 * 
	 * @author:zhugw
	 * @time:2017年4月11日 上午9:35:43
	 * @param project
	 */
	@Override
	@Transactional
	public Project createProject(Project project) throws IOException {
		if (StringUtils.isBlank(project.getPic())) {
			project.setPic(OssUtil.PROJECT_IMG+Constant.DEFAULT_PROJECT_PIC);
		}
		Assert.state(StringUtils.isNotBlank(project.getName()), "项目名称不能为空，请重新填写");
		Assert.state(project.getMaintainContract() != null && StringUtils.isNotBlank(project.getProvince())
				&& StringUtils.isNotBlank(project.getCity()) && StringUtils.isNotBlank(project.getArea())
				&& StringUtils.isNotBlank(project.getAddress()) , Constant.PARAMS_ERROR);
		User user = userDao.selectByPrimaryKey(project.getCreateUserId());
		Assert.state(user != null, Constant.PARAMS_ERROR);
		Assert.state(user.getCompanyId() != null && !user.getCompanyId().equals(0),"您还未加入团队,请先加入团队!");
		
		List<ServiceType> services = project.getServices();
		Assert.state(services.size() > 0,Constant.PARAMS_ERROR);
		project.setCreatetime(new Date());
		project.setState(0);// 默认未确认
		project.setItemType(1);
		//判断甲方负责人是否已经注册过
		User faultUser = userDao.selectByPhone(project.getOwnerPhone());
		if(faultUser != null){
			project.setCompanyId(faultUser.getCompanyId());
			project.setOwnerId(faultUser.getId());
			List<User> users = userDao.selectUsersByCompanyId(faultUser.getCompanyId(), null, null);
			for (ServiceType service : services) {
				//过滤甲方成员
				for(User u : users){
					Assert.state(!u.getPhone().equals(service.getMaintainUserMobile()),"您团队的人员不能成为”"+service.getName()+"“服务类型的维修负责人。");
				}
			}
		}
		dao.insertSelective(project);
		for (ServiceType service : services) {
			service.setProjectId(project.getId());
		}
		// 批量保存服务内容
		serverTypeDao.insertBatch(services);
		if(faultUser!=null){
			if(user.getId().equals(faultUser.getId())){
				// 添加项目负责人
				ProjectUser projectUser = new ProjectUser(1, project.getId(), faultUser.getId(), Constant.PROJECT_USER_TYPE_INDEX.A_INDEX, 0);
				projectUserDao.insertSelective(projectUser);
				DealEvent dealEvent = dealEventDao.selectByUserId(user.getId());
				if(dealEvent != null && dealEvent.getHasProject() == 0){
					dealEvent.setHasProject(1);
					dealEventDao.updateByPrimaryKeySelective(dealEvent);// 更新表
				}
			}else{
				//发送甲方消息
				messageService.sendMessage(faultUser.getPhone(), user.getId(),
						"邀请您成为“" + project.getName() + "”项目报障方负责人，请尽快确认噢~",
						Constant.MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX, project.getId(),
						Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, project.getId(), Constant.MESSAGE_STATE_INDEX.PENDING,
						user.getName(), 1);								
			}
		}else{
			// 发送短信
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,Constant.MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX,
					project.getOwnerPhone(), user.getId(), project.getId(), false,null);
		}
		//给乙方发送消息
		for (ServiceType service : services) {
			User maintain_user = userDao.selectByPhone(service.getMaintainUserMobile());
			if (maintain_user != null) {
				if(user.getId().equals(maintain_user.getId())){
					// 添加项目负责人
					ProjectUser projectUser = new ProjectUser(1, project.getId(), maintain_user.getId(), Constant.PROJECT_USER_TYPE_INDEX.B_INDEX, service.getId());
					projectUserDao.insertSelective(projectUser);
					DealEvent dealEvent = dealEventDao.selectByUserId(user.getId());
					service.setState(1);
					serverTypeDao.updateByPrimaryKeySelective(service);
					if(dealEvent != null && dealEvent.getHasProject() == 0){
						dealEvent.setHasProject(1);
						dealEventDao.updateByPrimaryKeySelective(dealEvent);// 更新表
					}
				}else{					
					messageService.sendMessage(maintain_user.getPhone(), user.getId(),
							"邀请您成为“" + project.getName() + "”项目维保方负责人，请尽快确认噢~",
							Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX, service.getId(),
							Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, project.getId(), Constant.MESSAGE_STATE_INDEX.PENDING,
							user.getName(), 1);
				}
			} else {
				// 发送短信
				messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX,
						service.getMaintainUserMobile(), user.getId(), service.getId(), false,null);
			}
		}
		return project;
	}

	/**
	 * 查询项目详情
	 */
	@Override
	public Project getProjectDetail(Integer userId, Integer projectId, Integer type) {
		Project project = dao.selectByPrimaryKey(projectId);
		Assert.state(userId != null && type != null,Constant.PARAMS_ERROR);
		if(userId.equals(project.getOwnerId())){
			project.setIs_leader(1);
		}
		project.setType(type);
		User user = userDao.selectByPrimaryKey(project.getOwnerId());
		if(user != null){
			project.setUserHeadPic(user.getHeadPic());
		}
		Company company = companyDao.selectByResponserId(userId);
		project.setIs_company_leader(company != null ? 1:0);
		Integer companyId = company != null ? company.getId():null;
		//判断甲乙方，获取不同的服务内容
		List<ServiceType> sers = null;
		if(type.equals(Constant.APP_TYPE.A)){
			sers = serverTypeDao.selectByProjectId(projectId);
			for(ServiceType st : sers){
				User maintainUser = userDao.selectByPhone(st.getMaintainUserMobile());
				st.setUserHeadPic(maintainUser != null ? maintainUser.getHeadPic() :OssUtil.USER_IMG+Constant.DEFAULT_HEAD_PIC);
				if(maintainUser != null && st.getId() != 0 && st.getId() != null){
					ServiceType resServer = serverTypeDao.selectByParams(projectId, 1, maintainUser.getId(), st.getId());
					st.setCanEdit(resServer != null ? 1 : 0);
				}
			}
			List<ProjectUser> pus = projectUserDao.selectListByParames(projectId, null, type, null, null);
			project.setProjectUsers(pus);
		}else{
			List<ProjectUser> pus = projectUserDao.selectByCompanyPrincipal(projectId, userId, type,companyId);
			if(pus.size() > 0 ){
				sers = new ArrayList<ServiceType>();
				for(ProjectUser pu : pus){
					if(pu == null){
						continue;
					}
					ServiceType st = serverTypeDao.selectByPrimaryKey(pu.getServiceTypeId());
					List<ProjectUser> pusers = projectUserDao.selectListByParames(st.getProjectId(), null, type, null, st.getId());
					st.setProjectUsers(pusers);
					st.setIs_leader(pu.getIsLeader());
					User maintainUser = userDao.selectByPhone(st.getMaintainUserMobile());
					st.setUserHeadPic(maintainUser != null ? maintainUser.getHeadPic() :OssUtil.USER_IMG+Constant.DEFAULT_HEAD_PIC);
					if(pu.getIsLeader() == 1){
						project.setIs_leader(1);
					}
					sers.add(st);
				}
			}
		}
		Project res = dao.selectCountNum(projectId);
		if(res != null){
			project.setAveEvaluate(res.getAveEvaluate());
			project.setAveTimes(res.getAveTimes());
		}
		project.setServices(sers);
		Company companyObj = companyDao.selectByPrimaryKey(project.getCompanyId());
		if(companyObj != null){
			project.setCompanyName(companyObj.getName());
		}
		return project;
	}

	/**
	 * 移除项目成员
	 * 
	 * @author:zhugw
	 * @time:2017年4月11日 下午7:52:59
	 * @param userId
	 * @param projectId
	 * @param type
	 * @param serviceTypeId
	 * @param delUserId
	 */
	@Override
	public int delUserByUserId(Integer userId, Integer projectId, Integer type, Integer serviceTypeId,
			String delUserId) {
		String[] delUsers = delUserId.split(",");
		int res = 0;
		for(String del_user : delUsers){
			Integer del_user_id = Integer.valueOf(StringUtils.trim(del_user));
			ProjectUser delObj = projectUserDao.selectByParames(projectId, del_user_id, type, null, serviceTypeId);
			Assert.state(delObj != null,Constant.PARAMS_ERROR);
			Assert.state(delObj.getIsLeader() != 1, "该用户为项目负责人不能删除。");
			ProjectUser leaderObj = projectUserDao.selectByParames(projectId, userId, type, 1, serviceTypeId);
			Assert.state(leaderObj != null, "您不是该项目负责人不能删除项目成员。");
			// 判断执行人员是否是负责人,判断被删除人员是否有未完成的报障
			ProjectUser pu = projectUserDao.selectByParames(projectId, userId, type, 1, serviceTypeId);
			Assert.state(pu != null,Constant.NO_PROJECT_POWER);
			Integer count = faultInfoDao.checkUnfinished(del_user_id, projectId, serviceTypeId);
			Assert.state(count == 0,Constant.NO_FINISH_FAULTINFO);
			// 假删除项目用户
			delObj.setIsDel(1);
			res = projectUserDao.updateByPrimaryKeySelective(delObj);
			
			User delUser=userDao.selectByPrimaryKey(del_user_id);
			
			Project project =dao.selectByPrimaryKey(projectId);
			messageService.sendMessage(delUser.getPhone(), userId, "您已经被移出" + project.getName(),
					Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX, projectId, Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,
					projectId, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null, 1);
			// 更新用户项目信息
			DealEvent dealEvent = dealEventDao.selectByUserId(del_user_id);
			if (dealEvent != null && dealEvent.getHasProject() == 1) {
				List<ProjectUser> resList= projectUserDao.selectListByParames(null, del_user_id, null, null, null);
				if(resList == null || resList.size() < 1){
					dealEvent.setHasProject(0);
					dealEventDao.updateByPrimaryKeySelective(dealEvent);				
				}
			}
		}
		return res;
	}

	/**
	 * 短信邀请项目成员  甲方/乙方邀请成员
	 * 
	 * @author:zhugw
	 * @time:2017年4月11日 下午4:52:41
	 * @param phone
	 * @param type
	 * @param projectId
	 * @param userId
	 * @throws IOException
	 */
	@Override
	public void inviteUserJoin(String phone, Integer type, Integer projectId, Integer userId, Integer serviceTypeId)
			throws IOException {
		//查看是否已加入团队
		User inviteUser = userDao.selectByPrimaryKey(userId);
		Assert.state(!inviteUser.getCompanyId().equals(0), "您还未加入团队不能邀请成员。");
//		ProjectUser projectUser = projectUserDao.selectByUserIdAndProjectId(projectId, userId, type, null);
//		Assert.state(projectUser != null, "您还未加入该项目，不能邀请该项目成员。");
		Project project = dao.selectByPrimaryKey(projectId);
		ServiceType serviceType = null;
		// 判断甲乙方
		Integer msgType = Constant.MESSAGE_TYPE_INDEX.A_INVITED_PEOPLE_JOIN_PROJECT_INDEX;
		Integer referId = projectId;
		if (type.equals(Constant.APP_TYPE.B)) {
			serviceType = serverTypeDao.selectByPrimaryKey(serviceTypeId);
			msgType = Constant.MESSAGE_TYPE_INDEX.B_INVITED_PEOPLE_JOIN_PROJECT_INDEX;
			referId = serviceTypeId;
		}
		String[] phons = phone.split(",");
		for(String p : phons){
			User user = userDao.selectByPhone(p);
			if (user != null) {
//				ProjectUser pu = projectUserDao.selectByUserIdAndProjectId(projectId,user.getId(), null, null);
//				Assert.state(pu == null, "无法邀请，对方是该项目" + (pu != null && pu.getType() == 0 ? "乙方" : "甲方") + "成员");
				String content = serviceType == null ? "邀您成为" + project.getName() + "项目甲方成员，请尽快确认噢~"
						: "邀您成为" + project.getName() + "项目乙方" + serviceType.getName() + "成员,请尽快确认噢~";
				messageService.sendMessage(p, userId, content, msgType, referId,
						Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, projectId, Constant.MESSAGE_STATE_INDEX.PENDING,
						inviteUser.getName(), 1);
			}
			int refer_id = type.equals(Constant.APP_TYPE.B) ? serviceTypeId : 0;
			String inviteToken = projectId+"_"+p+"_"+userId+"_"+type+"_"+refer_id;
			System.out.println(referId+"============="+inviteToken+"=============================");
			inviteToken = AES.encrypt(inviteToken);//AES加密后的companyId+phone
			String webPath = "http://"+Constant.hostSite+"/dweibao/join-pm/"+inviteToken;
			// 发送短信邀请
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,msgType, p, userId, referId, false,webPath);
		}
	}

	/**
	 * 获取我的项目列表
	 */
	@Override
	public List<Project> selectProjectList(Integer userId, Integer startIndex, Integer pageSize) {
		List<Project> resProject = null;
		Company company = companyDao.selectByResponserId(userId);
		if(company != null){
			resProject = dao.selectByCompanyPrincipal(company.getId(),userId,startIndex, pageSize);
			if (resProject != null && resProject.size() > 0) {
				// 遍历添加服务内容
				for (Project p : resProject) {
					p.setIs_company_leader(1);
					List<ServiceType> serObj = null;
					if (p.getType() < 1) {
						serObj = serverTypeDao.selectByProjectId(p.getId());
					} else {
						serObj = serverTypeDao.selectByCompanyPrincipal(p.getId(),p.getType(),company.getId());
					}
					if (serObj != null && serObj.size() > 0) {
						p.setServices(serObj);
					}
				}
			}
		}else{			
			resProject = dao.selectProjectList(userId, startIndex, pageSize);
			if (resProject != null && resProject.size() > 0) {
				// 遍历添加服务内容
				for (Project p : resProject) {
					List<ServiceType> serObj = null;
					if (p.getType() < 1) {
						serObj = serverTypeDao.selectByProjectId(p.getId());
					} else {
						serObj = serverTypeDao.selectListByParams(p.getId(),p.getType(),userId);
					}
					if (serObj != null && serObj.size() > 0) {
						p.setServices(serObj);
					}
				}
			}
		}
		return resProject;
	}

	/**
	 * 邀请项目成员
	 * 
	 * @author:zhugw
	 * @time:2017年4月11日 下午6:37:10
	 * @param inUserId
	 * @param type
	 * @param projectId
	 * @param userId
	 */
	@Override
	public void inviteUserJoinById(Integer inUserId, Integer type, Integer projectId, Integer userId,
			Integer serviceTypeId) {
		// 获取负责人信息
		ProjectUser projectUser = projectUserDao.selectByUserIdAndProjectId(projectId, userId, type, 1);
		Assert.state(projectUser != null, "您不是该项目负责人，您不能邀请该项目成员。");
		User user = userDao.selectByPrimaryKey(inUserId);// 获取被邀请的成员信息
		Assert.state(user != null, Constant.PARAMS_ERROR);
		User inviteUser = userDao.selectByPrimaryKey(userId);
		ProjectUser pu = projectUserDao.selectByUserIdAndProjectId(user.getId(), projectId, null, null);
		Assert.state(pu == null, "无法邀请，对方是该项目" + (pu != null && pu.getType() == 0 ? "乙方" : "甲方") + "成员");
		Project project = dao.selectByPrimaryKey(projectId);
		ServiceType serviceType = null;
		// 判断甲乙方
		Integer msgType = Constant.MESSAGE_TYPE_INDEX.A_INVITED_PEOPLE_JOIN_PROJECT_INDEX;
		Integer referId = projectId;
		if (type.equals(Constant.APP_TYPE.B)) {
			serviceType = serverTypeDao.selectByPrimaryKey(serviceTypeId);
			msgType = Constant.MESSAGE_TYPE_INDEX.B_INVITED_PEOPLE_JOIN_PROJECT_INDEX;
			referId = serviceTypeId;
		}
		// 更新用户的团队信息
		//if (user.getHeadPic().equals(Constant.DEFAULT_HEAD_PIC)) {
		//	userDao.updateByPrimaryKeySelective(user);
		//}
		String content = serviceType == null ? "邀您成为" + project.getName() + "项目甲方成员，请尽快确认噢~"
				: "邀您成为" + project.getName() + "项目乙方" + serviceType.getName() + "成员,请尽快确认噢~";
		messageService.sendMessage(user.getPhone(), userId, content, msgType,referId ,Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 
				projectId, Constant.MESSAGE_STATE_INDEX.PENDING,inviteUser.getName(), 1);
	}

	/**
	 * 保存项目信息
	 * @author:zhugw
	 * @time:2017年4月17日 下午3:40:21
	 * @param project
	 * @param userId
	 * @throws IOException 
	 */
	@Override
	@Transactional
	public void saveProject(Project project,Integer userId) throws IOException {
		Assert.state(project.getId() > 0,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(project.getName()), "项目名称不能为空，请重新填写");
		Assert.state(project.getMaintainContract() != null && StringUtils.isNotBlank(project.getProvince())
				&& StringUtils.isNotBlank(project.getCity()) && StringUtils.isNotBlank(project.getArea())
				&& StringUtils.isNotBlank(project.getAddress()) && StringUtils.isNotBlank(project.getLat())
				&& StringUtils.isNotBlank(project.getLng()), Constant.PARAMS_ERROR);
//		Project oldProject = dao.selectByPrimaryKey(project.getId());
//		if(oldProject.getOwnerId() != 0){
//			Assert.state(oldProject.getOwnerId().equals(userId),"您不是该项目负责人不能修改项目信息，请刷新重试！");
//		}else{
//			Assert.state(oldProject.getCreateUserId().equals(userId),"您不是该项目负责人不能修改项目信息，请刷新重试！");
//		}
		User currUser = userDao.selectByPrimaryKey(userId);
		//判断是否是负责人
		ProjectUser pu = projectUserDao.selectByParames(project.getId(), userId, null, 1, project.getServiceTypeId());
		Assert.state(pu != null,"您不是该项目负责人不能修改项目信息，请刷新重试！");
		//获取服务内容
		List<ServiceType> services = project.getServices();
		Assert.state(services.size() > 0,"服务内容不能为空!");
		User user = userDao.selectByPhone(project.getOwnerPhone());
		List<User> users = new ArrayList<User>();
		if(user != null){
			users = userDao.selectUsersByCompanyId(user.getCompanyId(), null, null);			
		}
		//update or insert
		for(ServiceType service : services){
			//过滤甲方成员
			for(User u : users){
				Assert.state(!u.getPhone().equals(service.getMaintainUserMobile()),"您团队的人员不能成为”"+service.getName()+"“服务类型的维修负责人。");
			}
			ServiceType serObj = serverTypeDao.selectByPrimaryKey(service.getId());
			if(serObj != null){
				//服务未生效这通知相关人员
				if(!serObj.getState().equals(Constant.ONE)){
					service.setState(0);//0 未生效
					serverTypeDao.updateByPrimaryKeySelective(service);
					User maintainUser = userDao.selectByPhone(service.getMaintainUserMobile());
					if(maintainUser != null){						
						if(!service.getMaintainUserMobile().equals(currUser.getPhone())){
							ProjectUser projectUser = projectUserDao.selectByParames(project.getId(), maintainUser.getId(), Constant.APP_TYPE.B, null, project.getServiceTypeId());
							if(projectUser == null){
								messageService.sendMessage(service.getMaintainUserMobile(), userId,
										"邀请您成为“" + project.getName() + "”项目维保方负责人，请尽快确认噢~",
										Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX, service.getId(),
										Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, project.getId(), Constant.MESSAGE_STATE_INDEX.PENDING,
										currUser.getName(), 1);
							}
						}else{
							messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,
									Constant.PHONE_MESSAGE_INDEX.user_invitation_project_b_tpl_id_index, 
									service.getMaintainUserMobile(), userId, service.getId(), false,null);
						}
					}
					//移除之前通知的人员
					messageService.delMessageByParams(serObj.getMaintainUserMobile(),
							null,Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX,
							service.getId(),0,Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,Constant.MESSAGE_STATE_INDEX.PENDING,project.getId());
				}
			}else{
				service.setProjectId(project.getId());
				serverTypeDao.insertSelective(service);
				User maintainUser = userDao.selectByPhone(service.getMaintainUserMobile());
				if(maintainUser != null){					
					messageService.sendMessage(service.getMaintainUserMobile(), userId,
							"邀请您成为“" + project.getName() + "”项目维保方负责人，请尽快确认噢~",
							Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX, service.getId(),
							Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, project.getId(), Constant.MESSAGE_STATE_INDEX.PENDING,
							currUser.getName(), 1);
				}else{
					messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,
							Constant.PHONE_MESSAGE_INDEX.user_invitation_project_b_tpl_id_index, 
							service.getMaintainUserMobile(), userId, service.getId(), false,null);
				}
			}
		}
		//update project infomation
		dao.updateByPrimaryKeySelective(project);
		
		//通知甲方负责人
		User faultUser = userDao.selectByPhone(project.getOwnerPhone());
		if(faultUser != null){
			if(!faultUser.getPhone().equals(currUser.getPhone())){			
				ProjectUser projectUser = projectUserDao.selectByParames(project.getId(), faultUser.getId(), Constant.APP_TYPE.A, null, project.getServiceTypeId());
				if(projectUser == null){
					messageService.sendMessage(project.getOwnerPhone(), userId,
							"邀请您成为“" + project.getName() + "”项目报障方负责人，请尽快确认噢~",
							Constant.MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX, project.getId(),
							Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, project.getId(), Constant.MESSAGE_STATE_INDEX.PENDING,
							currUser.getName(), 1);
				}
			}
		}else{
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,
					Constant.PHONE_MESSAGE_INDEX.user_invitation_project_a_tpl_id_index, 
					project.getOwnerPhone(), userId, project.getId(), false,null);
		}
		
	}

	/**
	 * 获取用户关联的项目及所有服务内容
	 * @author:zhugw
	 * @time:2017年4月19日 下午4:14:05
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<Project> selectFaultProjectList(Integer userId, Integer startIndex, Integer pageSize) {
		List<Project> resProject = dao.selectProjectList(userId, startIndex, pageSize);
		if (resProject != null && resProject.size() > 0) {
			// 遍历添加服务内容
			for (Project p : resProject) {
				List<ServiceType> serObj = serverTypeDao.selectByProjectId(p.getId());
				if (serObj != null && serObj.size() > 0) {
					p.setServices(serObj);
				}
			}
		}
		return resProject;
	}
	
	@Override
	public List<User> getProjectMember(Integer type, Integer projectId, Integer userId, Integer serviceTypeId,
			Integer startIndex, Integer pageSize) {
		List<User> results = null;
		if(type.equals(Constant.APP_TYPE.A)){
			results = userDao.selectListByParames(projectId,null,type,null,startIndex,pageSize);
		}else{
			results = userDao.selectListByParames(projectId, null, type, serviceTypeId, startIndex, pageSize);
		}
		return results == null ? new ArrayList<User>() : results;
	}

	/*
	 * 退出项目
	 * 不是项目负责人，
	 * 没有保障
	 */
	@Override
	public void outOfProject(Integer userId, Integer projectId, Integer type, Integer serviceTypeId) {
		serviceTypeId = serviceTypeId == null || serviceTypeId == 0 ? null : serviceTypeId;
		ProjectUser pu = projectUserDao.selectByParames(projectId, userId, type, null, serviceTypeId);
		Assert.state(pu != null,Constant.PARAMS_ERROR);
		Assert.state(pu.getIsLeader() != null && pu.getIsLeader().equals(0),"您是该项目的负责人，暂不能退出项目。");
		//判断是否有报障
		Integer count = faultInfoDao.checkUnfinished(userId, projectId, serviceTypeId);
		Assert.state(count == 0,Constant.NO_FINISH_FAULTINFO);
		ProjectUser leaderUser = projectUserDao.selectByParames(projectId, null, type, 1, serviceTypeId);
		Integer referId = serviceTypeId != null ?serviceTypeId:projectId;
		//通知项目乙方负责人
		User user = userDao.selectByPrimaryKey(leaderUser.getUserId());
		messageService.sendMessage(user.getPhone(), userId, "用户请求退出项目，请快去处理吧~", Constant.MESSAGE_TYPE_INDEX.QUIT_PROJECT, 
				referId, Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, projectId, Constant.MESSAGE_STATE_INDEX.PENDING, user.getName(),1);
	}

	/*
	 * 移交项目
	 */
	@Override
	public void transferProject(Integer userId, Integer type, Integer projectId, Integer reUserId,Integer serviceTypeId) {
		User user = userDao.selectByPrimaryKey(userId);
		User reUser = userDao.selectByPrimaryKey(reUserId);
		Assert.state(user != null && reUser != null,Constant.PARAMS_ERROR);
		Assert.state(!userId.equals(reUserId),"该用户为项目负责人，不能移交！");
		Assert.state(user.getCompanyId().equals(reUser.getCompanyId()),"负责人与移交人员非同一团队，请重新移交项目");
		Integer referId = projectId;
		Project project = dao.selectByPrimaryKey(projectId);
		Integer messageType = Constant.MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX;
		String content = "将" + project.getName() + "项目甲方负责人移交给您，请尽快确认噢~";
		ServiceType serviceType = null;
		if (type == Constant.PROJECT_USER_TYPE_INDEX.B_INDEX) {
			// 乙方移交 进行判断
			// 他是否有未完成的报障			
			//Integer count = faultInfoDao.checkUnfinished(userId, projectId, serviceTypeId);
			//Assert.state(count == 0,"您当前还有该项目未完成报障，不能进行移交。");
			//通知
			serviceType = serverTypeDao.selectByPrimaryKey(serviceTypeId);
			content = "将" + project.getName() + "项目"
					+ serviceType.getName() + "乙方负责人移交给您，请尽快确认噢~";
			messageType = Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX;
			referId = serviceTypeId;
		}
		ProjectUser projectUser = projectUserDao.selectByParames(projectId, reUserId, null, 1, null); 
		if (null != projectUser && projectUser.getType() != type) {
			String typeStr = Constant.PROJECT_USER_TYPE_MAP().get(
					projectUser.getType());
			Assert.state(false,"您选择的移交人员为"+ typeStr + "成员，无法移交项目");
		}
		DealEvent dealEvent = dealEventDao.selectByUserId(reUserId);
		if (dealEvent != null && dealEvent.getHasProject() == 0) {
			dealEvent.setHasProject(1);
			dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
		//将邀请加入项目的消息置为无效
		messageService.setReferProjectMsgUseless(null,referId, messageType,projectId);
		messageService.sendMessage(reUser.getPhone(), userId, content,messageType, referId,
				Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, projectId,
				Constant.MESSAGE_STATE_INDEX.PENDING, user.getName(),1);
	}

	@Override
	public List<Project> getNearByProject(Integer userId, String[] lfDown, String[] rgUp) {
		//左下经度
		Double lf_down_lng = Double.valueOf(lfDown[0]);
		//左下维度
		Double lf_down_lat = Double.valueOf(lfDown[1]);
		//右上经度
		Double rg_up_lng = Double.valueOf(rgUp[0]);
		//右上维度
		Double rg_up_lat = Double.valueOf(rgUp[1]);
		List<Project> resProject = dao.selectProjectListByPosition(userId,lf_down_lng,lf_down_lat,rg_up_lng,rg_up_lat);
		if (resProject != null && resProject.size() > 0) {
			// 遍历添加服务内容
			for (Project p : resProject) {
				List<ServiceType> serObj = null;
				if (p.getType() < 1) {
					serObj = serverTypeDao.selectByProjectId(p.getId());
				} else {
					// 乙方
					ProjectUser pu = projectUserDao.selectByUserIdAndProjectId(p.getId(), userId, p.getType(), null);// 获取乙方成员对象
					ServiceType ser = serverTypeDao.selectByPrimaryKey(pu.getServiceTypeId());// 获取服务内容
					if (ser != null) {
						serObj = new ArrayList<ServiceType>();
						serObj.add(ser);
					}
				}
				if (serObj != null && serObj.size() > 0) {
					p.setServices(serObj);
				}
			}
		}
		return resProject;
	}

	/*
	 * 手机号移交项目负责人
	 */
	@Override
	public void transferProjectByPhone(Integer userId, Integer type, Integer projectId, String phone,
			Integer serviceTypeId) throws IOException {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR);
		Assert.state(!phone.equals(user.getPhone()),"该手机号为项目当前项目负责人，不能移交！");
		User reUser = userDao.selectByPhone(phone);
		if(reUser != null){
			if(Constant.APP_TYPE.A.equals(type)){
				List<ProjectUser> res = projectUserDao.selectListByParames(projectId, null, Constant.APP_TYPE.B, 1, null);
				for(ProjectUser pu : res){
					User u = userDao.selectByPrimaryKey(pu.getUserId());
					Assert.state(u.getCompanyId() != reUser.getCompanyId(),"移交人员为该项目乙方团队成员,不能移交。");
				}
			}else{
				Assert.state(user.getCompanyId().equals(reUser.getCompanyId()),"负责人与移交人员非同一团队，请重新移交项目");
			}
			ProjectUser projectUser = projectUserDao.selectByParames(projectId, reUser.getId(), null, 1, null); 
			if (null != projectUser && projectUser.getType() != type) {
				String typeStr = Constant.PROJECT_USER_TYPE_MAP().get(
						projectUser.getType());
				Assert.state(false,"您选择的移交人员为"+ typeStr + "成员负责人,无法移交项目");
			}
		}
		Integer referId = projectId;
		Project project = dao.selectByPrimaryKey(projectId);
		Integer messageType = Constant.MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX;
		String content = "将" + project.getName() + "项目甲方负责人移交给您，请尽快确认噢~";
		ServiceType serviceType = null;
		if (type == Constant.PROJECT_USER_TYPE_INDEX.B_INDEX) {
			// 乙方移交 进行判断
			// 他是否有未完成的报障
			Integer count = faultInfoDao.checkUnfinished(userId, projectId, serviceTypeId);
			Assert.state(count == 0,Constant.NO_FINISH_FAULTINFO);
			//通知
			serviceType = serverTypeDao.selectByPrimaryKey(serviceTypeId);
			content = "将" + project.getName() + "项目"
					+ serviceType.getName() + "乙方负责人移交给您，请尽快确认噢~";
			messageType = Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX;
		}
		messageService.setReferProjectMsgUseless(phone,referId, messageType, serviceTypeId);//删除旧的移交消息
		if(reUser != null){
			messageService.sendMessage(reUser.getPhone(), userId, content,messageType, referId,
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, projectId,
					Constant.MESSAGE_STATE_INDEX.PENDING, user.getName(),1);
		}else{
			messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,messageType, phone, userId, referId, false,null);
		}
	}

	/*
	 * 申请加入项目
	 */
	@Override
	public void joinProject(Integer type, Integer projectId, Integer userId, Integer serviceTypeId) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null, Constant.PARAMS_ERROR);
		Assert.state(user.getCompanyId() != 0,"您还未加入团队,请先加入团队!");
		
		ProjectUser pu = projectUserDao.selectByParames(projectId, userId, null, null, serviceTypeId);
		Assert.state(pu == null, "申请无效，您已经是该项目" + (pu != null && pu.getType() == 0 ? "乙方" : "甲方") + "成员,请刷新重试！");
		Project project = dao.selectByPrimaryKey(projectId);
		ServiceType serviceType = null;
		Integer referId = 0;
		if(Constant.APP_TYPE.B.equals(type)){
			referId = serviceTypeId;
			serviceType = serverTypeDao.selectByPrimaryKey(serviceTypeId);
			if(serviceType.getState().equals(0)){
				Assert.state(false,"项目还未生效，请联系相关人员进行确认！");
			}
		}
		// 获取负责人信息
		ProjectUser projectUser = projectUserDao.selectByParames(projectId, null, type, 1, serviceTypeId);
		Assert.state(projectUser != null, Constant.PARAMS_ERROR);
		User priUser = userDao.selectByPrimaryKey(projectUser.getUserId());
		
		String content = serviceType == null ? "申请加入" + project.getName() + "项目甲方成员，请尽快确认噢~"
				: "申请加入" + project.getName() + "项目乙方" + serviceType.getName() + "成员,请尽快确认噢~";
		messageService.sendMessage(priUser.getPhone(), userId, content, Constant.MESSAGE_TYPE_INDEX.APPLY_JOIN_PROJECT_INDEX,
					referId, Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 
				projectId, Constant.MESSAGE_STATE_INDEX.PENDING,user.getName(), 1);
	}

	/*
	 * 判断是否同一个团队
	 */
	@Override
	public String checkPhoneNum(String maintainPhone, String faultPhone,Integer userId) {
		maintainPhone = StringUtils.trim(maintainPhone);
		faultPhone = StringUtils.trim(faultPhone);
		User mUser = userDao.selectByPhone(maintainPhone);
		if(mUser != null){
			User fUser = userDao.selectByPhone(faultPhone);
			if(fUser != null){
				if(mUser.getCompanyId().equals(fUser.getCompanyId())){
					return "1";
				}
			}
		}
		return "0";
	}

	/*
	 * 获取项目概览	 
	 */
	@Override
	public Map<?, ?> getProjectDetailCollet(Integer userId, Integer projectId, Integer type,Integer startIndex,Integer pageSize) {
		if(Constant.APP_TYPE.A.equals(type)){
			Map<String,Object> res = dao.selectCollect(projectId);
			List<FaultInfo> fault_list = faultInfoDao.selectFaultListForProject(projectId,type,startIndex, pageSize);
			res.put("fault_list", fault_list);
			return res;
		}else{
			Map<String,Object> res = dao.selectCollectByParam(userId,projectId);
			List<FaultInfo> fault_list = faultInfoDao.selectFaultListForProjectByParam(projectId,userId,type,startIndex, pageSize);
			res.put("fault_list", fault_list);
			return res;
		}
	}

	/*
	 * 首页获取附近项目
	 */
	@Override
	public Map<String,Object> getHomeNearByInfo(Integer userId, String[] lfDown, String[] rgUp) {
		Map<String,Object> res = new HashMap<String,Object>();
		//左下经度
		Double lf_down_lng = Double.valueOf(lfDown[0]);
		//左下维度
		Double lf_down_lat = Double.valueOf(lfDown[1]);
		//右上经度
		Double rg_up_lng = Double.valueOf(rgUp[0]);
		//右上维度
		Double rg_up_lat = Double.valueOf(rgUp[1]);
		List<Project> resProject = dao.selectByPosition(userId,lf_down_lng,lf_down_lat,rg_up_lng,rg_up_lat);
		if (resProject != null && resProject.size() > 0) {
			// 遍历添加服务内容
			for (Project p : resProject) {
				List<ServiceType> sts = serverTypeDao.selectListByParams(p.getId(), p.getType(), userId);
				p.setServices(sts);
			}
		}
		List<Map<String, Object>> resFaultInfo = faultInfoDao.selectByPosition(userId,lf_down_lng,lf_down_lat,rg_up_lng,rg_up_lat);
		res.put("project_list", resProject);
		res.put("faultinfo_list", resFaultInfo);
		return res;
	}

	@Override
	public int existNearByProject(Integer userId, String[] lfDown, String[] rgUp) {
		//左下经度
		Double lf_down_lng = Double.valueOf(lfDown[0]);
		//左下维度
		Double lf_down_lat = Double.valueOf(lfDown[1]);
		//右上经度
		Double rg_up_lng = Double.valueOf(rgUp[0]);
		//右上维度
		Double rg_up_lat = Double.valueOf(rgUp[1]);
		List<Project> resProject = dao.selectByPosition(userId,lf_down_lng,lf_down_lat,rg_up_lng,rg_up_lat);
		if(resProject != null && resProject.size() > 0){
			return 1;
		}
		return 0;
	}

	@Override
	public Integer getProjectCount() {
		Integer projectCount = dao.selectCount();
		return projectCount;
	}
	
	/**
	 * 邀请加入项目
	 * @author:zhugw
	 * @time:2017年8月9日 上午8:52:01
	 * @param type
	 * @param userId
	 * @param projectId
	 * @param serviceTypeId
	 * @param reUserId
	 */
	@Override
	public void joinProjectMember(Integer type,Integer userId,Integer projectId,Integer serviceTypeId,Integer reUserId){
		Integer msgType = type.equals(Constant.APP_TYPE.B) 
				? Constant.MESSAGE_TYPE_INDEX.B_INVITED_PEOPLE_JOIN_PROJECT_INDEX 
						: Constant.MESSAGE_TYPE_INDEX.A_INVITED_PEOPLE_JOIN_PROJECT_INDEX;
		Integer referId = type.equals(Constant.APP_TYPE.B) ? serviceTypeId : projectId;
		User user = userDao.selectByPrimaryKey(userId);
		//加入项目		
		Message msg = new Message();
		msg.setClassify(Constant.MESSAGE_CLASSIFY_INDEX.PROJECT);
		msg.setPhone(user.getPhone());
		msg.setIsDel(0);
		msg.setType(msgType);
		msg.setState(Constant.MESSAGE_STATE_INDEX.PENDING);
		msg.setReferId(referId);
		msg.setReferUserId(reUserId);
		Message message = messageDao.selectByParames(msg);
		if(message != null){
			// 更新消息状态_1同意
			message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
			message.setIsRead(Constant.ONE);
			messageService.updateByPrimaryKeySelective(message);			
		}
		DealEvent dealEvent = dealEventDao.selectByUserId(userId);
		if(user.getCompanyId().equals(0)){
			//加入团队
			User reUser = userDao.selectByPrimaryKey(reUserId);
			// 更新用户的团队信息
			user.setCompanyId(reUser.getCompanyId());
			user.setJoinTime(new Date());
			userDao.updateByPrimaryKeySelective(user);
			// 更新用户项目信息
			if (dealEvent != null && dealEvent.getHasCompany()==0) {
				dealEvent.setHasCompany(1);
				dealEventDao.updateByPrimaryKeySelective(dealEvent);
			}
		}
		//加入项目
		Integer userType = type.equals(Constant.APP_TYPE.B) ? Constant.PROJECT_USER_TYPE_INDEX.B_INDEX
						:Constant.PROJECT_USER_TYPE_INDEX.A_INDEX;
		// 新增项目人员记录
		ProjectUser pu = projectUserDao.selectByParames(projectId, userId, userType, null,serviceTypeId);
		if (pu == null) { // 查看原来的工作人员里面是否有该成员
			ProjectUser obj = new ProjectUser(Constant.ZERO, projectId, userId,userType, serviceTypeId);
			projectUserDao.insertSelective(obj);
		}
		// 更新用户项目信息
		if (dealEvent != null && dealEvent.getHasProject()==0) {
			dealEvent.setHasProject(1);
			dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
	}

	@Override
	public Map<String, Object> getFaultType(Integer projectId,Integer userId) {
		Integer type=dao.getFaultType(projectId, userId);
		Project project=dao.selectByPrimaryKey(projectId);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("type", type==null?1:type);
		map.put("faultUserName", project.getOwnerName());
		map.put("faultUserPhone", project.getOwnerPhone());
		return map;
	}

	@Override
	public Object selectProjectListByUserId(Integer userId,Integer pageNo, Integer pageSize) {
		Integer companyId = null;
		Company company = companyDao.selectByResponserId(userId);
		if(company != null){
			companyId = company.getId();
		}
		List<Map<String,Object>> res = dao.selectListByUserId(userId,companyId,new Pagination<>(pageNo, pageSize).getRowBounds());
		Pagination<Map<String,Object>> pageInfo = new Pagination<Map<String,Object>>(res);
		return pageInfo;
	}

	/*
	 * web 项目详情
	 */
	@Override
	public Object selectProjectInfo(Integer userId, Integer type, Integer projectId,Integer pageNo,Integer pageSize) {
		Project project = getProjectDetail(userId, projectId, type);
		JSONObject obj = new JSONObject();
		obj.put("project", project);
		List<Map<String,Object>> faultList = faultInfoDao.selectProjectHistoryList(projectId,new Pagination<List<Map<String,Object>>>(pageNo,pageSize).getRowBounds());
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(faultList);
		obj.put("faultInfoList", pageInfo);
		return obj;
	}

	/**
	 * 搜索项目成员
	 * @param userId
	 * @param searchPhone
	 * @param projectId
	 * @return
	 */
	@Override
	public List<User> searchProjectMember(Integer userId, String searchPhone, Integer projectId) {
		ProjectUser puResult = projectUserDao.selectByUserIdAndProjectId(projectId,userId,null,null);
		Assert.state(puResult != null,"当前用户不是该项目成员不能搜索成员");
		List<User> results = userDao.selectSearchProjectMember(projectId,searchPhone);
		return results;
	}

	/**
	 * 根据项目名称搜索项目列表
	 * @author:zhugw
	 * @time:2017年11月29日 上午10:15:02
	 * @param userId
	 * @param projectName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.ProjectService#searchProjectListByPage(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Object searchProjectListByPage(Integer userId, String projectName, Integer pageNum, Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR);
		projectName = StringUtils.isNotBlank(projectName) ? projectName:null;
		List<Project> resProject = dao.searchCompanyProjectByPage(userId,projectName,user.getCompanyId(),new Pagination<List<Project>>(pageNum,pageSize).getRowBounds());
//		if (resProject != null && resProject.size() > 0) {
//			// 遍历添加服务内容
//			for (Project p : resProject) {
//				List<ServiceType> serObj = new ArrayList<ServiceType>();
//				if(Constant.APP_TYPE.A.equals(p.getType())){
//					serObj = serverTypeDao.selectByProjectId(p.getId());
//				}else{
//					serObj = serverTypeDao.selectListByUnEqUser(p.getId(),userId,user.getCompanyId());
//				}
//				if (serObj != null && serObj.size() > 0) {
//					p.setServices(serObj);
//				}
//			}
//		}
		Pagination<Project> pageInfo = new Pagination<Project>(resProject);
		return pageInfo;
	}
	
	/**
	 * 查询项目list分页
	 * @author:zhugw
	 * @time:2017年11月30日 下午2:48:08
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public Object selectProjectListByPage(Integer userId,String projectName,Integer pageNum,Integer pageSize){
		Company company = companyDao.selectByResponserId(userId);
		Integer companyId = null;
		if(company != null){
			companyId = company.getId();
		}
		List<Map<String, Object>> results = dao.selectProjectListByPage(companyId,userId,projectName,new Pagination<>(pageNum,pageSize).getRowBounds());
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(results);
		return pageInfo;
	}
	
	/**
	 * 根据项目名称搜索项目是否已经存在
	 * @param userId
	 * @param name
	 * @return
	 */
	@Override
	public Object isExist(Integer userId, String name,Integer type) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user!=null,Constant.PARAMS_ERROR+" userId错误！");
		Map<String,Object> project = dao.selectProjectByName(name,user.getCompanyId(),type);
		if(project == null){
			project = new HashMap<String,Object>();
		}
		return project;
	}

}
