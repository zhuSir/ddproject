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
 * 4甲方邀请普通成员进项目 
 * @author:zhugw
 * @time:2017年8月1日 上午10:04:39
 */
@MessageOperationEntity(type=4)
public class MessageOperationAInvitedPeopleJoinProject extends MessageOperationBase{

	public MessageOperationAInvitedPeopleJoinProject(Integer userId, Integer type, 
			Integer messageId,MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}
	
	@Override
	@Transactional
	public void operationMsgConsent() {
		// 同意加入项目甲方
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		Assert.state(!user.getCompanyId().equals(0), "您还未加入该团队请去加入团队。");
		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 新增项目人员记录
		ProjectUser pu = messageService.projectUserDao.selectByParames(null, userId, Constant.PROJECT_USER_TYPE_INDEX.A_INDEX, null,
				message.getReferId());
		if (pu == null) { // 查看原来的工作人员里面是否有该成员
			ProjectUser obj = new ProjectUser(Constant.ZERO, message.getReferId(), userId,
					Constant.PROJECT_USER_TYPE_INDEX.A_INDEX, Constant.ZERO);
			messageService.projectUserDao.insertSelective(obj);
		}
		// 更新用户项目信息
		if (dealEvent != null && dealEvent.getHasProject()==0) {
			dealEvent.setHasProject(1);
			messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
		Project newProject = messageService.projectDao.selectByPrimaryKey(message.getProjectId());
		User referUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		messageService.sendMessage(referUser.getPhone(), userId, "同意加入“" + newProject.getName() + "”项目.",
				Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX, message.getReferId(),
				Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 0, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, user.getName(), 0);
	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒绝加入项目甲方
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		String userName = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
		// 更新消息状态_2拒绝
		message.setState(Constant.MESSAGE_STATE_INDEX.REFUSE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 给转移团队的原负责人发消息
		if (!message.getReferUserId().equals(0)) {
			messageService.sendMessage(reUser.getPhone(), userId,
					"拒绝成为“" + messageService.projectDao.selectByPrimaryKey(message.getReferId()).getName() + "“项目甲方成员.",
					Constant.MESSAGE_TYPE_INDEX.A_INVITED_PEOPLE_JOIN_PROJECT_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, 0, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, userName, 1);
		} else {
			// 新增一条系统消息（给后台客服看的）
			Message sysMessage = new Message("", userId,
					"拒绝成为“" + messageService.projectDao.selectByPrimaryKey(message.getReferId()).getName() + "“项目甲方成员.",
					Constant.MESSAGE_TYPE_INDEX.SYSTEM_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, new Date(), 0, 0);
			messageService.dao.insertSelective(sysMessage);
		}
	}

}
