package com.xmsmartcity.maintain.service.impl.message_operation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.shiro.util.Assert;

import com.xmsmartcity.maintain.common.MessageOperationEntity;
import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.service.impl.MessageServiceImpl;
import com.xmsmartcity.util.Constant;

public class MessageOperation extends MessageOperationBase{

	public MessageOperation(Integer userId, Integer type, Integer messageId,MessageServiceImpl messageService){
		this.userId = userId;
		this.type = type;
		this.messageId = messageId;
		this.messageService = messageService;
	}
	
	public static Class<?>[] clss = new Class<?>[]{
		MessageOperationInvitedAPerson.class,
		MessageOperationInvitedBPerson.class,
		MessageOperationInvitedPeopleJoinCompany.class,
		MessageOperationAInvitedPeopleJoinProject.class,
		MessageOperationBInvitedPeopleJoinProject.class,
		MessageOperationInvitedPeopleChangeCompanyPerson.class,
		MessageOperationApplyJoinCompany.class,
		MessageOperationQuitProject.class,
		MessageOperationApplyJoinProject.class
	};
	
	/**
	 * 反射调用对象方法
	 * @author:zhugw
	 * @time:2017年7月28日 上午11:07:11
	 */
	public MessageOperationBase executeMessageOperation(){
		Message message = messageService.dao.selectByPrimaryKey(messageId);
		Assert.state(message.getState().equals(Constant.MESSAGE_STATE_INDEX.PENDING), Constant.MESSAGE_STATE_ERROR);
		boolean isError = true;
		for(Class<?> cls : clss){
			MessageOperationEntity annotation = cls.getAnnotation(MessageOperationEntity.class);
			if(annotation != null){
				int val = annotation.type();
				if(message.getType().equals(val)){
					try {
						Constructor<?> constructor = cls.getConstructor(Integer.class, Integer.class, Integer.class,MessageServiceImpl.class);
						return (MessageOperationBase)constructor.newInstance(userId,type,messageId,messageService);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(isError){
			Assert.state(false, Constant.SERVICE_ERROR);
		}
		return null;
	}
	
	@Override
	public void operationMsgConsent() {}

	@Override
	public void operationMsgReject() {}	
	
}
