package com.xmsmartcity.maintain.controller.mobile;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.pojo.Message;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

/**
 * 消息处
 * 
 * @author Owner
 *
 */
@Controller
@RequestMapping("/api/message")
public class MessageApiController {

	@Autowired
	private MessageService service;

	@RequestMapping("/message-list")
	public String getMsgList() {

		return null;
	}

	/**
	 * 消息首页
	 * 
	 * @param phone
	 * @param userId
	 * @param token
	 * @return 返回最新一条数据，及对应消息类型的未读书
	 */
	@RequestMapping(value = "/message-index")
	@ResponseBody
	public String messageIndex(Integer userId, String token) {
		String res = service.getIndexMsg(userId);
		return Utils.toSuccessJsonRes(res);
	}

	/**
	 * 
	 * 消息首页获取具体类型消息列表
	 * @author:zhugw
	 * @time:2017年4月9日 下午2:57:48
	 * @param userId
	 * @param token
	 * @param classify
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/message-index-classify")
	@ResponseBody
	public String messageIndexByClassify(Integer userId, String token, Integer classify, Integer startIndex,
			Integer pageSize) {
		Assert.state(startIndex != null&&startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		List<Message> res = service.getMessageByClassify(userId,classify,startIndex,pageSize);
		return Utils.toSuccessJson(res);
	}
	
	/**
	 * 消息类操作
	 * 
	 * @param userId
	 * @param type
	 *            0同意 1拒绝
	 * @param messageId
	 * 
	 */
	@RequestMapping(value = "/message-operate")
	@ResponseBody
	public String messageOperate(Integer userId, String token, Integer type,Integer messageId) {
		Assert.state(messageId != null && messageId > 0,
				Constant.PARAMS_ERROR);
		Assert.state(type != null, Constant.PARAMS_ERROR);
		service.messageOperate(userId,type,messageId);
		return Utils.toSuccessJson("操作成功");
	}

	/**
	 * 首页待处理事项
	 * @time:2017年11月29日 上午11:20:46
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/message-getListByState")
	@ResponseBody
	public String getMessageByState(Integer userId, String token, Integer startIndex,Integer pageSize) {
		Assert.state(startIndex != null && pageSize !=null,
				Constant.PARAMS_ERROR);
		List<Map<String, Object>> list= service.getMessageByState(0, userId, startIndex, pageSize);
		return Utils.toSuccessJson(list);
	}
	
	/**
	 * 设置消息为已读
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:20:56
	 * @param userId
	 * @param token
	 * @param messageId
	 * @return
	 */
	@RequestMapping(value = "/set-isRead")
	@ResponseBody
	public String setMessageIsRead(Integer userId, String token, String messageId) {
		service.setMessageIsRead(messageId,userId);
		return Utils.toSuccessJson("操作成功");
	}
	
	/**
	 * 设置消息删除
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:21:11
	 * @param userId
	 * @param token
	 * @param messageId
	 * @return
	 */
	@RequestMapping(value = "/message-del")
	@ResponseBody
	public String delMessages(Integer userId, String token, String messageId) {
		service.delMessageById(messageId,userId);
		return Utils.toSuccessJson("操作成功");
	}
	
	/**
	 * app 首页获取邀请消息
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:21:25
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/invite-message")
	@ResponseBody
	public String getInviteMessage(Integer userId) {
		List<Map<String,Object>> res = service.getInviteMessage(userId);
		return Utils.toSuccessJson(res);
	}
}
