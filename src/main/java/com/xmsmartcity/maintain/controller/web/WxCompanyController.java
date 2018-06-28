package com.xmsmartcity.maintain.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import com.xmsmartcity.extra.weixin.WxConstant;
import com.xmsmartcity.extra.weixin.WxConstant.URL;
import com.xmsmartcity.extra.weixin.WxUtil;
import com.xmsmartcity.extra.weixin.pojo.WxToken;
import com.xmsmartcity.extra.weixin.pojo.WxUser;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.AES;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.MD5;

@Controller
public class WxCompanyController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;
	
	/**
	 * 微信 invite company member
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:26:22
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/wx/company/invite-member",method=RequestMethod.GET)
	public String joinMember(HttpServletRequest request,ModelMap model){
		String loc = ServletRequestUtils.getStringParameter(request, "loc", "");
		if(WxUtil.isWeixinBrowser(request)){
			//授权获取unionid
			String url =  WxUtil.goUserAuthorizeURL(URL.SERVE_NAME+request.getContextPath()+"/mp/wx/company/init",loc);
			System.out.println(WxConstant.URL.SERVE_NAME);
			System.out.println("wxurl=="+url);
			return url;
		}else{
			model.put("errorMsg", "请用微信平台打开！");
			return "/wxweb/error-page";
		}
	}
	
	/**
	 * 微信 init company page
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:26:33
	 * @param code
	 * @param state
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/mp/wx/company/init")
	public String initCompany(String code,String state,ModelMap model,HttpServletRequest request){
		try{			
			WxToken wxToken = WxUtil.getWxToken(code);
			WxUser wxUser = WxUtil.getWxUser(wxToken);
			WebUtils.setSessionAttribute(request, "TEMPUSER", wxUser);
			User userVo = userService.getUserByUnionid(wxUser.getUnionid());
			model.put("openId", wxUser.getOpenId());
			model.put("unionid", wxUser.getUnionid());
			if(userVo != null){
				if(userVo.getCompanyId() != 0){
					//邀请团队成员
					String inviteToken = userVo.getCompanyId()+"_0_"+userVo.getId();
					inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
					model.put("path","http://"+Constant.hostSite+"/dweibao/join-cm/"+inviteToken);
					return "/wxweb/company-invite";
				}else{
					//跳转到创建团队页面
					model.put("userId", userVo.getId());
					return "/wxweb/join-company";
				}
			}
			return "/wxweb/join-company";
		}catch(Exception e){
			e.printStackTrace();
			return "/wxweb/error-page";
		}
	}
	
	/**
	 * 微信跳转创建公司页面
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:26:43
	 * @param openId
	 * @param unionid
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("/wx/company/to/create-company")
	public String toCreateCompany(String openId,String unionid,Integer userId,ModelMap model){
		model.put("openId", openId);
		model.put("unionid", unionid);
		model.put("userId", userId);
		if(userId != null && userId != 0){
			return "/wxweb/company-create";
		}
		return "/wxweb/company-create-register";
	}
	
	/**
	 * 微信 create company and register 
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:26:58
	 * @param companyName
	 * @param username
	 * @param phone
	 * @param code
	 * @param unionid
	 * @param openId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/wx/company/create-register",method=RequestMethod.POST)
	public String createCompanyAndRegister(String companyName,String username,String phone,
			String code,String unionid,String openId,ModelMap model){
		try{
			if(!StringUtils.isNotEmpty(openId)
					|| !StringUtils.isNotEmpty(unionid)){
				return "/wxweb/error-page";
			}
			model.put("companyName", companyName);
			model.put("username", username);
			model.put("phone", phone);
			model.put("code", code);
			model.put("unionid", unionid);
			model.put("openId", openId);
			
			String phone_regex = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
			boolean isTrue = phone.matches(phone_regex);
			if(!isTrue){
				model.put("error", "手机号格式错误，请重新填写！");
				return "/wxweb/company-create-register";
			}
			User userVo = userService.getUserByUnionid(unionid);
			if(userVo != null){
				//unionid 已经绑定过。
				model.put("error","您的账号已绑定过，请重新刷新!");
				return "/wxweb/company-create-register";
			}
			model.put("openId", openId);
			model.put("unionid", unionid);
			User user = userService.selectByPhone(phone);
			if(user != null){
				//model.put("error", "该手机号已注册过，请重新输入");
				user.setUnionid(unionid);
				userService.updateByPrimaryKeySelective(user);
				if(!user.getCompanyId().equals(0)){
					model.put("error", "你已注册过团队，请刷新重试！");
					String inviteToken = user.getCompanyId()+"_0_"+user.getId();
					inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
					model.put("path","http://"+Constant.hostSite+"/dweibao/join-cm/"+inviteToken);
					return "/wxweb/company-invite";
				}else{
					//跳转到创建团队页面
					Company company = new Company();
					company.setName(companyName);
					company.setResponserId(user.getId());
					company = companyService.createCompany(company);
					//邀请团队成员
					String inviteToken = company.getId()+"_0_"+user.getId();
					inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
					model.put("path","http://"+Constant.hostSite+"/dweibao/join-cm/"+inviteToken);
					return "/wxweb/company-invite";
				}
			}
			String password = MD5.parseMD5("123456");
			user = userService.newUser(phone, username, password);
			user.setOpenId(openId);
			user.setUnionid(unionid);
			userService.updateByPrimaryKeySelective(user);
			
			Company company = new Company();
			company.setName(companyName);
			company.setResponserId(user.getId());
			company = companyService.createCompany(company);
			//邀请团队成员
			String inviteToken = company.getId()+"_0_"+user.getId();
			inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
			model.put("path","http://"+Constant.hostSite+"/dweibao/join-cm/"+inviteToken);
			return "/wxweb/company-invite";
		}catch(Exception e){
			e.printStackTrace();
			return "/wxweb/error-page";
		}
	}
	
	/**
	 * 微信 create company
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:27:10
	 * @param companyName
	 * @param region
	 * @param addr
	 * @param unionid
	 * @param userId
	 * @param openId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/wx/company/create",method=RequestMethod.POST)
	public String createCompany(String companyName,String region,String addr,
			String unionid,Integer userId,String openId,ModelMap model){
		try{
			model.put("openId", openId);
			model.put("unionid", unionid);
			model.put("userId", userId);
			model.put("addr", addr);
			model.put("region", region);
			model.put("companyName", companyName);
			if(userId == null || userId == 0 || !StringUtils.isNotEmpty(openId)
					|| !StringUtils.isNotEmpty(unionid)){
				return "/wxweb/error-page";
			}
			if(!StringUtils.isNotEmpty(companyName)){
				model.put("error", "团队名称不能为空，请重新填写！");
				return "/wxweb/company-create";
			}
			User userVo = userService.getUserByUnionid(unionid);
			if(userVo == null){
				//unionid 已经绑定过。
				return "redirect:/company-invite-member";
			}
			String[] strs = region.split("-");
			String province = strs[0];
			String city = strs[strs.length-2];
			String area = strs[strs.length-1];
			Company company = new Company();
			company.setProvince(province);
			company.setCity(city);
			company.setArea(area);
			company.setAddress(addr);
			company.setName(companyName);
			company.setResponserId(userVo.getId());
			company = companyService.createCompany(company);
			userVo.setCompanyId(company.getId());
			userService.updateByPrimaryKeySelective(userVo);
			//邀请团队成员
			String inviteToken = company.getId()+"_0_"+userId;
			inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
			model.put("path","http://"+Constant.hostSite+"/dweibao/join-cm/"+inviteToken);
			return "/wxweb/company-invite";
		}catch(Exception e){
			e.printStackTrace();
			return "/wxweb/error-page";
		}
	}
	
	/**
	 * 微信 意见反馈
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:27:19
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wx/to-feedback")
	public String toFeedback(ModelMap model,HttpServletRequest request){
		String loc = ServletRequestUtils.getStringParameter(request, "loc", "");
		if(WxUtil.isWeixinBrowser(request)){
			//授权获取unionid
			String url =  WxUtil.goUserAuthorizeURL(URL.SERVE_NAME+request.getContextPath()+"/mp/wx/init-toFeedback",loc);
			System.out.println(WxConstant.URL.SERVE_NAME);
			System.out.println("wxurl=="+url);
			return url;
		}else{
			model.put("errorMsg", "请用微信平台打开！");
			return "/wxweb/feedback";
		}
	}
	
	/**
	 * 微信 反馈页面
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:27:30
	 * @param code
	 * @param state
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/mp/wx/init-toFeedback")
	public String initToFeedback(String code,String state,ModelMap model,HttpServletRequest request){
		WxToken wxToken = WxUtil.getWxToken(code);
		WxUser wxUser = WxUtil.getWxUser(wxToken);
		WebUtils.setSessionAttribute(request, "TEMPUSER", wxUser);
		User userVo = userService.getUserByUnionid(wxUser.getUnionid());
		if(userVo != null){
			model.put("userId", userVo.getId());
		}
		return "/wxweb/feedback";
	}
	
	/**
	 * 微信邀请
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:28:23
	 * @return
	 */
	@RequestMapping("/select-join-company")
	public String joinCompany(){
		return "/wxweb/company-invite";
	}
	
}
