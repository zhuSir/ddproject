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
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 1邀请甲方负责人
 * @author:zhugw
 * @time:2017年7月28日 上午11:16:12
 */
@MessageOperationEntity(type=1)
public class MessageOperationInvitedAPerson extends MessageOperationBase{

	public MessageOperationInvitedAPerson(Integer userId, Integer type, Integer messageId,
			MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}

	@Override
	@Transactional
	public void operationMsgConsent() {
		// 4同意成为甲方负责人		
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		// 团队项目，负责人必须有团队，才能同意
		Assert.state(!user.getCompanyId().equals(0), Constant.NOT_JOIN_COMPANY);

		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 更新项目信息并且新增项目人员记录
		// 使之前的甲方负责人失效
		ProjectUser projectUserA = messageService.projectUserDao.selectByParames(message.getProjectId(), null,
				Constant.PROJECT_USER_TYPE_INDEX.A_INDEX, 1, null);
		if (projectUserA != null) {
			projectUserA.setIsLeader(0);
			messageService.projectUserDao.updateByPrimaryKeySelective(projectUserA);
		}
		ProjectUser pu = messageService.projectUserDao.selectByParames(message.getProjectId(), userId, Constant.PROJECT_USER_TYPE_INDEX.A_INDEX, null,null);
		if (pu == null) { // 查看原来的工作人员里面是否有该成员
			ProjectUser projectUser = new ProjectUser(Constant.ONE, message.getProjectId(), userId,
					Constant.PROJECT_USER_TYPE_INDEX.A_INDEX, Constant.ZERO);
			messageService.projectUserDao.insertSelective(projectUser);
		} else {
			pu.setIsLeader(1);
			messageService.projectUserDao.updateByPrimaryKeySelective(pu);
		}
		// update project infomation
		Project project = messageService.projectDao.selectByPrimaryKey(message.getProjectId());
		if (project != null) {
			project.setState(1);
			project.setOwnerId(userId);
			project.setOwnerName(user.getName());
			project.setOwnerPhone(user.getPhone());
			project.setCompanyId(user.getCompanyId());
			project.setCompanyName(messageService.companyDao.selectByPrimaryKey(user.getCompanyId()).getName());
			messageService.projectDao.updateByPrimaryKeySelective(project);
		}
		// 更新用户 等级 头像
		user.setRole(Constant.USER_ROLE_INDEX.PROJECT_USER);
		messageService.userDao.updateByPrimaryKeySelective(user);
		if (dealEvent != null && dealEvent.getHasProject() == 0) {
			dealEvent.setHasProject(1);
			messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
		// 更新旧负责人
		if (null != projectUserA) {
			User projectA = messageService.userDao.selectByPrimaryKey(projectUserA.getUserId());
			projectA.setRole(Constant.USER_ROLE_INDEX.GENERAL_USER);
			messageService.userDao.updateByPrimaryKeySelective(projectA);
		}
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		if (reUser != null) {
			//给移交者发消息
			messageService.sendMessage(reUser.getPhone(), userId, "同意成为“" + project.getName() + "“项目负责人.",
					Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 0, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, reUser.getName(),1);
			// todo 发送微信提醒
			// apiHelper.sendConten(template, userIdList, params);
		}
	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒绝成为甲方负责人
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		
		String refuseName = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
		// 更新消息状态_2拒绝
		message.setState(Constant.MESSAGE_STATE_INDEX.REFUSE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		Project project = messageService.projectDao.selectByPrimaryKey(message.getProjectId());
		project.setState(2);
		messageService.projectDao.updateByPrimaryKeySelective(project);
		if (0 != message.getReferUserId()) {
			// 新增一条消息（给甲方负责人看的）
			messageService.sendMessage(reUser.getPhone(), userId, "拒绝成为“" + project.getName() + "“项目负责人.",
					Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 0, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, refuseName,
					1);
			// todo 发送微信提醒
			// apiHelper.sendConten(template, userIdList, params);
		} else {
			// 新增一条系统消息（给客服看的）
			Message newSysMessage = new Message("", userId, refuseName + "拒绝成为“" + project.getName() + "“项目负责人.",
					Constant.MESSAGE_TYPE_INDEX.SYSTEM_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, new Date(), 0, 0);
			messageService.dao.insertSelective(newSysMessage);
		}
	}

}
