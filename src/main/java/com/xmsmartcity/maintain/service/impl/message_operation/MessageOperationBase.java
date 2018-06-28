package com.xmsmartcity.maintain.service.impl.message_operation;

import javax.transaction.Transactional;

import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;

public abstract class MessageOperationBase {
	
	public Integer userId;
	public Integer type; 
	public Integer messageId;
	public MessageServiceImpl messageService;

	@Transactional
	public abstract void operationMsgConsent();
	
	@Transactional
	public abstract void operationMsgReject();
	
}
