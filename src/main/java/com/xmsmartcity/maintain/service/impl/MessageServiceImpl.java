package com.xmsmartcity.maintain.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.xmsmartcity.extra.push.PushUtil;
import com.xmsmartcity.extra.weixin.TemplateEnum;
import com.xmsmartcity.maintain.common.MsgSynchronizationHandler;
import com.xmsmartcity.maintain.controller.util.SendMsgTask;
import com.xmsmartcity.maintain.controller.util.TaskConstant;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.CompanyMapper;
import com.xmsmartcity.maintain.dao.DealEventMapper;
import com.xmsmartcity.maintain.dao.FaultInfoMapper;
import com.xmsmartcity.maintain.dao.MessageMapper;
import com.xmsmartcity.maintain.dao.ProjectMapper;
import com.xmsmartcity.maintain.dao.ProjectUserMapper;
import com.xmsmartcity.maintain.dao.ServiceTypeMapper;
import com.xmsmartcity.maintain.dao.UserMapper;
import com.xmsmartcity.maintain.dao.UserTokenMapper;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.pojo.UserToken;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.maintain.service.impl.message_operation.MessageOperation;
import com.xmsmartcity.maintain.service.impl.message_operation.MessageOperationBase;
import com.xmsmartcity.orm.Pagination;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.JavaSmsApi;

import net.sf.json.JSONObject;

@Service
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService {

	public MessageServiceImpl(BaseDao<Message> dao) {
		super(dao);
	}

	@Autowired
	public MessageMapper dao;

	@Autowired
	public UserMapper userDao;

	@Autowired
	public ProjectMapper projectDao;

	@Autowired
	public ProjectUserMapper projectUserDao;

	@Autowired
	public UserTokenMapper userTokenDao;

	@Autowired
	public CompanyMapper companyDao;

	@Autowired
	public ServiceTypeMapper serviceTypeDao;

	@Autowired
	public DealEventMapper dealEventDao;
	
	@Autowired
	public FaultInfoMapper faultInfoDao;
	
	private Logger logger = Logger.getLogger(this.getClass());

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * 发送消息
	 * 
	 * @param phone
	 *            发送给谁的手机号
	 * @param userId
	 *            谁发送的 用户id (邀请人/申请者的id)
	 * @param content
	 *            发送内容
	 * @param type
	 *            发送类型 0系统消息 1邀请甲方负责人，2邀请乙方负责人 3邀请普通成员进团队 4甲方邀请普通成员进项目
	 *            5乙方邀请普通成员进项目 6邀请成为团队负责人 7申请加入企业 8项目相关 9其他消息
	 * @param referId
	 *            相关id 1项目id， 2服务类型id 3团队id 4项目id 5服务类型id 6团队id 7团队id 8报障id
	 * @param classify
	 *            归类 0企业类 1邀请类 2项目类
	 * @param projectId
	 *            项目ID
	 * @param state
	 *            状态 0未处理 1已同意 2已拒绝 3不用处理（通知类）4已失效
	 * @param userName
	 *            用户名称，用于推送内容的首部
	 * @param jumpType
	 *            跳转类型 app专用，类型为项目类时0跳转报障详情 1不跳转
	 */
	public void saveMessage(String phone, Integer userId, String content, Integer type, Integer referId,
			Integer classify, Integer projectId, Integer state, String userName, Integer jumpType) {
		Message message = new Message(phone, userId, content, type, referId, classify, new Date(), projectId, state);
		message.setJumpType(jumpType);
		dao.insertSelective(message);// 添加消息
	}

	/**
	 * 发送消息
	 * 
	 * @param phone
	 *            发送给谁的手机号
	 * @param userId
	 *            谁发送的 用户id (邀请人/申请者的id)
	 * @param content
	 *            发送内容
	 * @param type
	 *            消息类型，0系统消息 1邀请甲方负责人 2邀请乙方负责人 3邀请普通成员进团队 4甲方邀请普通成员进项目
	 *            5乙方邀请普通成员进项目 6邀请成为团队负责人 7邀请成为项目负责人 8申请加入企业 9退出项目  10申请加入项目 11项目相关 12其他消息 13离开团队
	 * @param referId
	 *            相关id 1项目id， 2服务类型id 3团队id 4项目id 5服务类型id 6团队id 7项目id 8团队id 9项目id 10服务类型id 11报障id
	 * @param classify
	 *            归类 0企业类 1项目类 2故障类
	 * @param projectId
	 *            项目ID
	 * @param state
	 *            状态 0未处理 1已同意 2已拒绝 3不用处理（通知类）4已失效
	 * @param userName
	 *            用户名称，用于推送内容的首部
	 * @param jumpType
	 *            跳转类型 app专用，类型为项目类时0跳转报障详情 1不跳转
	 */
	@Override
	public void sendMessage(String phone, Integer userId, String content, Integer type, Integer referId,
			Integer classify, Integer projectId, Integer state, String userName, Integer jumpType) {
		Integer count = getPendingMessageForInteger(content,phone, userId,classify,type,referId);
		if(count != 0){
			//已发送邀请
			return ;
		}
		Message message = new Message(phone, userId, content, type, referId, classify, new Date(), projectId, state);
		message.setJumpType(jumpType);
		dao.insertSelective(message);// 添加消息
		User user = userDao.selectByPhone(phone);
		if (null != user) {// 推送消息
			UserToken userToken = userTokenDao.getTokenInfo(user.getId(), null, null);
			// 发送一条推送
			if (null != userToken) {
				JSONObject msgResult = new JSONObject();
				msgResult.put("type", Constant.ONE);
				msgResult.put("info", message);
				msgResult.put("content", StringUtils.isNotBlank(userName) ? userName + content : content);
				PushUtil.pushSingleMsg(userToken.getPushToken(), msgResult.toString());
				//推送web消息
				JSONObject msg = new JSONObject();
				msg.put("classify", classify);
				msg.put("content", content);
				MsgSynchronizationHandler.sendMessage(msg.toString(), userToken.getSysToken());
			}
			// todo 发送微信提醒
			sendWxTip(user, state, classify, userName, content);
			// apiHelper.sendConten(template, userIdList, params);
		}
	}

	public void sendWxTip(User user, Integer state, Integer classify, String userName, String content) {
		String firstData = "";
		String keyword1 = "";
		String keyword2 = "";
		String keyword3 = "";
		String remark = "";
		SendMsgTask sendMsgTask = null;
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		switch (state) {
		case 0:
		case 1:
		case 2:
			// 操作类

			if (classify == 0) {
				firstData = "企业类事项流程";
			} else if (classify == 1) {
				firstData = "邀请类事项流程";
			} else {
				firstData = "项目类事项流程";
			}
			keyword1 = StringUtils.isNotBlank(userName) ? userName + content : content;

			keyword2 = simpleDateFormat.format(new Date());
			remark = "温馨提示：您可以通过登录APP客户端或者PC客户端进行处理，谢谢。";
			sendMsgTask = new SendMsgTask(firstData, keyword1, keyword2, keyword3, remark, TemplateEnum.temp2,
					userList);
			break;

		case 3:
		case 4:
			// 无需操作类
			if (classify == 0) {
				keyword1 = "企业类消息通知";
			} else if (classify == 1) {
				keyword1 = "邀请类消息通知";
			} else {
				keyword1 = "项目类消息通知";
			}
			keyword2 = StringUtils.isNotBlank(userName) ? userName + content : content;// simpleDateFormat.format(new
																						// Date());

			remark = "此消息仅是微信提醒，详情请登录APP客户端或者PC客户端查看，谢谢。";
			List<User> userIdList = new ArrayList<User>();
			userIdList.add(user);
			sendMsgTask = new SendMsgTask(firstData, keyword1, keyword2, keyword3, remark, TemplateEnum.temp5,
					userList);
			break;
		default:
			break;
		}

		if (sendMsgTask != null) {
			TaskConstant.POOL.execute(sendMsgTask);
		}
	}

	/**
	 * 发送短信(不保存信息)
	 * 
	 * @author:zhugw
	 * @time:2017年4月10日 上午9:42:45
	 * @param type
	 *            发送模板 0 客服邀请团队负责人 1 用户邀请团队成员 2 用户邀请乙方负责人 3 客服邀请乙方负责人 4
	 *            客服邀请项目负责人 5 用户邀请项目成员 6 用户邀请项目负责人
	 * @param phone
	 *            发送给谁
	 * @param userId
	 *            用户id
	 * @param referId
	 *            (type 为某一值放回的数据类型) 0,1 companyId 2,3 serviceTypeId 4,5,6项目id
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void sendPhoneMsg(Integer type, String phone, Integer userId, Integer referId) throws IOException {
		Long tpl = Constant.PHONE_MESSAGE_MAP().get(type);
		String content = getPhoneMessageContentByType(phone,type, referId, userId,null);
		String responseBody = JavaSmsApi.tplSendSms(Constant.apikey_marketing, tpl, content, phone);
		JSONObject obj = JSONObject.fromObject(responseBody);
		if(!"0".equals(obj.get("code").toString())){
			logger.error("send message error: "+responseBody);
		}
	}

	/*
	 * 根据参数获取message
	 */
	@Override
	public Message getMessage(String phone, Integer referUserId, Integer classify, Integer projectId, Integer referId,
			Integer type, Integer state) {
		Message msg = new Message(phone, referUserId, null, type, referId, classify, null, projectId, state);
		return dao.selectByParames(msg);
	}
	
	/**
	 * 发送短信(保存信息)
	 * 
	 * @author:zhugw
	 * @time:2017年4月10日 上午9:42:45
	 * @param type
	 *            消息类型， 1邀请甲方负责人 2邀请乙方负责人 3邀请普通成员进团队 4甲方邀请普通成员进项目 5乙方邀请普通成员进项目
	 *            6邀请成为团队负责人 7邀请成为项目负责人,8项目报障相关
	 * @param phone
	 *            发送给谁
	 * @param userId
	 *            用户id
	 * @param referId
	 *            相关id 1项目id， 2服务类型id 3团队id 4项目id 5服务类型id 6团队id 7项目id 8项目id
	 * @param isSendSystemMessage
	 *            是否发送系统消息 true 发送
	 * @throws IOException
	 */
	@Transactional
	@Override
	public void sendPhoneMessage(Integer classify,Integer type, String phone, Integer userId, Integer referId,
			Boolean isSendSystemMessage,String webPath) throws IOException {
		//判断是否已发送过
		Integer count = getPendingMessageForInteger(null,phone, userId,classify,type,referId);
		if(count != 0){
			return;
		}
		Long tpl = Constant.PHONE_MESSAGE_MAP().get(type);
		String content = getPhoneMessageContentByType(phone,type, referId, userId,webPath);
		String responseBody = JavaSmsApi.tplSendSms(Constant.apikey_marketing, tpl, content, phone);
		JSONObject obj = JSONObject.fromObject(responseBody);
		if(!"0".equals(obj.get("code").toString())){
			logger.error("send message error: "+responseBody);
			//Assert.state(false,"发送信息失败！请检查电话号码是否正确！");
		}
		// 添加项目消息
		if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_invitation_project_a_tpl_id_index)) {
			// 邀请甲方负者人
			Project project = projectDao.selectByPrimaryKey(referId);
			Message message = new Message(phone, userId, "邀您成为" + project.getName() + "项目甲方负责人，请尽快确认噢~",
					Constant.MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX, referId,
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, new Date(),project.getId(), 0);
			dao.insertSelective(message);
		} else if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_invitation_project_b_tpl_id_index)) {
			// 邀请乙方负责人
			ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(referId);
			Project project = projectDao.selectByPrimaryKey(serviceType.getProjectId());
			Message message = new Message(phone, userId,
					"邀请您成为“" + project.getName() + "”项目乙方" + serviceType.getName() + "负责人，请尽快确认噢~",
					Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX, referId, 
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, new Date(),project.getId(),0);
			dao.insertSelective(message);
		} else if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_invitation_company_tpl_id_index)) {
			// 用户邀请团队成员
			Company company = companyDao.selectByPrimaryKey(referId);
			Message message = new Message(phone, userId, "邀您成为" + company.getName() + "团队成员，请尽快确认噢~",
					Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_JOIN_COMPANY_INDEX, referId,
					Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, new Date(), 0, 0);
			dao.insertSelective(message);
		} else if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_invitation_project_a_member_tpl_id_index)) {
			// 甲方邀请普通成员进项目
			Project project = projectDao.selectByPrimaryKey(referId);
			Message message = new Message(phone, userId, "邀您成为" + project.getName() + "项目甲方成员，请尽快确认噢~",
					Constant.MESSAGE_TYPE_INDEX.A_INVITED_PEOPLE_JOIN_PROJECT_INDEX, referId, 
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, new Date(),project.getId(), 0);
			dao.insertSelective(message);
		} else if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_invitaion_project_b_member_tpl_id_index)) {
			// 乙方邀请普通成员进项目
			ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(referId);
			Project project = projectDao.selectByPrimaryKey(serviceType.getProjectId());
			Message message = new Message(phone, userId,
					"邀您成为" + project.getName() + "项目乙方" + serviceType.getName() + "成员,请尽快确认噢~",
					Constant.MESSAGE_TYPE_INDEX.B_INVITED_PEOPLE_JOIN_PROJECT_INDEX, referId, 
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,new Date(),project.getId(), 0);
			dao.insertSelective(message);
		} else if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_invitation_company_principal_tpl_id_index)) {
			// 邀请成为团队负责人
			Company company = companyDao.selectByPrimaryKey(referId);
			Message message = new Message(phone, userId, "邀您成为" + company.getName() + "团队负责人，请尽快确认噢~",
					Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX, referId, 
					Constant.MESSAGE_CLASSIFY_INDEX.COMPANY,new Date(), 0, 0);
			dao.insertSelective(message);
		} else if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_invitation_project_principal_tpl_id_index)) {
			//邀请成为项目负责人
			Project project = projectDao.selectByPrimaryKey(referId);
			Message message = new Message(phone, userId, "邀您成为" + project.getName() + "项目负责人，请尽快确认噢~",
					Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_PROJECT_PERSON_INDEX, referId, 
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT,new Date(),project.getId(), 0);
			dao.insertSelective(message);
		} else if (type.equals(Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index)) {
			// 项目报障相关
			// 报障消息发送
		}

	}
	
	/**
	 * 推送消息(保存信息)不发送短信
	 * @param classify 消息类别
	 * @param type 类别
	 * @param phone 发送号码
	 * @param userId  用户ID
	 * @param referId  关联ID
	 * @param webPath  web路径
	 * @throws IOException
	 */
	@Override
	public void pushMessage(JSONObject jsonObject) throws IOException{
		int type=jsonObject.getInt("type");
		UserToken userToken = userTokenDao.getTokenInfo(jsonObject.getInt("userId"), null, null);
		//该用户无pushToken
		if (userToken==null || userToken.getPushToken()==null) {
			return;
		}
		String content="";
		if (type==Constant.PHONE_MESSAGE_INDEX.user_patrol_remind_tpl_id_index) {
			//巡检提醒发送
			content=jsonObject.getString("equipName")+ "计划在"+jsonObject.getString("checkTime")+"完成巡检";
		}else if (type==Constant.PHONE_MESSAGE_INDEX.user_patrol_missing_tpl_id_index) {
			//漏巡检推送提醒
			content=jsonObject.getString("equipName")+ "计划在"+jsonObject.getString("checkTime")+"完成巡检,已延迟"+jsonObject.getString("overTime")+"小时";
		}
		Message message = new Message(jsonObject.getString("phone"), jsonObject.getInt("userId"),
				content,type, jsonObject.getInt("id"),Constant.MESSAGE_CLASSIFY_INDEX.PATROL,new Date(),0, 0);
		JSONObject msgResult = new JSONObject();
		msgResult.put("type", Constant.ONE);
		msgResult.put("info", message);
		msgResult.put("content", content);
		PushUtil.pushSingleMsg(userToken.getPushToken(), msgResult.toString());
		dao.insertSelective(message);
	}

	/**
	 * 获取发送短信所需参数
	 * 
	 * @author:zhugw
	 * @time:2017年4月12日 下午3:31:03
	 * @param type
	 *            消息类型， 1邀请甲方负责人 2邀请乙方负责人 3邀请普通成员进团队 4甲方邀请普通成员进项目 5乙方邀请普通成员进项目
	 *            6邀请成为团队负责人 7邀请成为项目负责人 8项目报障相关 13-15乙方接受报障单、正在维修、完成维修  16乙方受理报障单
	 * @param referId
	 *            相关id 1服务类型id， 2服务类型id 3团队id 4项目id 5服务类型id 6团队id 7项目id 8项目id
	 * @param userId
	 *            用户id
	 * @param webPath
	 *            消息中网址
	 * @param phone
	 * 			    发送的手机号
	 * @return
	 */
	public String getPhoneMessageContentByType(String phone,Integer type, Integer referId, Integer userId,String webPath) {
		String content = "";
		String referUserName = "";
		User referUser = userDao.selectByPrimaryKey(userId);
		if (null != referUser) {
			referUserName = referUser.getName();
		}
		User user = userDao.selectByPhone(phone);
		String userName = user != null ? user.getPhone() : phone;
		webPath = webPath != null ? webPath+" " : Constant.webSite+" ";
		if (type == 1) {// 邀请甲方负者人
			Project project = projectDao.selectByPrimaryKey(referId);
			content = "#username#=" + referUserName + "&#project#=" + project.getName() + "&#remark#=" + Constant.webSite;
		} else if (type == 2) {// 邀请乙方负责人
			ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(referId);
			Project project = projectDao.selectByPrimaryKey(serviceType.getProjectId());
			content = "#username#=" + referUserName + "&#project#=" + project.getName() + "&#servicetype#="
					+ serviceType.getName() + "&#remark#=" + Constant.webSite;
		} else if (type == 3) {// 邀请普通成员进团队
			Company company = companyDao.selectByPrimaryKey(referId);
			content = "#username#=" + referUserName + "&#phone#=" +referUser.getPhone()+"&#companyname#=" + company.getName() + "&#remork#=" + webPath;
		} else if (type == 4) {// 甲方邀请普通成员进项目
			Project project = projectDao.selectByPrimaryKey(referId);
			content = "#username#=" + referUserName + "&#phone#=" +referUser.getPhone()+"&#projectname#=" + project.getName() + "&#remork#=" + webPath;
		} else if (type == 5) {// 乙方邀请普通成员进项目
			ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(referId);
			Project project = projectDao.selectByPrimaryKey(serviceType.getProjectId());
			content = "#username#=" + referUserName + "&#phone#=" +phone+"&#projectname#=" + project.getName() + "&#remork#=" + webPath;
		} else if (type == 6) {// 邀请成为团队负责人
			Company company = companyDao.selectByPrimaryKey(referId);
			content = "#username#=" + referUserName + "&#company#=" + company.getName() + "&#remark#=" + Constant.webSite;
		} else if (type == 7) {// 邀请成为项目负责人
			Project project = projectDao.selectByPrimaryKey(referId);
			content = "#username#=" + referUserName + "&#project#=" + project.getName() + "&#remark#=" + Constant.webSite;
		} else if (type == 8) {// 项目报障相关
			FaultInfo faultinfo = faultInfoDao.selectByPrimaryKey(referId);
			ServiceType st = serviceTypeDao.selectByPrimaryKey(faultinfo.getServiceTypeId());
			content = "#faultname#=" + st.getName() +"&#code#="+faultinfo.getCode()+"&#remark#="+ Constant.webSite;
		} else if (type==13 || type==14 || type==15) {//乙方接受报障单、正在维修、完成维修
			content = "#username#="+referUserName;
		}else if (type==16) {//乙方受理报障单，并预约时间
			FaultInfo faultinfo = faultInfoDao.selectByPrimaryKey(referId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String a_time = sdf.format(faultinfo.getAppointmentTime());
			content = "#username#="+referUserName+"&#time#="+a_time;
		}else if (type==17) {//用户提交工单
			FaultInfo faultinfo = faultInfoDao.selectByPrimaryKey(referId);
			ServiceType st = serviceTypeDao.selectByPrimaryKey(faultinfo.getServiceTypeId());
			content = "#username#="+userName+"&#referUsername#="+referUserName+"&#content#="+st.getName();
		}else if (type==18) {//用户提交了费用工单
			FaultInfo faultinfo = faultInfoDao.selectByPrimaryKey(referId);
			ServiceType st = serviceTypeDao.selectByPrimaryKey(faultinfo.getServiceTypeId());
			content = "#username#="+userName+"&#referUsername#="+referUserName+"&#content#="+st.getName();
		}else if (type==19) {//退回报修单
			FaultInfo faultinfo = faultInfoDao.selectByPrimaryKey(referId);
			Project project=projectDao.selectByPrimaryKey(faultinfo.getProjectId());
			content = "#username#=" + referUserName +"&#projectname#="+project.getName();
		}
		return content;
	}
	
	/**
	 * 根据参数判断是否已发送消息
	 * @author:zhugw
	 * @time:2017年8月7日 上午11:50:40
	 * @param phone
	 * @param userId
	 * @param classify
	 * @param type
	 * @param referId
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.MessageService#getPendingMessageForInteger(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Integer getPendingMessageForInteger(String content,String phone, Integer userId, Integer classify, Integer type,Integer referId) {
		if (classify < Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN) {
			return dao.selectMessageForInteger(phone, classify, userId, type,referId);
		} else {
			// 报障消息
			return dao.selectMessageForParames(content,phone, classify, userId, type,referId);
		}
	}

	/**
	 * 将邀请加入团队的消息置为无效
	 */
	@Override
	public void setReferMsgUseless(Integer referId,Integer type) {
		Message msg = new Message();
		msg.setState(Constant.ZERO);
		msg.setReferId(referId);
		msg.setType(type);
		msg.setIsDel(Constant.ZERO);
		Message obj = dao.selectMessageByParames(msg);// 查询对应消息
		if (obj != null) {
			dao.updateByPrimaryKeySelective(obj);
		}
	}

	/**
	 * 将相关项目的消息置为无效
	 * 
	 * @param companyId
	 */
	public void setReferProjectMsgUseless(String phone,Integer referId, Integer type, Integer projectId) {
		Message msg = new Message();
		msg.setReferId(referId);
		msg.setType(type);
		msg.setProjectId(projectId);
		if(phone != null){
			msg.setPhone(phone);
		}
		msg.setIsDel(Constant.ZERO);
		List<Message> objList = dao.selectListMsgByParames(msg);// 查询对应消息
		if (objList != null && objList.size() > 0) {
			for(Message obj : objList){
				obj.setIsDel(1);
				dao.updateByPrimaryKeySelective(obj);
			}
		}
	}

	/**
	 * 获取消息首页（企业类、邀请类）
	 */
	@Override
	public Object getNotProjectMessage(String phone, Integer classify) {
		// 最新的消息
		Message message = dao.getNewestMessage(phone, classify);
		if (null == message) {
			message = new Message();
			message.setContent("");
			message.setClassify(classify);
		} else {
			// 非通知消息
			if (!message.getState().equals(Constant.MESSAGE_STATE_INDEX.NO_HANDLE)) {
				String userName = null;
				if (Constant.ZERO == message.getReferUserId()) {
					userName = "咚咚客服";
				} else {
					User user = userDao.selectByPrimaryKey(message.getReferUserId());
					userName = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
				}
				message.setContent(userName + message.getContent());
				message.setReferUserName(userName);
			}
		}
		message.setReferUserId(null);
		message.setState(null);
		message.setReferId(null);
		message.setNoReadSum(dao.getNotReadSum(message));
		return message;
	}

	/**
	 * 
	 * 消息首页(最新数据及未读数量)
	 * 
	 * @author:zhugw
	 * @time:2017年4月9日 下午2:47:43
	 * @param userId
	 * @return (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.MessageService#getIndexMsg(java.lang.Integer)
	 */
	@Override
	public String getIndexMsg(Integer userId) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null, Constant.PARAMS_ERROR);
		JSONObject result = new JSONObject();
		// 企业消息
		result.put("companyMessage", getNotProjectMessage(user.getPhone(), Constant.MESSAGE_CLASSIFY_INDEX.COMPANY));
		// 项目消息
		result.put("projectMessage", getNotProjectMessage(user.getPhone(), Constant.MESSAGE_CLASSIFY_INDEX.PROJECT));
		// 报障消息
		result.put("maintainMessage", getNotProjectMessage(user.getPhone(), Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN));
		// 巡检消息
		result.put("patrolMessage", getNotProjectMessage(user.getPhone(), Constant.MESSAGE_CLASSIFY_INDEX.PATROL));
		return result.toString();
	}

	/*
	 * 获取消息对应类型
	 */
	@Override
	@Transactional
	public List<Message> getMessageByClassify(Integer userId, Integer classify, Integer startIndex, Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null, Constant.PARAMS_ERROR);
		List<Message> res = dao.selectMessageForClassify(user.getPhone(), classify, startIndex, pageSize);
		for (Message msg : res) {
			// 非通知消息
			if (msg.getClassify() != 2 && msg.getState().equals(Constant.MESSAGE_STATE_INDEX.NO_HANDLE)) {
				msg.setIsRead(Constant.READED);
				dao.updateByPrimaryKeySelective(msg);
			}
			// 添加用户名称
			String userName = null;
			if (msg.getReferUserId() == null || msg.getReferUserId() == Constant.ZERO) {
				userName = "咚咚客服";
			} else {
				User referUser = userDao.selectByPrimaryKey(msg.getReferUserId());
				userName = StringUtils.isNotBlank(referUser.getName()) ? referUser.getName() : referUser.getPhone();
			}
			msg.setContent(userName + msg.getContent());
			msg.setReferUserName(userName);
		}
		return res;
	}

	/**
	 * 给消息的操作分类
	 * @param messageType
	 *            0同意 1拒绝
	 * 
	 * @return type 0同意成为团队负责人 1拒绝成为团队负责人 2同意别人申请加入团队 3拒绝别人申请加入团队 4同意成为甲方负责人
	 *         5拒绝成为甲方负责人 6同意成为乙方负责人 7拒绝成为乙方负责人 8同意加入团队（别人邀请你加入） 9拒绝加入团队
	 *         10同意加入项目甲方 11拒绝加入项目甲方 12同意加入项目乙方 13拒接加入项目乙方 14同意成员退出项目 15拒绝成员退出项目
	 * 
	 */
	@SuppressWarnings("unused")
	private Integer classifyMessageType(Integer messageType, Integer messageId) {
		Message message = dao.selectByPrimaryKey(messageId);
		Assert.state(message.getState().equals(Constant.MESSAGE_STATE_INDEX.PENDING), Constant.MESSAGE_STATE_ERROR);
		// 邀请甲方负责人
		if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.INVITED_A_PERSON_INDEX)) {
			return messageType == 0 ? Constant.FOUR : Constant.FIVE;
		}
		// 邀请乙方负责人
		else if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX)) {
			return messageType == 0 ? Constant.SIX : Constant.SEVEN;
		}
		// 邀请普通成员进团队
		else if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_JOIN_COMPANY_INDEX)) {
			return messageType == 0 ? Constant.EIGHT : Constant.NINE;
		}
		// 甲方邀请普通成员进项目
		else if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.A_INVITED_PEOPLE_JOIN_PROJECT_INDEX)) {
			return messageType == 0 ? Constant.TEN : Constant.ELEVEN;
		}
		// 乙方邀请普通成员进项目
		else if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.B_INVITED_PEOPLE_JOIN_PROJECT_INDEX)) {
			return messageType == 0 ? Constant.TWELVE : Constant.THIRTEEN;
		}
		// 邀请成为团队负责人
		else if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_CHANGE_COMPANY_PERSON_INDEX)) {
			return messageType == 0 ? Constant.ZERO : Constant.ONE;
		}
		// 申请加入团队
		else if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.APPLY_JOIN_COMPANY_INDEX)) {
			return messageType == 0 ? Constant.TWO : Constant.THREE;
		}
		// 退出项目
		else if (message.getType().equals(Constant.MESSAGE_TYPE_INDEX.QUIT_PROJECT)) {
			return messageType == 0 ? Constant.FOURTEEN : Constant.FIFTEEN;
		}
		// 申请加入项目
		else if(message.getType().equals(Constant.MESSAGE_TYPE_INDEX.APPLY_JOIN_PROJECT_INDEX)){
			return messageType == 0 ? Constant.SIXTEEN : Constant.SEVENTEEN;
		}		
		else {
			Assert.state(false, Constant.SERVICE_ERROR);
			return null;
		}
	}
	
	/**
	 * 消息类操作
	 * @param userId
	 *            操作者id
	 * @param messageId
	 *            消息id
	 * @param type
	 *            0同意成为团队负责人 1拒绝成为团队负责人(理由) 2同意别人申请加入团队 3拒绝别人申请加入团队（理由）
	 *            4同意成为甲方负责人 5拒绝成为甲方负责人（理由） 6同意成为乙方负责人 7拒绝成为乙方负责人（理由）
	 *            8同意加入团队（别人邀请你加入） 9拒绝加入团队（理由）10同意加入项目甲方 11拒绝加入项目甲方 12同意加入项目乙方
	 *            13拒接加入项目乙方 14同意成员退出项目 15拒绝成员退出项目 16同意申请加入项目 17拒接申请加入项目
	 * @param reason
	 * 
	 */
	@Transactional
	public void messageOperate(Integer userId, Integer type, Integer messageId) {
		MessageOperation msgOperation = new MessageOperation(userId,type,messageId,this);
		MessageOperationBase obj = msgOperation.executeMessageOperation();
		if(type == 0){
			// 同意
			obj.operationMsgConsent();
		}else{
			// 拒绝
			obj.operationMsgReject();
		}
	}

	/**
	 * 根据消息状态获取首页待处理事项
	 */
	@Override
	public List<Map<String, Object>> getMessageByState(Integer state,Integer userId, Integer startIndex, Integer pageSize) {
		return dao.selectMessageByState(state,userId, startIndex, pageSize);
	}
	
	
	/**
	 * 测试短信
	 */
	public static void main(String[] args) throws IOException {
//		String content = "#faultname#=321321&#code#=DD23115526&#remark#=" + Constant.webSite;
//		JavaSmsApi.tplSendSms(Constant.apikey_marketing, 1802094, content, "15857123072");
	}

	@Override
	public Integer selectMessageExist(String phone, Integer userId, Integer classify, Integer type, Integer referId,
			Integer state) {
		return dao.selectMessageExist(phone, classify, userId, type, referId, state);
	}

	@Override
	public void setMessageIsRead(String messageIds, Integer userId) {
		int index = messageIds.lastIndexOf(",");
		if(index != -1 && index == messageIds.length()-1){
			messageIds = messageIds.substring(0, index);
		}
		String[] ids = messageIds.split(",");
		List<String> resIds = Arrays.asList(ids);
		dao.updateIsReadForBatch(resIds);
	}
	
	@Override
	public void delMessageById(String messageIds, Integer userId) {
		int index = messageIds.lastIndexOf(",");
		if(index != -1 && index == messageIds.length()-1){
			messageIds = messageIds.substring(0, index);
		}
		String[] ids = messageIds.split(",");
		List<String> resIds = Arrays.asList(ids);
		dao.updateIsDelForBatch(resIds);
	}

	@Override
	public List<Map<String, Object>> getInviteMessage(Integer userId) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR+" 用户ID错误.");
		List<Map<String,Object>> res = dao.getInviteMessage(userId,user.getPhone());
		return res;
	}

	/*
	 * web 页面分类消息
	 */
	@Override
	public Object selectMessageByClassify(Integer userId, Integer classify,Integer pageNo, Integer pageSize) {
		List<Map<String, Object>> unreadResult = dao.selectListByUnread(userId,classify);
		List<Map<String, Object>> readResult = dao.selectListByRead(userId,classify,new Pagination<Map<String, Object>>(pageNo,pageSize).getRowBounds());
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(readResult);
		JSONObject obj = new JSONObject();
		obj.put("unreadList", unreadResult);
		obj.put("readList", pageInfo);
		return obj;
	}

	/**
	 * 统计消息未读数
	 * @author:zhugw
	 * @time:2017年11月15日 上午10:16:11
	 * @param phone
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.MessageService#selectUnreadMessageSum(java.lang.String)
	 */
	@Override
	public List<Map<String,Integer>> selectUnreadMessageSum(String phone) {
		List<Map<String,Integer>> result = dao.selectUnreadMessageSum(phone);
		return result;
	}

	/**
	 * 根据参数删除消息
	 * @author:zhugw
	 * @time:2017年12月19日 下午2:53:23
	 * @param phone
	 * @param referUserId
	 * @param type
	 * @param referId
	 * @param isRead
	 * @param classify
	 * @param state
	 * @param projectId
	 */
	@Override
	public void delMessageByParams(String phone, Integer referUserId, Integer type, Integer referId, Integer isRead,
			Integer classify, Integer state, Integer projectId) {
		Message message = dao.selectMessageByParams(phone,type, referId,referUserId, isRead, classify,state,projectId);
		if(message != null){
			dao.deleteByPrimaryKey(message.getId());
		}
	}

	

}
