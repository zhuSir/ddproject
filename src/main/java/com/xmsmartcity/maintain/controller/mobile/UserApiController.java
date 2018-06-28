package com.xmsmartcity.maintain.controller.mobile;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xmsmartcity.extra.weixin.WxUtil;
import com.xmsmartcity.maintain.controller.util.ApiHelper;
import com.xmsmartcity.maintain.controller.util.SendEmailUtils;
import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.pojo.UserToken;
import com.xmsmartcity.maintain.service.RedisCacheService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.maintain.service.UserTokenService;
import com.xmsmartcity.util.AES;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;
import com.xmsmartcity.util.JavaSmsApi;
import com.xmsmartcity.util.MD5;
import com.xmsmartcity.util.Utils;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/api/user")
public class UserApiController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private ApiHelper helper;
	
	@Autowired
	private RedisCacheService redisCache;
	
	@Autowired
	private UserTokenService userTokenService;
	
	private static Logger logger = Logger.getLogger(UserApiController.class);
	
	/**
	 * 注册接口
	 * @param response
	 * @param username
	 * @param password 密码需要用MD5加密
	 * @param code
	 * @param phone
	 * @throws IOException 
	 */
	@RequestMapping(value="/register")
	@ResponseBody
	public String register(String username,String password,String code,String phone){
		Assert.state(StringUtils.isNotBlank(code), Constant.EMPTY_CODE);
		Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(username), Constant.EMPTY_NAME);
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = phone.matches(phone_regex);
		Assert.state(isTrue,"手机号错误，请重新输入。");
		Assert.state(StringUtils.isNotBlank(password),
				Constant.EMPTY_PASSWORD);
		if (!Constant.WANNENG_CODE.equals(code.toUpperCase())) {
			Assert.state(redisCache.getObject(phone)!=null, "验证码已过期");
			String redisCode= redisCache.getObject(phone).toString();
			Assert.state(redisCode.equals(code), Constant.CODE_ERROR);
		}
		service.newUser(phone, username,password);
		return Utils.toSuccessJson("注册成功");
	}
	
	/**
	 * 获取验证码
	 * @param phone
	 * @param type 0注册 1忘记密码/修改密码 (0注册 1登录  2忘记密码  3修改手机号 4修改密码)
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/code")
	@ResponseBody
	public String code(String phone,Integer type) throws IOException{
		Assert.isTrue(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.state(null != type, Constant.PARAMS_ERROR);
		// 0注册 1 忘记密码/修改密码
		if (type == 0) {
			Assert.state(!service.checkExist(phone), Constant.USER_EXIST);
		} else {
			Assert.state(service.checkExist(phone), Constant.USER_NO_EXIST);
		}
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = phone.matches(phone_regex);
		Assert.state(isTrue,"发送验证码失败,请确定电话号码是否正确!");
		//判断手机号是否已经发送过信息
		if (redisCache.exists(phone)){
			Assert.state(false,"不能在60秒内重复发送");
		}
		// 生成验证码并发送
		Integer randomCode = new Random().nextInt(999999);
		String randomCodeStr = StringUtils.leftPad(randomCode.toString(), 6, "0");
		// 验证redis缓存里是否有该手机号的验证码
		redisCache.putObject(phone, randomCodeStr, 60);
		String shortContent = "#code#=" + randomCodeStr;
		String responseBody = JavaSmsApi.tplSendSms(Constant.apikey, Constant.tpl_id, shortContent, phone);
		JSONObject obj = JSONObject.fromObject(responseBody);
		if(!"0".equals(obj.get("code").toString())){
			logger.error("send message error: "+responseBody);
			Assert.state(false,"发送验证码失败,请确定电话号码是否正确!");
		}
		logger.debug("responseBody:"+responseBody);
		logger.debug("code is "+shortContent);
		return Utils.toSuccessJson("发送成功。");
	}
	
	/**
	 * 获取验证码
	 * @param phone
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/xcx/code")
	@ResponseBody
	public String xcxCode(String phone) throws IOException{
		Assert.isTrue(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = phone.matches(phone_regex);
		Assert.state(isTrue,"发送验证码失败,请确定电话号码是否正确!");
		//判断手机号是否已经发送过信息
		if (redisCache.exists(phone)){
			Assert.state(false,"不能在60秒内重复发送");
		}
		// 生成验证码并发送
		Integer randomCode = new Random().nextInt(999999);
		String randomCodeStr = StringUtils.leftPad(randomCode.toString(),6, "0");
		//验证存入redis缓存
		redisCache.putObject(phone, randomCodeStr,60);
		String shortContent = "#code#=" + randomCodeStr;
		String responseBody = JavaSmsApi.tplSendSms(Constant.apikey, Constant.tpl_id,shortContent, phone);
		JSONObject obj = JSONObject.fromObject(responseBody);
		if(!"0".equals(obj.get("code").toString())){
			logger.error("send message error: "+responseBody);
			Assert.state(false,"发送验证码失败,请确定电话号码是否正确!");
		}
		logger.debug("responseBody:"+responseBody);
		logger.debug("code is "+shortContent);
		return Utils.toSuccessJson("发送成功");
	}
	
	/**
	 * 登录接口
	 * @param username
	 * @param password 密码需要经过MD5加密
	 * @param puthToken
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/login")
	@ResponseBody
	public String loginByPsw(String phone,String password,String pushToken,HttpServletRequest request){
		Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(password),Constant.EMPTY_PASSWORD);
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = phone.matches(phone_regex);
		Assert.state(isTrue,"手机号错误,请重新输入。");
		User user = service.login(phone, password, pushToken,request);
		return Utils.toSuccessJsonResults(user);
	}
	
	/**
	 * 登录接口
	 * @param username
	 * @param password 密码需要经过MD5加密
	 * @param puthToken
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/login_code")
	@ResponseBody
	public String loginByCode(String phone,String code,String pushToken,HttpServletRequest request){
		Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(code), Constant.EMPTY_CODE);
		// Assert.state(StringUtils.isNotBlank(pushToken),Constant.PARAMS_ERROR);
		if (!Constant.WANNENG_CODE.equals(code.toUpperCase())) {
			Assert.state(redisCache.getObject(phone)!=null, "验证码已过期");
			String redisCode= redisCache.getObject(phone).toString();
			Assert.state(redisCode.equals(code), Constant.CODE_ERROR);
		}
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = phone.matches(phone_regex);
		Assert.state(isTrue,"手机号错误，请重新输入。");
		User user = service.loginByCode(phone,pushToken,request);
		return Utils.toSuccessJsonResults(user);
	}
	
	/**
	 * 忘记密码
	 * 
	 * @param phone
	 *            手机号
	 * @param password
	 *            新密码
	 * @param code
	 *            验证码
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/forget-password")
	@ResponseBody
	public String forgetPassword(String phone, String password, String code){
		Assert.state(StringUtils.isNotBlank(code), Constant.EMPTY_CODE);
		Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(password),Constant.EMPTY_PASSWORD);
		if (!Constant.WANNENG_CODE.equals(code.toUpperCase())) {
			Assert.state(redisCache.getObject(phone)!=null, "验证码已过期");
			String redisCode= redisCache.getObject(phone).toString();
			Assert.state(redisCode.equals(code), Constant.CODE_ERROR);
		}
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = phone.matches(phone_regex);
		Assert.state(isTrue,"手机号错误，请重新输入。");
		User user = service.getUserByPhone(phone);
		Assert.notNull(user,Constant.USER_NO_EXIST);
		user.setPassword(password);
		if (service.modifyPassword(user) == 0) {
			return Utils.toFailJson(Constant.SERVICE_ERROR,null);
		}
		return Utils.toSuccessJson("修改成功");
	}
	
	/**
	 * 修改密码
	 * @param userId
	 * @param token
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "/modify-password")
	@ResponseBody
	public String modifyPassword(Integer userId, String token,String oldPassword, String newPassword){
		Assert.state(StringUtils.isNotBlank(newPassword),Constant.EMPTY_PASSWORD);
		User user = service.getUser(userId);
		Assert.notNull(user,Constant.PARAMS_ERROR);
		Assert.state(user.getPassword().equals(oldPassword),Constant.PASSWORD_ERROR);//验证旧密码
		user.setPassword(newPassword);
		Assert.state(service.modifyPassword(user)!=0,Constant.SERVICE_ERROR);//更新密码
		return Utils.toSuccessJson("修改成功");
	}
	
	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/user-info")
	@ResponseBody
	public String getUserInfo(Integer userId, String token){
		User user = service.getUser(userId);
		user.setPassword(null);
		user.setToken(token);
		Map<String,Object> obj = service.getCountUserInfo(userId);
		obj.put("user_info", user);
		return Utils.toSuccessJsonResults(obj);
	}
	
	/**
	 * 获取其他用户信息
	 * 
	 * @param userId
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/other-user-info")
	@ResponseBody
	public String getOtherUserInfo(Integer userId, String token,Integer otherUserId){
		User user = service.getUser(otherUserId);
		if(user != null){
			user.setPassword(null);
			user.setToken(null);
			Map<String,Object> obj = service.getCountUserInfo(otherUserId);
			obj.put("user_info", user);
			return Utils.toSuccessJsonResults(obj);
		}
		return Utils.toFailJson(Constant.PARAMS_ERROR, null);
	}
	
	/**
	 * 登出
	 * @param userId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public String logout(Integer userId, String token,String pushToken){
		helper.validateToken(token);
		service.removeUserTokenByParams(userId, token);
		return Utils.toSuccessJson("退出成功!");
	}
	
	/**
	 * 修改用户信息
	 * @param userId
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/modify-userInfo")
	@ResponseBody
	public String modifyUserInfo(User user){
		int res = service.modifyUserInfo(user);
		Assert.state(res != 0,Constant.SERVICE_ERROR);
		return Utils.toSuccessJson("修改成功");
	}
	
	/**
	 * 修改用户手机号
	 * 
	 * @param userId
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/modify-phone")
	@ResponseBody
	public String modifyUserPhone(String oldCode,String oldPhone,
			String newCode,String newPhone,Integer userId,String token){
		Assert.state(StringUtils.isNotBlank(newCode), Constant.EMPTY_CODE);
		Assert.state(StringUtils.isNotBlank(newPhone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(oldCode), Constant.EMPTY_CODE);
		Assert.state(StringUtils.isNotBlank(oldPhone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(token), Constant.PARAMS_ERROR);
		if (!Constant.WANNENG_CODE.equals(newCode.toUpperCase())) {
			Assert.state(redisCache.getObject(newPhone)!=null, "新手机验证码已过期");
			String redisCode= redisCache.getObject(newPhone).toString();
			Assert.state(redisCode.equals(newCode), "输入的新验证码有误，请重新输入.");
		}
		if (!Constant.WANNENG_CODE.equals(oldCode.toUpperCase())) {
			Assert.state(redisCache.getObject(oldPhone)!=null, "旧手机验证码已过期");
			String redisCode= redisCache.getObject(oldPhone).toString();
			Assert.state(redisCode.equals(oldCode), "输入的旧验证码有误，请重新输入.");
		}
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = newPhone.matches(phone_regex);
		Assert.state(isTrue,"手机号错误，请重新输入。");
		User user = new User();
		user.setPhone(newPhone);
		user.setId(userId);
		int res = service.modifyUserInfo(user);//修改用户信息
		Assert.state(res != 0,Constant.SERVICE_ERROR);
		return Utils.toSuccessJson("修改成功");
	}
	
	/**
	 * 发送绑定邮箱的邮件
	 * @param mail
	 * @return
	 */
	@RequestMapping(value="/send-mail")
	@ResponseBody
	public String sendMail(String mail,Integer userId,String token,HttpServletRequest request){
		Assert.state(StringUtils.isNotBlank(mail),Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(token), Constant.PARAMS_ERROR);
		String email_regex = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";
		Assert.state(mail.matches(email_regex),"邮箱格式错误，请重新输入");
		service.validateMail(userId,mail);
		User user = service.getUser(userId);
		String oldMail = user.getMail();
		if(StringUtils.isNotBlank(oldMail)){
			String[] temp = oldMail.split("_");
			if(temp != null && temp.length > 0 && temp[0].matches(email_regex)){
				Assert.state(temp[0].indexOf(mail)!=-1,"邮箱还未解绑，请先解绑后进行绑定邮箱");
				long interval_time = System.currentTimeMillis() - (new Long(temp[1]));
				long temp_time = 30*60*1000;
				Assert.state(interval_time > temp_time,"三十分钟内不能重复绑定邮箱，请稍后重试");
			}
		}
		SendEmailUtils sendMail = new SendEmailUtils(mail);
		String mailToken = DateUtils.DateToString(new Date(), DateUtils.formatStr_yyyymmdd);
		mailToken= userId+"_"+mailToken;
		mailToken = AES.encrypt(mailToken);//AES加密后的userId+date
		String contentPath = request.getContextPath();
		//String localHost = request.getLocalAddr();    //取得服务器IP     
		//int localPort = request.getLocalPort();    //取得服务器端口
		String url = "http://"+Constant.hostSite+contentPath+"/api/user/modify-mail?id="+mailToken;
		String subject = "咚咚维保云平台邮箱绑定";
		String content = "请点击如下链接绑定咚咚维保云平台 <br/> <a href='"+url+"'>"+url+"</a>";
		boolean isTrue = sendMail.sendHtmlMail(subject,content);//发送申请邮件
		Assert.state(isTrue,"发送邮件错误，请检查邮箱是否正确。");
		logger.debug("send mail url : "+url);
		mail = mail+"_"+System.currentTimeMillis();
		user.setMail(mail);
		service.modifyUserInfo(user);//保存邮箱
		return Utils.toSuccessJson("发送成功");
	}
	
	/**
	 * 验证邮箱
	 * @param id
	 * @return
	 */
	@RequestMapping(value="modify-mail")
	@ResponseBody
	public String modifyMail(String id){
		logger.debug("mail token : "+id);
		Assert.state(StringUtils.isNotBlank(id),"抱歉您申请绑定邮箱已过期，请重新申请。");
		String deString = AES.decrypt(id);
		String[] res = deString.split("_");
		if(res != null && res.length > 0){
			Assert.state(res[1].equals(
					DateUtils.DateToString(new Date(), DateUtils.formatStr_yyyymmdd)),"抱歉您申请绑定邮箱已过期，请重新申请。");
			Integer userId = new Integer(res[0]);
			User user = service.getUser(userId);
			Assert.notNull(user,"抱歉您申请绑定邮箱已过期，请重新申请。");
			Assert.state(StringUtils.isNotBlank(user.getMail()),"抱歉您申请绑定邮箱已过期，请重新申请。");
			String mail = user.getMail();
			String[] mails = mail.split("_");
			user.setMail(mails[0]);
			service.modifyUserInfo(user);
			return user.getName()+" 您已成功绑定邮箱！";
		}
		return "抱歉您申请绑定邮箱已过期，请重新申请。";
	}
	
	/**
	 * 获取用户当前状态
	 * @author:zhugw
	 * @time:2017年4月19日 上午11:29:01
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/user-state",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String userState(Integer userId,String token){
		DealEvent res = service.selectUserState(userId);
		return Utils.toSuccessJsonResults(res);
	}
	/**
	 * 根据微信openid获取用户
	 * @param phone
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/xcx/getUserByOpenId")
	@ResponseBody
	public String getUserByOpenId(String code,String encryptedData,String iv) throws IOException{
		JSONObject openJson=WxUtil.getUserOpenIdByCode(code);
		User user=new User();
		try {
			//对微信小程序获取用户unionId进行解密
			String result =(!encryptedData.equals("undefined") && !iv.equals("undefined"))? AES.decrypt(encryptedData, openJson.getString("session_key"), iv, "UTF-8"):null;
			if (null != result && result.length() > 0) {
				JSONObject userInfoJSON = JSONObject.fromObject(result);
				if (userInfoJSON.get("unionId")!=null && !userInfoJSON.get("unionId").toString().equals("")) {
					user=service.getUserByUnionid(userInfoJSON.getString("unionId"));
					//查无此用户
					if (user==null) {
						user=new User();
						user.setXcxOpenId(openJson.getString("openid"));
						user.setUnionid(userInfoJSON.getString("unionId"));
					}else if (user.getOpenId()==null || user.getOpenId().equals("")) {
						user.setXcxOpenId(openJson.getString("openid"));
					}
				}
				else{
					//解密成功，证明未关注过公众号及未登录过公众号
					user.setXcxOpenId(openJson.getString("openid"));
					user.setUnionid(userInfoJSON.get("unionId")!=null?userInfoJSON.getString("unionId"):"");
				}		
			}else {
				//解密失败，则先调取是否可以通过openid获取用户
				user=service.getUserByXcxOpenId(openJson.getString("openid"));
				if (user==null) {
					user=new User();
					user.setXcxOpenId(openJson.getString("openid"));
					user.setUnionid("");
				}
			}
		} catch (Exception e) {
			//解密出现报错
			logger.error(e.getMessage());
			user=service.getUserByXcxOpenId(openJson.getString("openid"));
			if (user==null) {
				user=new User();
				user.setUnionid("");
				user.setXcxOpenId(openJson.getString("openid"));					
			}else {
				user.setUnionid("");
				user.setXcxOpenId(openJson.getString("openid"));
			}
		}
		return Utils.toSuccessJsonResults(user);
	}
	
	/**
	 * 根据微信openid注册，已有则绑定微信
	 * @param phone
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/xcx/register")
	@ResponseBody
	public String xcxRegister(String openId,String code,String username,String phone,String unionid) throws IOException{
		//验证是否已注册
		Assert.isTrue(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.isTrue(StringUtils.isNotBlank(username), Constant.EMPTY_NAME);
		Assert.state(StringUtils.isNotBlank(code), Constant.EMPTY_CODE);
		Assert.state(redisCache.getObject(phone)!=null,"验证码已过期，请重新获取验证码");
		Assert.state(redisCache.getObject(phone).toString().equals(code),"验证码有误，请重新输入");
		User user=service.getUserByPhone(phone);
		//未注册则帮其注册并绑定微信小程序
		if (user==null) {
			String password=MD5.parseMD5("123456");
			username=URLDecoder.decode(username,"UTF-8");
			user=service.newUser(phone, username, password);//新建用户
			user.setPassword(password);
			user.setXcxOpenId(openId);
			user.setUnionid(unionid.equals("undefined")?"":unionid);
			service.update(user);//更新xcxOpenId
		}
		//已注册过，未绑定微信则绑定微信小程序
		else if (user!=null && (user.getXcxOpenId()==null || user.getXcxOpenId().equals(""))) {
			user.setXcxOpenId(openId);
			user.setUnionid(unionid.equals("undefined")?"":unionid);
			service.update(user);//更新xcxOpenId
		}
		if (user!=null && (user.getUnionid()==null || user.getUnionid().equals("")) && !unionid.equals("undefined")) {
			user.setUnionid(unionid.equals("undefined")?"":unionid);
			service.update(user);//更新xcxOpenId
		}
		user.setName(username);
		user.setPassword(null);
		//已注册且已绑定微信则不做操作
		return Utils.toSuccessJsonResults(user);
	}
	
	/**   
	 * 登录接口
	 * @param username
	 * @param password 密码需要经过MD5加密
	 * @param puthToken
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/xcx/login")
	@ResponseBody
	public String xcxLoginByCode(String phone,HttpServletRequest request){
		Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		User user = service.selectByPhone(phone);//登录验证手机号
		Assert.state(user!=null, Constant.USER_NO_EXIST);
		UserToken oldPushtoken=userTokenService.getTokenInfo(null, user.getId());
		user = service.loginByCode(phone,(oldPushtoken==null || oldPushtoken.getPushToken()==null)?UUID.randomUUID().toString():oldPushtoken.getPushToken(),request);
		return Utils.toSuccessJsonResults(user);
	}
	
	/**   
	 * 登录接口
	 * @param username
	 * @param password 密码需要经过MD5加密
	 * @param puthToken
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="/xcx/binding")
	@ResponseBody
	public String xcxBinding(String phone,String password,String openid,String unionid){
		Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(password), Constant.EMPTY_PASSWORD);
		Assert.state(StringUtils.isNotBlank(openid), "openid不能为空");
		Assert.state(StringUtils.isNotBlank(unionid), "未通过用户授权");
		password=MD5.parseMD5(password);
		User user =service.getByPhoneAndPsw(phone, password);
		if (user==null) {
			return Utils.toFailJson("用户名或密码错误，请重试", null);
		}
		User unionUser = service.getUserByUnionid(unionid);
		if (unionUser!=null) {
			return Utils.toFailJson("该微信账号已绑定平台账户，请联系管理员解绑", null);
		}
		user.setXcxOpenId(openid);
		user.setUnionid(unionid);
		service.update(user);
		return Utils.toSuccessJsonResults(user);
	}
	
	/**
	 * 获取用户动态
	 * @author:zhugw
	 * @time:2017年9月8日 下午3:44:07
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userDynamic")
	@ResponseBody
	public String getUserDynamic(Integer userId,Integer startIndex,Integer pageSize){
		List<Map<String,Object>> results = service.getUserDynamic(userId,startIndex,pageSize);
		return Utils.toSuccessJson(results);
	}
	
	/**
	 * 更新pushToken
	 * @author:zhugw
	 * @time:2017年9月8日 下午3:44:07
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/update-pushToken")
	@ResponseBody
	public String updatePushToken(Integer userId,String pushToken){
		userTokenService.setPushToken(userId,pushToken);
		return Utils.toSuccessJson("修改成功！");
	}

	@RequestMapping(value = "/pc/login")
	@ResponseBody
	public String loginByPc(String phone, String password, HttpServletRequest request) {
		Assert.state(StringUtils.isNotBlank(phone), Constant.EMPTY_PHONE);
		Assert.state(StringUtils.isNotBlank(password), Constant.EMPTY_PASSWORD);
		// Assert.state(StringUtils.isNotBlank(pushToken),Constant.PARAMS_ERROR);
		String phone_regex = "^1(3[0-9]|4[0-9]|6[0-9]|5[0-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		boolean isTrue = phone.matches(phone_regex);
		Assert.state(isTrue, "手机号错误,请重新输入。");
		User user = service.getByPhoneAndPsw(phone, password);
		Assert.notNull(user, Constant.USER_INFO_ERROR);
		Assert.state(user.getCompanyId() != null && user.getCompanyId() != 0, "请使用公司负责人账号登陆");
		String key = "pc_" + user.getId() + "_print";
		String time = "";
		if (redisCache.getObject(key) != null) {
			time = redisCache.getObject(key).toString();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("time", time);
		return Utils.toSuccessJsonResults(map);
	}
}
