package com.xmsmartcity.extra.weixin;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping(value = "/")
public class WxController {

	/**
	 * 接收微信事件推送的方法。
	 * 
	 * @author linweiqin
	 * @Date 2014-11-21 下午5:47:24
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/get-event", method = RequestMethod.POST)
	public void getEvent(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
//			InputStream is = request.getInputStream();
//			Map<String, String> map = XmlUtil.parseXml(is);
//			String type = map.get("Event");
//			if ("event".equals(map.get("MsgType"))) {
//				// 如果是关注或取消关注的事件。
//				boolean subscribe = "subscribe".equals(type);
//				boolean unsubscribe = "unsubscribe".equals(type);
//				if (subscribe || unsubscribe) {
//					UserInfo user = userInfoService.getUserByOpenId(map.get("FromUserName"));
//					if (subscribe) {
//						if(user == null){
//							WxUser wxUser = WxUtil.getWxUser(Constant.sysToken,map.get("FromUserName"));
//							user = userInfoService.newUser(wxUser.getOpenId(),wxUser.getNickName(),wxUser.getHeadImgUrl());
//						}
//					}
//					SubscribeRecord sr = new SubscribeRecord();
//					Date createDate = new Date();
//					sr.setCreateDate(createDate);
//					sr.setFromUserName(user.getWeixinId());
//					sr.setToUserName(map.get("ToUserName"));
//					if (subscribe) {
//						sr.setIsSubscribe(1);
//					} else if (unsubscribe) {
//						sr.setIsSubscribe(0);
//					}
//
//					subscribeRecordService.newRecord(sr, subscribe);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微信验证服务URL的方法（验证token）
	 * 
	 * @author linweiqn create_date 2014-11-23 下午1:26:24
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/get-event", method = RequestMethod.GET)
	public void getEvent(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String echostr = request.getParameter("echostr"); // 时间戳
			// 确认请求来至微信
			response.getWriter().print(echostr);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
