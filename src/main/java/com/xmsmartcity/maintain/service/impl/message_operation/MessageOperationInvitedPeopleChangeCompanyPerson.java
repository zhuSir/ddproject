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
 * 6邀请成为团队负责人 
 * @author:zhugw
 * @time:2017年8月1日 上午10:05:44
 */
@MessageOperationEntity(type=6)
public class MessageOperationInvitedPeopleChangeCompanyPerson extends MessageOperationBase{

	public MessageOperationInvitedPeopleChangeCompanyPerson(Integer userId, Integer type, Integer messageId,
			MessageServiceImpl messageService) {
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}

	@Override
	@Transactional
	public void operationMsgConsent() {
		// 同意成为团队负责人
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		User user = messageService.userDao.selectByPrimaryKey(userId);
		DealEvent dealEvent = messageService.dealEventDao.selectByUserId(userId);
		
		Assert.state(user.getCompanyId().equals(Constant.ZERO) || user.getCompanyId().equals(message.getReferId()),
				Constant.ALREADY_HAS_COMPANY_ME);
		// 更新用户的团队信息
		user.setCompanyId(message.getReferId());
		user.setRole(Constant.USER_ROLE_INDEX.COMPANY_USER);
		user.setJoinTime(new Date());
		messageService.userDao.updateByPrimaryKeySelective(user);
		Company company = messageService.companyDao.selectByPrimaryKey(message.getReferId());
		Integer oldResponseId = null;
		if (company.getResponserId() != 0) {
			oldResponseId = company.getResponserId();
		}
		// 更新团队状态
		//company.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		company.setResponserId(userId);
		company.setResponserName(user.getName());
		company.setResponserPhone(user.getPhone());
		messageService.companyDao.updateByPrimaryKeySelective(company);
		// 更新用户等级
		//user.setRole(Constant.USER_ROLE_INDEX.COMPANY_USER);
		//messageService.userDao.updateByPrimaryKeySelective(user);
		if (dealEvent != null && dealEvent.getHasCompany().equals(0)) {
			dealEvent.setHasCompany(1);
			messageService.dealEventDao.updateByPrimaryKeySelective(dealEvent);
		}
		// 如果之前有负责人 需要降级
		if (null != oldResponseId) {
			User oldResponseUser = messageService.userDao.selectByPrimaryKey(oldResponseId);
			oldResponseUser.setRole(Constant.USER_ROLE_INDEX.GENERAL_USER);
			messageService.userDao.updateByPrimaryKeySelective(oldResponseUser);
		}
		// 更新消息状态_1同意
		message.setState(Constant.MESSAGE_STATE_INDEX.AGREE);
		message.setIsRead(Constant.ONE);
		messageService.dao.updateByPrimaryKeySelective(message);
	}

	@Override
	@Transactional
	public void operationMsgReject() {
		// 拒绝成为团队负责人
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
					"拒绝成为“" + messageService.companyDao.selectByPrimaryKey(message.getReferId()).getName() + "“的团队负责人.", 10,
					message.getReferId(), Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, 0,
					Constant.MESSAGE_STATE_INDEX.NO_HANDLE, userName, 1);
		} else {
			// 新增一条系统消息（给后台客服看的）
			Message sysMessage = new Message("", userId,
					userName + "拒绝成为“" + messageService.companyDao.selectByPrimaryKey(message.getReferId()).getName() + "“的团队负责人.",
					Constant.MESSAGE_TYPE_INDEX.SYSTEM_INDEX, message.getReferId(),
					Constant.MESSAGE_CLASSIFY_INDEX.COMPANY, new Date(), 0, 0);
			messageService.dao.insertSelective(sysMessage);
		}
	}

}
