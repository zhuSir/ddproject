package com.xmsmartcity.maintain.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/message")
public class WebMessageController {
	
	@Autowired
	private MessageService service;

	/**
	 * 页面分类消息
	 * @author:zhugw
	 * @param userId
	 * @param companyId
	 * @param searchPhone
	 * @return
	 */
	@RequestMapping(value="/classify-company")
	@ResponseBody
	public String searchMember(Integer userId,Integer classify,Integer pageNo,Integer pageSize){
		Assert.state(userId != null && userId > 0,Constant.PARAMS_ERROR+"：userID错误,为null或为0");
		Object obj = service.selectMessageByClassify(userId,classify,pageNo,pageSize);
		return Utils.toSuccessJsonResults(obj);
	}
}
