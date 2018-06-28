package com.xmsmartcity.maintain.service.impl.message_operation;

import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;
import com.xmsmartcity.util.Constant;

import javax.transaction.Transactional;

import org.apache.shiro.util.Assert;

import com.xmsmartcity.maintain.common.MessageOperationEntity;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.ProjectUser;
import com.xmsmartcity.maintain.pojo.User;

/**
 * 10加入项目
 * @author:zhugw
 * @time:2017年8月1日 上午10:06:55
 */
@MessageOperationEntity(type=10)
public class MessageOperationApplyJoinProject extends MessageOperationBase{
	
	public MessageOperationApplyJoinProject(Integer userId, Integer type, Integer messageId,
			MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}

	@Override
	@Transactional
	public void operationMsgConsent() {
		//同意申请加入项目
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		
		Assert.state(!user.getCompanyId().equals(0), "您还未加入该团队请去加入团队。");
		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		//查看是否已经加入
		Integer referId = message.getReferId();
		// 新增项目人员记录
		if(referId == null || referId == 0){
			ProjectUser pu = messageService.projectUserDao.selectByParames(message.getProjectId(),message.getReferUserId(),null,null,null);
			Assert.state(pu == null,"该用户已是项目成员,请刷新重试！");
			//甲方
			ProjectUser projectUser = new ProjectUser(Constant.ZERO, message.getProjectId(), message.getReferUserId(),
					Constant.PROJECT_USER_TYPE_INDEX.A_INDEX, Constant.ZERO);
			messageService.projectUserDao.insertSelective(projectUser);
		}else{
			ProjectUser pu = messageService.projectUserDao.selectByParames(message.getProjectId(),message.getReferUserId(),null,null,message.getReferId());
			Assert.state(pu == null,"该用户已是项目成员,请刷新重试！");
			//乙方
			ProjectUser projectUser = new ProjectUser(Constant.ZERO, message.getProjectId(), message.getReferUserId(),
					Constant.PROJECT_USER_TYPE_INDEX.B_INDEX, message.getReferId());
			messageService.projectUserDao.insertSelective(projectUser);
		}
		// 通知用户
		messageService.sendMessage(reUser.getPhone(), userId, "项目负责人已同意您加入项目，快去看看吧~", Constant.MESSAGE_TYPE_INDEX.APPLY_JOIN_PROJECT_INDEX,
				message.getReferId(), Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, message.getProjectId(),
				Constant.MESSAGE_STATE_INDEX.NO_HANDLE, user.getName(), 1);
		// 更新用户项目信息
		dealEvent = messageService.dealEventDao.selectByUserId(message.getReferUserId());
		if (dealEvent != null && dealEvent.getHasProject() == 0) {
			dealEvent.setHasProject(1);
			messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒接申请加入项目
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		
		Assert.state(!user.getCompanyId().equals(0), "您还未加入该团队请去加入团队。");
		// 更新消息状态
		message.setState(Constant.MESSAGE_STATE_INDEX.REFUSE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 通知用户
		messageService.sendMessage(reUser.getPhone(), userId, "项目负责人已拒绝您加入项目，快去看看吧~", Constant.MESSAGE_TYPE_INDEX.APPLY_JOIN_PROJECT_INDEX,
				message.getReferId(), Constant.MESSAGE_CLASSIFY_INDEX.PROJECT, message.getProjectId(),
				Constant.MESSAGE_STATE_INDEX.NO_HANDLE, user.getName(), 1);
	}

}
