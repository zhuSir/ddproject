package com.xmsmartcity.maintain.service.impl.message_operation;

import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.shiro.util.Assert;

import com.xmsmartcity.maintain.common.MessageOperationEntity;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.ProjectUser;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.util.Constant;

/**
 * 9 退出项目
 * @author:zhugw
 * @time:2017年8月1日 上午10:06:33
 */
@MessageOperationEntity(type=9)
public class MessageOperationQuitProject extends MessageOperationBase{
	
	public MessageOperationQuitProject(Integer userId, Integer type, Integer messageId,
			MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}

	@Override
	@Transactional
	public void operationMsgConsent() {
		
		// 同意成员退出项目
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		
		Assert.state(!user.getCompanyId().equals(0), "您还未加入该团队请去加入团队。");
		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 新增项目人员记录
		ProjectUser projectUser = messageService.projectUserDao.selectByParames(message.getProjectId(), message.getReferUserId(), null,
				null, message.getReferId().equals(message.getProjectId())?null:message.getReferId());
		if (projectUser != null) {
			// 假删除项目用户
			projectUser.setIsDel(1);
			messageService.projectUserDao.updateByPrimaryKeySelective(projectUser);
		}
		// 通知用户
		User referUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		messageService.sendMessage(referUser.getPhone(), userId, "项目负责人已同意您退出项目，快去看看吧~", Constant.MESSAGE_TYPE_INDEX.QUIT_PROJECT,
				message.getReferId(), Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, message.getProjectId(),
				Constant.MESSAGE_STATE_INDEX.NO_HANDLE, user.getName(), 1);
		// 更新用户项目信息
		dealEvent = messageService.dealEventDao.selectByUserId(message.getReferUserId());
		if (dealEvent != null && dealEvent.getHasProject() == 1) {
			List<ProjectUser> res= messageService.projectUserDao.selectListByParames(null, message.getReferUserId(), null, null, null);
			if(res == null || res.size() < 1){
				dealEvent.setHasProject(0);
				messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);				
			}
		}
	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒绝成员退出项目
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		
		Assert.state(!user.getCompanyId().equals(0), "您还未加入该团队请去加入团队。");
		// 更新消息状态
		message.setState(Constant.MESSAGE_STATE_INDEX.REFUSE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 通知用户
		messageService.sendMessage(message.getPhone(), userId, "项目负责人已拒绝您退出团队，快去看看吧~", Constant.MESSAGE_TYPE_INDEX.QUIT_PROJECT,
				message.getReferId(), Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, message.getProjectId(),
				Constant.MESSAGE_STATE_INDEX.NO_HANDLE, user.getName(), 1);
	}

}
