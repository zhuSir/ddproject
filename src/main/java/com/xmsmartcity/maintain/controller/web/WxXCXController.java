package com.xmsmartcity.maintain.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WxXCXController {
	
	/**
	 * 微信扫码进入小程序版本过低提示
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/xcx/qr",method=RequestMethod.GET)
	public String qr(HttpServletRequest request){
		return "/xcx/qr";
	}
}
