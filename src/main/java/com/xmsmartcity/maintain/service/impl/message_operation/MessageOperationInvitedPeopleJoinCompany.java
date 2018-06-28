package com.xmsmartcity.maintain.service.impl.message_operation;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.Assert;

import com.xmsmartcity.maintain.common.MessageOperationEntity;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 3邀请普通成员进团队 
 * @author:zhugw
 * @time:2017年8月1日 上午10:03:14
 */
@MessageOperationEntity(type=3)
public class MessageOperationInvitedPeopleJoinCompany extends MessageOperationBase {

	public MessageOperationInvitedPeopleJoinCompany(Integer userId, Integer type, 
			Integer messageId,MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}

	@Override
	@Transactional
	public void operationMsgConsent() {
		// 同意加入团队
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		Assert.state(user.getCompanyId().equals(Constant.ZERO), Constant.ALREADY_HAS_COMPANY_ME);
		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		// 更新用户的团队信息
		user.setCompanyId(message.getReferId());
		user.setJoinTime(new Date());
		messageService.userDao.updateByPrimaryKeySelective(user);
		if (dealEvent != null && dealEvent.getHasCompany().equals(0)) {
			dealEvent.setHasCompany(1);
			messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}

	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒绝加入团队
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		User reUser = messageService.userDao.selectByPrimaryKey(message.getReferUserId());
		String uuName = StringUtils.isNotBlank(user.getName()) ? user.getName() : user.getPhone();
		// 更新消息状态_2拒绝
		message.setState(Constant.MESSAGE_STATE_INDEX.REFUSE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
		Company refuseCompany = messageService.companyDao.selectByPrimaryKey(message.getReferId());
		// 新增一条消息
		messageService.sendMessage(reUser.getPhone(), userId, "拒绝加入“" + refuseCompany.getName() + "”.",
				Constant.MESSAGE_TYPE_INDEX.INVITED_PEOPLE_JOIN_COMPANY_INDEX, message.getReferId(),
				Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, 0, Constant.MESSAGE_STATE_INDEX.NO_HANDLE, uuName, 0);
		// todo 发送微信提醒
		// apiHelper.sendConten(template, userIdList, params);
	}

}
