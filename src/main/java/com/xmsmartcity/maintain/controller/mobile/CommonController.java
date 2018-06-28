package com.xmsmartcity.maintain.controller.mobile;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.extra.weixin.WxConstant;
import com.xmsmartcity.extra.weixin.WxConstant.URL;
import com.xmsmartcity.extra.weixin.WxUtil;
import com.xmsmartcity.extra.weixin.pojo.WxToken;
import com.xmsmartcity.extra.weixin.pojo.WxUser;
import com.xmsmartcity.maintain.controller.util.SendEmailUtils;
import com.xmsmartcity.maintain.controller.util.UploadHelper;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.InviteParams;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CommonService;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.ProjectService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.maintain.service.ValidateCodeService;
import com.xmsmartcity.util.AES;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.MD5;
import com.xmsmartcity.util.Utils;

@Controller
public class CommonController {

	private Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private UploadHelper uploadHelper;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ValidateCodeService codeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProjectService projectService;
	
	
	/**
	 * 文件上传
	 * 注意：如果是html提交文件，input里面的 name一定要是file
	 * @param userId
	 * @param type 
	 * 			1:用户图片
	 * 			2:团队图片
	 * 			3:项目图片
	 * 			4:报障图片
	 * @param token
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajaxfileupload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(Integer userId,Integer type,String token,MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
//			if (file.getSize() > 1024 * 1024) {
//				logger.debug("文件大小为：" + file.getSize() / 1024 / 1024 + "MB");
//			} else {
//				logger.debug("文件大小为：" + file.getSize() / 1024 + "KB");
//			}
			// 设置响应给前台内容的数据格式
			response.setContentType("text/plain; charset=UTF-8");
			int sizeLimit = 30 * 1024 * 1024;
			JSONObject jobject = uploadHelper.saveSingleFile(request, file,type,true, sizeLimit);
			return jobject.toString();
		} catch (Exception e) {
//			logger.error("文件上传失败", e);
			return Utils.toFailJson("文件上传失败",e);
		}
	}
	
	/**
	 * 多文件上传
	 * 注意：如果是html提交文件，input里面的 name一定要是file
	 * @param userId
	 * @param type
	 * 			1:用户图片
	 * 			2:团队图片
	 * 			3:项目图片
	 * 			4:报障图片
	 * @param token
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/ajaxfileupload/batch", method = RequestMethod.POST)
	@ResponseBody
	public String upload(Integer userId,Integer type,String token,@RequestParam(required=false) MultipartFile[] file, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			if (null == file) {
				return Utils.toFailJson("您上传的是空文件",null);
			}
			// 设置响应给前台内容的数据格式
			response.setContentType("text/plain; charset=UTF-8");
			int sizeLimit = 30 * 1024 * 1024;
			JSONArray list = new JSONArray();
			for (int i = 0; i < file.length; i ++) {
				JSONObject result = uploadHelper.saveSingleFile(request,file[i],type,true, sizeLimit);
				if (!result.getBooleanValue("success")) {
					continue;
				}
				list.add(result);
			}
			JSONObject result = new JSONObject();
			result.put("code",0);
			result.put("data",list);
			result.put("info", "");
			return result.toString();
		} catch (Exception e) {
			return Utils.toFailJson("上传失败",e);
		}
	}
	
	/**
	 * 上传文件
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:16:42
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/uploadFile",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String CC(@RequestParam(required=false) MultipartFile[] file,HttpServletRequest request){
		try {
			int sizeLimit = 30 * 1024 * 1024;
			JSONObject res = null;
			for(int i=0;i<file.length;i++){				
				res = uploadHelper.saveSingleFile(request, file[i],0,false, sizeLimit);
			}
			return Utils.toSuccessJsonRes(res.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.toFailJson(e.getMessage(),e);
		}
	}
	
	/**
	 * 返回测试页面
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:17:19
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/to-test")
	public String toTest(HttpServletRequest request,HttpServletResponse response){
		if("127.0.0.1".equals(request.getLocalAddr())){
			return "test";			
		}
		return "mobile/error";
	}
	
	/**
	 * 意见反馈
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:17:45
	 * @param content
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/advice-feedback")
	@ResponseBody
	public String sendMail(String content,Integer userId,String token){
		try{
			Assert.state(StringUtils.isNotBlank(content),Constant.PARAMS_ERROR);
			//Assert.state(StringUtils.isNotBlank(token), Constant.PARAMS_ERROR);
			//String email_regex = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";
			//String mail = Constant.MAIL;
			//Assert.state(mail.matches(email_regex),"邮箱格式错误，请重新输入");
			System.out.println("======================= content : "+content);
			User user = userService.selectByPrimaryKey(userId);
			if(user != null){
				content = " 用户："+user.getName()+" <br/> 用户手机号："+user.getPhone()+" <br/> 用户反馈意见如下： <br/> "+content;
			}else{
				content = " 用户：游客 <br/>  用户反馈意见如下： <br/> "+content;
			}
			SendEmailUtils sendMail = new SendEmailUtils(Constant.MAIL);
			String subject = "咚咚维保云平台用户意见反馈";
			boolean isTrue = sendMail.sendHtmlMail(subject,content);//发送申请邮件
			if(!isTrue){
				logger.info("用户反馈意见发送邮件错误,请移步查看~");
			}
			return Utils.toSuccessJson("反馈成功，谢谢您对我们咚咚的支持！");
		}catch(Exception e){
			e.printStackTrace();
			return Utils.toFailJson(e.getMessage(),e);
		}
	}
	
	/**
	 * 获取app版本号 
	 * @author:zhugw
	 * @time:2017年6月22日 上午11:05:48
	 * @param appType 类型(0:IOS,1:ANDROID)
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/app-version")
	@ResponseBody
	public String getAppVersion(Integer appType,String version)throws Exception{
		try{
			String appVersion = commonService.getAppVersion(appType,version);
			return Utils.toSuccessJsonRes(appVersion);
		}catch(Exception e){
			e.printStackTrace();
			return Utils.toFailJson(e.getMessage(),e);
		}
	}
	
	/**
	 * 帮助文档
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/help-docment")
	public String getHelpDoc(Integer userId,String token){
		return "helpdoc/index.html";
	}
	
	/**
	 * APP快速上手
	 * @param userId
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/help-html")
	public String getHelpDocHtml(Integer userId,String token) throws IOException{
		return "redirect:/quickStart/index.html";
	}
	
	/**
	 * 加入团队
	 * @param companyId
	 * @param inviteUserId
	 *            被邀请的人的id
	 * @throws IOException 
	 * 
	 */
	@RequestMapping(value = "/join-cm/{token}")
	public String joinCompanyMember(@PathVariable String token,ModelMap model,HttpServletRequest request){
		try{
			String str = AES.decrypt(token);
			if(str == null){
				logger.info("error token: "+token);
				return "mobile/error";
			}
			String[] datas = str.split("_");
			Integer companyId = Integer.valueOf(datas[0]);
			String phone = datas[1];
			User user = userService.getUserByPhone(phone);
			Integer reUserId = Integer.valueOf(datas[2]);
			User reUser = userService.selectByPrimaryKey(reUserId);
			model.put("reUserName", reUser.getName());
			model.put("phone", reUser.getPhone());
			model.put("reUserId", reUserId);
			model.put("companyId", companyId);
			String loc = ServletRequestUtils.getStringParameter(request, "loc", "");
			if(WxUtil.isWeixinBrowser(request)){
				String url =  WxUtil.goUserAuthorizeURL(URL.SERVE_NAME+request.getContextPath()+"/mp/join-c/"+token,loc);
				logger.info(WxConstant.URL.SERVE_NAME);
				logger.info("wxurl=="+url);
				return url;
			}
			//已加入团队
			Company company = companyService.selectByPrimaryKey(companyId);
			model.put("companyName", company.getName());
			if(user != null){
				if(user.getCompanyId()!= 0 && !user.getCompanyId().equals(companyId)){
					Assert.state(false,"该手机号已加入其他团队，请重新输入！");
				}
				model.put("userName", user.getName());
				if(user.getCompanyId().equals(Constant.ZERO)){				
					//join company
					companyService.joinCompanyMember(phone,user.getId(),companyId,reUserId);
				}
				return "mobile/join-company-result";
			}
			//joined company
			return "mobile/join-company";
		}catch(Exception e){
			e.printStackTrace();
			String msg = e.getMessage();
			if(!(e instanceof IllegalStateException)&&
	    			!(e instanceof IllegalArgumentException)){
	    		msg = "serviceError_服务器异常，请稍后重试";
	    	}
			model.put("error",msg);
			return "mobile/join-company";
		}
	}
	
	
	/**
	 * 微信端加入团队
	 * @param companyId
	 * @param inviteUserId
	 *            被邀请的人的id
	 * @throws IOException 
	 * 
	 */
	@RequestMapping(value = "/mp/join-c/{token}",method = RequestMethod.GET)
	public String weixinJoinCompanyMember(String code,String state,@PathVariable String token,
			ModelMap model,HttpServletRequest request){
		try{
			String str = AES.decrypt(token);
			if(str == null){
				logger.info("error token: "+token);
				return "mobile/error";
			}
			if(!StringUtils.isNotEmpty(code)){
				return "mobile/error";
			}
			String[] datas = str.split("_");
			Integer companyId = Integer.valueOf(datas[0]);
			String phone = datas[1];
			User user = userService.getUserByPhone(phone);
			Integer reUserId = Integer.valueOf(datas[2]);
			
			WxToken wxToken = WxUtil.getWxToken(code);
			WxUser wxUser = WxUtil.getWxUser(wxToken);
			WebUtils.setSessionAttribute(request, "TEMPUSER", wxUser);
			model.put("userName", wxUser.getNickName());
			model.put("openId", wxUser.getOpenId());
			model.put("unionid", wxUser.getUnionid());
			User reUser = userService.selectByPrimaryKey(reUserId);
			model.put("reUserName", reUser.getName());
			model.put("phone", reUser.getPhone());
			model.put("reUserId", reUserId);
			model.put("companyId", companyId);
			User userVo = userService.getUserByUnionid(wxUser.getUnionid());
			//绑定过并加入别的团队
			if(userVo != null && !userVo.getCompanyId().equals(companyId)){
				model.put("error", "您已经加入团队了!");
				//返回成功
				return "mobile/join-company";
			}
			//已加入团队
			Company company = companyService.selectByPrimaryKey(companyId);
			model.put("companyName", company.getName());
			if(user != null){
				if(user.getCompanyId()!= 0 && !user.getCompanyId().equals(companyId)){
					Assert.state(false,"该手机号已加入其他团队，请重新输入！");
				}
				model.put("userName", user.getName());
				if(user.getCompanyId().equals(Constant.ZERO)){
					//update openId
					user.setOpenId(wxUser.getOpenId());
					user.setUnionid(wxUser.getUnionid());
					userService.updateByPrimaryKeySelective(user);
					//join company
					companyService.joinCompanyMember(phone,user.getId(),companyId,reUserId);
				}
				return "mobile/join-company-result";
			}
			//joined company
			return "mobile/join-company";
		}catch(Exception e){
			e.printStackTrace();
			String msg = e.getMessage();
			if(!(e instanceof IllegalStateException)&&
        			!(e instanceof IllegalArgumentException)){
        		msg = "serviceError_服务器异常，请稍后重试";
        	}
			model.put("error",msg);
			return "mobile/join-company";
		}
	}

	/**
	 * 加入团队（web邀请成员）
	 * @author:zhugw
	 * @time:2017年8月8日 上午10:20:32
	 * @param phone
	 * @param userName
	 * @param code
	 * @param password
	 * @param reUserId
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value="/join-company")
	public String joinCompany(String unionid,String openId,String phone,String userName,String code,String password,
			Integer reUserId,Integer companyId,ModelMap model){
		try{
			User reUser = userService.selectByPrimaryKey(reUserId);
			Company company = companyService.selectByPrimaryKey(companyId);
			model.put("userName", userName);
			model.put("reUserName", reUser.getName());
			model.put("phone", reUser.getPhone());
			model.put("reUserId", reUserId);
			model.put("companyId", companyId);
			model.put("companyName", company.getName());
			model.put("openId", openId);
			model.put("unionid", unionid);
			Assert.state(StringUtils.isNotBlank(code), Constant.EMPTY_CODE);
			Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
			Assert.state(StringUtils.isNotBlank(userName), Constant.EMPTY_NAME);
			Assert.state(StringUtils.isNotBlank(password),
					Constant.EMPTY_PASSWORD);
			Assert.state(Constant.WANNENG_CODE.equals(code.toUpperCase())
							|| codeService.validateCode(phone, code),Constant.CODE_ERROR);
			User user = userService.selectByPhone(phone);
			if(user == null){
				//register user
				password = MD5.parseMD5(password);
				user = userService.newUser(phone, userName,password);				
			}else if(!user.getCompanyId().equals(0)){
				Assert.state(false,"该手机号已注册过，并已加入其它团队！");
			}
			if(openId != null && unionid != null){
				//update openID
				user.setOpenId(openId);
				user.setUnionid(unionid);
				userService.updateByPrimaryKeySelective(user);
			}
			//join company
			companyService.joinCompanyMember(phone,user.getId(),companyId,reUserId);
			model.put("userName", user.getName());
			return "mobile/join-company-result";
		}catch(Exception e){
			e.printStackTrace();
			String msg = e.getMessage();
			if(!(e instanceof IllegalStateException)&&
        			!(e instanceof IllegalArgumentException)){
        		msg = "服务器异常，请稍后重试";
        	}
			model.put("error",msg);
			return "mobile/join-company";
		}
	}
	
	/**
	 * 微信重定向 绑定openid
	 * @author:zhugw
	 * @time:2017年8月10日 下午3:45:54
	 * @param code
	 * @param state
	 * @param token
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/mp/join-p/{token}",method = RequestMethod.GET)
	public String weixinJoinMember(String code,String state,@PathVariable String token,
			ModelMap model,HttpServletRequest request){
		try{
			String str = AES.decrypt(token);
			if(str == null){
				return "mobile/error";
			}
			if(!StringUtils.isNotEmpty(code)){
				return "mobile/error";
			}
			String[] datas = str.split("_");
			Integer projectId = Integer.valueOf(datas[0]);
			String phone = datas[1];
			Integer reUserId =  Integer.valueOf(datas[2]);
			Integer type = Integer.valueOf(datas[3]);
			Integer serviceTypeId = Integer.valueOf(datas[4]);
			User user = userService.getUserByPhone(phone);
			User reUser = userService.selectByPrimaryKey(reUserId);
			Project project = projectService.selectByPrimaryKey(projectId);
			model.put("projectName", project.getName());
			model.put("type", type);
			model.put("projectId", projectId);
			model.put("serviceTypeId", serviceTypeId);
			model.put("reUserId", reUserId);
			Company company = companyService.selectByPrimaryKey(reUser.getCompanyId());
			model.put("companyName", company.getName());
			
			WxToken wxToken = WxUtil.getWxToken(code);
			WxUser wxUser = WxUtil.getWxUser(wxToken);
			WebUtils.setSessionAttribute(request, "TEMPUSER", wxUser);
			model.put("userName", wxUser.getNickName());
			model.put("openId", wxUser.getOpenId());
			model.put("unionid", wxUser.getUnionid());
			model.put("reUserName", reUser.getName());
			model.put("phone", reUser.getPhone());
			logger.debug("userName:"+wxUser.getNickName()+"-------------------------");
			logger.debug("openId:"+wxUser.getOpenId()+"-------------------------");
			logger.debug("Unionid : "+wxUser.getUnionid() +"-------------------------");
			logger.debug("reUserName:"+ reUser.getName()+"-------------------------");
			logger.debug("phone:"+ reUser.getPhone()+"-------------------------");
			logger.debug("projectName:"+ project.getName()+"-------------------------");
			User userVo = userService.getUserByUnionid(wxUser.getUnionid());
			if(reUser != null && reUser.getCompanyId().equals(0)){
				model.put("error", "邀请人退出团队，暂时不能加入。");
				return "mobile/join-project";
			}
			//绑定过
			if(userVo != null && !userVo.getCompanyId().equals(reUser.getCompanyId())){
				model.put("error", "您已经加入团队了!");
				//返回成功
				return "mobile/join-project";
			}
			if(user == null){
				//注册用户
				return "mobile/join-project";
			}else{
				//用户已加入别团队
				if(!user.getCompanyId().equals(0) && !user.getCompanyId().equals(reUser.getCompanyId())){
					company = companyService.selectByPrimaryKey(user.getCompanyId());
					if(company != null){
						model.put("error", "您已经加入'"+company.getName()+"'，请进入咚咚APP进行管理。");					
					}else{
						model.put("error", "您已经加入团队了，请进入咚咚APP进行管理。");
					}
					return "mobile/join-project";
				}
				//保存openId
				user.setOpenId(wxUser.getOpenId());
				user.setUnionid(wxUser.getUnionid());
				userService.updateByPrimaryKeySelective(user);
				//join project
				projectService.joinProjectMember(type,user.getId(),projectId,serviceTypeId,reUserId);
				//返回成功
				return "mobile/join-project-result";
			}
		}catch(Exception e){
			e.printStackTrace();
			String msg = e.getMessage();
			if(!(e instanceof IllegalStateException)&&
        			!(e instanceof IllegalArgumentException)){
        		msg = "serviceError_服务器异常，请稍后重试";
        	}
			model.put("error",msg);
			return "mobile/join-project";
		}
	}
	
	/**
	 * 邀请项目成员
	 * @author:zhugw
	 * @time:2017年8月8日 下午4:19:20
	 * @param token
	 * @param model
	 * @return
	 * 
	 * 用户有团队
	 * 团队相同则加入项目并返回成功
	 * 团队不同则报错
	 * 用户未注册返回注册页面 
	 */
	@RequestMapping(value = "/join-pm/{token}")
	public String joinProjectMember(@PathVariable String token,ModelMap model,HttpServletRequest request) {
		try{
			String str = AES.decrypt(token);
			if(str == null){
				logger.info("error token: "+token);
				return "mobile/error";
			}
			String[] datas = str.split("_");
			Integer projectId = Integer.valueOf(datas[0]);
			String phone = datas[1];
			Integer reUserId =  Integer.valueOf(datas[2]);
			Integer type = Integer.valueOf(datas[3]);
			Integer serviceTypeId = Integer.valueOf(datas[4]);
			User user = userService.getUserByPhone(phone);
			User reUser = userService.selectByPrimaryKey(reUserId);
			Project project = projectService.selectByPrimaryKey(projectId);
			model.put("projectName", project.getName());
			model.put("type", type);
			model.put("projectId", projectId);
			model.put("serviceTypeId", serviceTypeId);
			model.put("reUserId", reUserId);
			model.put("token", token);
			model.put("reUserName", reUser.getName());
			model.put("phone", reUser.getPhone());
			
			String loc = ServletRequestUtils.getStringParameter(request, "loc", "");
			if(WxUtil.isWeixinBrowser(request)){
				String url =  WxUtil.goUserAuthorizeURL(URL.SERVE_NAME+request.getContextPath()+"/mp/join-p/"+token,loc);
				return url;
			}
			if(reUser != null && reUser.getCompanyId().equals(0)){
				model.put("error", "邀请人退出团队，暂时不能加入。");
				return "mobile/join-project";
			}
			Company company = companyService.selectByPrimaryKey(reUser.getCompanyId());
			model.put("companyName", company.getName());
			if(user == null){
				//注册用户
				return "mobile/join-project";
			}else{
				model.put("userName", user.getName());
				//用户已加入别团队
				if(!user.getCompanyId().equals(0) && !user.getCompanyId().equals(reUser.getCompanyId())){
					company = companyService.selectByPrimaryKey(user.getCompanyId());
					if(company != null){
						model.put("error", "您已经加入'"+company.getName()+"'，请进入咚咚APP进行管理。");					
					}else{
						model.put("error", "您已经加入团队了，请进入咚咚APP进行管理。");
					}
					return "mobile/join-project";
				}
				//join project
				projectService.joinProjectMember(type,user.getId(),projectId,serviceTypeId,reUserId);
				//返回成功
				return "mobile/join-project-result";
			}
		}catch(Exception e){
			String msg = e.getMessage();
			if(!(e instanceof IllegalStateException)&&
        			!(e instanceof IllegalArgumentException)){
        		msg = "serviceError_服务器异常，请稍后重试";
        	}
			model.put("error",msg);
			return "mobile/join-project";
		}
	}
	
	/**
	 * 加入项目（web邀请成员）
	 * @author:zhugw
	 * @time:2017年8月9日 上午9:31:11
	 * @param phone
	 * @param userName
	 * @param code
	 * @param password
	 * @param reUserId
	 * @param projectId
	 * @param type
	 * @param serviceTypeId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/join-project")
	public String joinProject(String token,String phone,String userName,String code,String password,Integer reUserId,
			InviteParams params,ModelMap model){
		try{
			User reUser = userService.selectByPrimaryKey(reUserId);
			model.put("userName", userName);
			model.put("reUserName", reUser.getName());
			model.put("phone", reUser.getPhone());
			model.put("openId", params.getOpenId());
			model.put("unionid", params.getUnionid());
			model.put("reUserId", reUserId);
			model.put("projectId", params.getProjectId());
			model.put("type", params.getType());
			model.put("serviceTypeId", params.getServiceTypeId());
			Assert.state(StringUtils.isNotBlank(code), Constant.EMPTY_CODE);
			Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
			Assert.state(StringUtils.isNotBlank(userName), Constant.EMPTY_NAME);
			Assert.state(StringUtils.isNotBlank(password), Constant.EMPTY_PASSWORD);
			Assert.state(Constant.WANNENG_CODE.equals(code.toUpperCase())
							|| codeService.validateCode(phone, code),Constant.CODE_ERROR);
			User user = userService.selectByPhone(phone);
			if(user == null){
				//register user
				password = MD5.parseMD5(password);
				user = userService.newUser(phone, userName,password);				
			}else if(!user.getCompanyId().equals(0) && !user.getCompanyId().equals(reUser.getCompanyId())){
				Assert.state(false,"该手机号已注册过，并已加入其它团队！");
			}
			// push openId
			if(StringUtils.isNotEmpty(params.getOpenId())){
				User userVo = userService.getUserByUnionid(params.getUnionid());
				if(userVo == null){
					user.setOpenId(params.getOpenId());
					user.setUnionid(params.getUnionid());
					userService.updateByPrimaryKeySelective(user);
				}
			}
			//join project
			projectService.joinProjectMember(params.getType(),user.getId(),params.getProjectId(),params.getServiceTypeId(),reUserId);
			Company company = companyService.selectByPrimaryKey(reUser.getCompanyId());
			Project project = projectService.selectByPrimaryKey(params.getProjectId());
			model.put("companyName", company.getName());
			model.put("projectName", project.getName());
			model.put("userName", user.getName());
			return "mobile/join-project-result";
		}catch(Exception e){
			e.printStackTrace();
			String msg = e.getMessage();
			if(!(e instanceof IllegalStateException)&&
        			!(e instanceof IllegalArgumentException)){
        		msg = "服务器异常，请稍后重试";
        	}
			model.put("error",msg);
			return "mobile/join-project";
		}
	}
	
	/**
	 * 获取邀请url
	 * @author:zhugw
	 * @time:2017年8月9日 下午4:21:49
	 * @param params
	 * @return
	 */
	@RequestMapping(value="getinvite-path")
	@ResponseBody
	public String getJoinPath(InviteParams params){
		//invite company member
		Assert.state(params.getType() != null, Constant.EMPTY_CODE);
		Assert.state(params.getUserId() != null, Constant.EMPTY_PHONE);
		String inviteToken = params.getCompanyId()+"_0_"+params.getUserId();
		inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
		String webPath = "http://"+Constant.hostSite+"/dweibao/join-cm/"+inviteToken;
		if(params.getType().equals(1)){
			//invite project member 
			Integer referId = 0;
			if(params.getUserType().equals(Constant.APP_TYPE.B)){
				referId = params.getServiceTypeId();
			}
			inviteToken = params.getProjectId()+"_0_"+params.getUserId()+"_"+params.getUserType()+"_"+referId;
			inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
			webPath = "http://"+Constant.hostSite+"/dweibao/join-pm/"+inviteToken;
		}
		return Utils.toSuccessJsonRes(webPath);
	}
	
}
