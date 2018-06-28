package com.xmsmartcity.maintain.service.impl.message_operation;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.Assert;

import com.xmsmartcity.maintain.common.MessageOperationEntity;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ProjectUser;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 2邀请乙方负责人
 * @author:zhugw
 * @time:2017年8月1日 上午10:02:11
 */
@MessageOperationEntity(type=2)
public class MessageOperationInvitedBPerson extends MessageOperationBase{

	public MessageOperationInvitedBPerson(Integer userId, Integer type, Integer messageId,
			MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}
	
	@Override
	@Transactional
	public void operationMsgConsent() {
		// 同意成为乙方负责人
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		// 项目负责人必须有团队，才能同意
		Assert.state(!user.getCompanyId().equals(0), "您还未加入团队，请先去加入团队！");
		Project project = messageService.projectDao.selectByPrimaryKey(message.getProjectId());
		Assert.state(!project.getCompanyId().equals(user.getCompanyId()),"您当前为该项目甲方团队成员,不能成为该项目乙方负责人。");
		ServiceType service = messageService.serviceTypeDao.selectByPrimaryKey(message.getReferId());
//		Project project = messageService.projectDao.selectByPrimaryKey(service.getProjectId());
		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		ServiceType resServer = messageService.serviceTypeDao.selectByParams(message.getProjectId(), 0, userId, message.getReferId());
		Assert.state(resServer == null,"该项目已有负责人，您不能加入该项目！");
		// 更新服务信息并新增项目人员记录
//					ServiceType serviceType = serviceTypeDao.selectByPrimaryKey(message.getReferId());			
		// 使之前的乙方负责人失效
		ProjectUser projectUserB = messageService.projectUserDao.selectByParames(service.getProjectId(), null,
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX, 1, message.getReferId());
		if(projectUserB != null){
			projectUserB.setIsLeader(0);
			messageService.projectUserDao.updateByPrimaryKeySelective(projectUserB);
		}
		service.setMaintainUserMobile(user.getPhone());
		service.setMaintainUserName(user.getName());
		service.setState(1);
		messageService.serviceTypeDao.updateByPrimaryKeySelective(service);
		project.setState(1);
		messageService.projectDao.updateByPrimaryKeySelective(project);

		ProjectUser projectUser = messageService.projectUserDao.selectByParames(message.getProjectId(), userId,
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX, null, message.getReferId());
		if (projectUser == null) { // 查看原来的工作人员里面是否有该成员
			ProjectUser newProjectUser = new ProjectUser(Constant.ONE, service.getProjectId(), userId,
					Constant.PROJECT_USER_TYPE_INDEX.B_INDEX, service.getId());
			messageService.projectUserDao.insertSelective(newProjectUser);
		} else {
			projectUser.setIsLeader(1);
			messageService.projectUserDao.updateByPrimaryKeySelective(projectUser);
		}
		// 推送消息
		String newName = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
		if (0 != message.getReferUserId()) {
			// 新增一条消息（给甲方负责人看的）
			messageService.sendMessage(reUser.getPhone(), userId,
					"同意成为“" + project.getName() + "“项目乙方" + service.getName() + "的负责人",
					Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 0, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, newName, 1);
			// todo 发送微信提醒
			// apiHelper.sendConten(template, userIdList, params);
		} else {
			// 新增一条系统消息（给客服看的）
			Message sMessage = new Message("", userId,
					newName + "同意成为“" + messageService.projectDao.selectByPrimaryKey(service.getProjectId()).getName() + "““"
							+ service.getName() + "“的负责人",
					Constant.MESSAGE_TYPE_INDEX.SYSTEM_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, new Date(), 0, 0);
			messageService.dao.insertSelective(sMessage);
		}
		// 更新用户头像为项目负责人
		user.setRole(Constant.USER_ROLE_INDEX.PROJECT_USER);
		messageService.userDao.updateByPrimaryKeySelective(user);
		if (dealEvent != null && dealEvent.getHasProject() == 0) {
			dealEvent.setHasProject(1);
			messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
		if (null != projectUserB) {
			User projectB = messageService.userDao.selectByPrimaryKey(projectUserB.getUserId());
			projectB.setRole(Constant.USER_ROLE_INDEX.GENERAL_USER);
			messageService.userDao.updateByPrimaryKeySelective(projectB);
		}
	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒绝成为乙方负责人
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		
		ServiceType st = messageService.serviceTypeDao.selectByPrimaryKey(message.getReferId());
		String uName = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
		// 更新消息状态_2拒绝
		message.setState(Constant.MESSAGE_STATE_INDEX.REFUSE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 更新serverType状态
		st.setState(2);//2：已拒绝
		messageService.serviceTypeDao.updateByPrimaryKeySelective(st);
		Project project = messageService.projectDao.selectByPrimaryKey(st.getProjectId());
		project.setState(2);//2：已拒绝
		messageService.projectDao.updateByPrimaryKeySelective(project);
		if (0 != message.getReferUserId()) {
			// 新增一条消息（给甲方负责人看的）
			messageService.sendMessage(reUser.getPhone(), userId,
					"拒绝成为“" + project.getName() + "““" + st.getName() + "“的负责人.",
					Constant.MESSAGE_TYPE_INDEX.INVITED_B_PERSON_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 0, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, uName, 0);
			// todo 发送微信提醒
			// apiHelper.sendConten(template, userIdList, params);
		} else {
			// 新增一条系统消息（给客服看的）
			Message sMessage = new Message("", userId,
					uName + "拒绝成为“" + messageService.projectDao.selectByPrimaryKey(st.getProjectId()).getName() + "““"
							+ st.getName() + "“的负责人。",
					Constant.MESSAGE_TYPE_INDEX.SYSTEM_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, new Date(), 0, 0);
			messageService.dao.insertSelective(sMessage);
		}
	}

}
