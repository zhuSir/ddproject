package com.xmsmartcity.maintain.service.impl.message_operation;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.shiro.util.Assert;

import com.xmsmartcity.maintain.common.MessageOperationEntity;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 8申请加入企业 
 * @author:zhugw
 * @time:2017年8月1日 上午10:06:09
 */
@MessageOperationEntity(type=8)
public class MessageOperationApplyJoinCompany extends MessageOperationBase{

	public MessageOperationApplyJoinCompany(Integer userId, Integer type, Integer messageId,
			MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}

	@Override
	@Transactional
	public void operationMsgConsent() {
		// 同意别人申请加入团队
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		
		Assert.state(reUser.getCompanyId().equals(Constant.ZERO),
				Constant.ALREADY_HAS_COMPANY_OTHER);
		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 更新用户的团队信息
		reUser.setCompanyId(message.getReferId());
		reUser.setJoinTime(new Date());
		messageService.userDao.updateByPrimaryKeySelective(reUser);
		dealEvent = messageService.dealEventDao.selectByUserId(message.getReferUserId());
		if (dealEvent != null && dealEvent.getHasCompany().equals(0)) {
			dealEvent.setHasCompany(1);
			messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
		messageService.sendMessage(reUser.getPhone(), userId, "同意了您的加入申请.", Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX,
				message.getReferId(), Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, 0, Constant.THREE,user.getName(), 1);
	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒绝别人申请加入团队
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		
		// 更新消息状态_2拒绝
		message.setState(Constant.MESSAGE_STATE_INDEX.REFUSE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		Company company1 = messageService.companyDao.selectByPrimaryKey(message.getReferId());
		// 给申请者推送一条消息
		messageService.sendMessage(reUser.getPhone(), userId, "拒绝了您的加入申请.", Constant.MESSAGE_TYPE_INDEX.OTHER_INDEX,
				message.getReferId(), Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, 0, Constant.THREE,
				company1.getName(), 1);
		// todo 发送微信提醒
		// apiHelper.sendConten(template, userIdList, params);
	}

}
