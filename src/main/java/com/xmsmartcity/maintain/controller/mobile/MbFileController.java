package com.xmsmartcity.maintain.controller.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.extra.weixin.TemplateEnum;
import com.xmsmartcity.extra.weixin.WxConstant;
import com.xmsmartcity.extra.weixin.WxConstant.URL;
import com.xmsmartcity.extra.weixin.WxUtil;
import com.xmsmartcity.extra.weixin.pojo.WxToken;
import com.xmsmartcity.extra.weixin.pojo.WxUser;
import com.xmsmartcity.maintain.controller.util.ApiHelper;
import com.xmsmartcity.maintain.controller.util.ControllerHelper;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.MD5;

@Controller
@RequestMapping("/mp")
public class MbFileController {
	
	private Logger logger = LoggerFactory.getLogger(MbFileController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private ApiHelper apiHelper;
	
	/**
	 * 获取微信新的返回值
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="")
	public void wxCallBacknew(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 response.setCharacterEncoding(Constant.UTF8);
	        response.setContentType(Constant.TEXT_HTML);
	        PrintWriter writer = response.getWriter();
	        writer.write("oYlA8oTPauzGs1yS");
	        writer.flush();
	        writer.close();
	}
	
	/**
	 * 获取微信返回值
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/MP_verify_oYlA8oTPauzGs1yS.txt")
	public void wxCallBack(HttpServletRequest request,HttpServletResponse response) throws IOException{
		 response.setCharacterEncoding(Constant.UTF8);
	        response.setContentType(Constant.TEXT_HTML);
	        PrintWriter writer = response.getWriter();
	        writer.write("oYlA8oTPauzGs1yS");
	        writer.flush();
	        writer.close();
	}
 	
	/**
	 * 微信绑定、登录相关跳转
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mobile/register/go/new-file" , method = RequestMethod.GET)
	public String  goNewFile(HttpServletRequest request,Model model){
		System.out.println(WxConstant.URL.SERVE_NAME);
		String loc = ServletRequestUtils.getStringParameter(request, "loc", "");
		model.addAttribute("loc", loc);
		if(WxUtil.isWeixinBrowser(request)){
			String url =  WxUtil.goUserAuthorizeURL(URL.SERVE_NAME+request.getContextPath()+"/mp/mobile/register/go/bind-weixin",  loc);
			System.out.println("wxurl=="+url);
			return url;
		}
		return "/mobile/login";
	}
	 /**
		 * 
		 * @author linweiqin
		 * @createDate 2015-4-8 下午4:15:08
		 * @param code
		 * @param state
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/mobile/register/go/bind-weixin", method = RequestMethod.GET)
		public String goBindWeixin(String code,String state,Model model,HttpServletRequest request){
			logger.info("the code is :" + code + "; the state is :" + state);
			WxToken wxToken = WxUtil.getWxToken(code);
			WxUser wxUser = WxUtil.getWxUser(wxToken);
			WebUtils.setSessionAttribute(request, "TEMPUSER", wxUser);
			if(!StringUtils.isNotEmpty(wxUser.getUnionid())){
				logger.error("公众号未关联微信开放平台，获取不到unionid");
			}
			User userVo = userService.getUserByUnionid(wxUser.getUnionid());
			if(userVo!=null){
				//String content="\"data\":{\"first\": {\"value\":\"您有1条款式进度提醒，请关注\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"合同【DS12232121】的款号【短袖10239】的【产前样】节点\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"计划时间为2016-01-10\",\"color\":\"#173177\"},\"keyword3\": {\"value\":\"该节点临近计划时间，负责人为刘菲菲、陈潇\",\"color\":\"#173177\"},\"keyword4\": {\"value\":\"请提前做好该节点的启动工作，并确保按计划执行\",\"color\":\"#173177\"},\"remark\": {\"value\":\"谢谢各位！\",\"color\":\"#173177\"}}";
				return "redirect:/mp/mobile/register/go/binding-ok?id="+userVo.getId();
			}
			model.addAttribute("wxUser", wxUser);
			model.addAttribute("historyurl",state);
			//String content="\"data\":{\"first\": {\"value\":\"您有1条款式进度提醒，请关注\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"合同【DS12232121】的款号【短袖10239】的【产前样】节点\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"计划时间为2016-01-10\",\"color\":\"#173177\"},\"keyword3\": {\"value\":\"该节点临近计划时间，负责人为刘菲菲、陈潇\",\"color\":\"#173177\"},\"keyword4\": {\"value\":\"请提前做好该节点的启动工作，并确保按计划执行\",\"color\":\"#173177\"},\"remark\": {\"value\":\"谢谢各位！\",\"color\":\"#173177\"}}";
			return "/mobile/login";
		}
	
	/**
	 * 微信绑定
	 * @param userName
	 * @param password
	 * @param unionid
	 * @param openId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/mobile/register/go/binding" , method = RequestMethod.POST)
	public void binding(String userName,String password,String unionid,String openId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		try{			
			User user = userService.login(userName.trim(), MD5.parseMD5(password));
			if(StringUtils.isBlank(openId)){
				ControllerHelper.sendErrorJson(response, "微信绑定失败，openID为空");
				return ;
			}
			if(null == user ){
				ControllerHelper.sendErrorJson(response, "账号或密码错误");
				return ;
			}
			user.setOpenId(openId);
			user.setUnionid(unionid);
			userService.update(user);
			JSONObject object = new JSONObject();
			object.put("info", user.getId());
			object.put("success", true);
			ControllerHelper.sendJson(response, object.toString());
		}catch(Exception e){
			logger.debug("error message: "+e.getMessage());
			e.printStackTrace();
			ControllerHelper.sendErrorJson(response, e.getMessage());
		}
	}
	
	/**
	 * 微信完成绑定
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mobile/register/go/binding-ok" , method = RequestMethod.GET)
	public String  goBinding(Integer id,Model model){
		
		User user = userService.selectByPrimaryKey(id);
		model.addAttribute("user", user);
		return "/mobile/account-details";
	}
	
	@RequestMapping(value = "/go/detail" , method = RequestMethod.GET)
	public String  goDetail(){
		return "/mobile/order-datail";
	}
	
	@RequestMapping(value = "/go/test" , method = RequestMethod.GET)
	public void testSendInfo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<Integer> userIdList = new ArrayList<Integer>();
		userIdList.add(52);
//		String firstData = "";
		String keyword1Data="1";
		String keyword2Data = "2";
//		String keyword3Data = "3";
//		String keyword4Data = "4";
//		String remarkData = "";
		apiHelper.sendConten(TemplateEnum.temp1,userIdList , keyword1Data,keyword2Data);
		ControllerHelper.sendSuccessJson(response);
	}
	 
	
	
	

}
