package com.xmsmartcity.maintain.controller.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.xmsmartcity.extra.weixin.TemplateEnum;
import com.xmsmartcity.maintain.pojo.User;


@Component
public class SendMsgTask extends Task implements ApplicationContextAware {

	private String keyword1;
	private String keyword2;
	private String keyword3;
	private String firstData;
	private String remarkData;
	private TemplateEnum enum1;
	private List<User> userList;
	
	private ApiHelper apiHelper ;
	//private SendEmailUtils sendEmailUtils;
	
	public SendMsgTask(String firstData,String keyword1,String keyword2,String keyword3,String remarkData,TemplateEnum enum1,List<User> userList) {
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.firstData = firstData;
		this.remarkData = remarkData;
		this.enum1 = enum1;
		this.userList =userList;
	}

	public SendMsgTask(){}
	@Override
	public String deal() throws Exception {
		System.out.println("begin wxAndMsgSend:"+System.currentTimeMillis());
		apiHelper = (ApiHelper) getBean("apiHelper");
		List<Integer> userIdList = new ArrayList<Integer>();
		for (User user : userList) {
			userIdList.add(user.getId());
		}
		System.out.println(enum1.getName());
//		if(enum1.equals(TemplateEnum.temp2)){
//			System.out.println("temp2");
//			apiHelper.sendConten(enum1,userIdList, firstData,keyword1,keyword2,remarkData);
//		}else if(enum1.equals(TemplateEnum.temp5)){
//			System.out.println("temp5");
//			
//		}
		apiHelper.sendConten(enum1,userIdList, firstData,keyword1,keyword2,remarkData);
		
		//消息发送
		
		//邮件
//		for (User user : userList) {
//			if(StringUtils.isBlank(user.getMail())){
//				sendEmailUtils = new SendEmailUtils(user.getMail());
//				sendEmailUtils.send(subject, content);
//			}
//		}
		System.out.println("end wxAndMsgSend:"+System.currentTimeMillis());
		return null;
	}

	private static ApplicationContext context;

    public static ApplicationContext getContext(){
        return context;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext arg0)
			throws BeansException {
		 context = arg0;
	}
	
}
