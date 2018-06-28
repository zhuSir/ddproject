package com.xmsmartcity.maintain.controller.util;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.xmsmartcity.extra.weixin.TemplateEnum;
import com.xmsmartcity.extra.weixin.WxConstant;
import com.xmsmartcity.extra.weixin.WxConstant.SYS;
import com.xmsmartcity.extra.weixin.WxConstant.URL;
import com.xmsmartcity.extra.weixin.WxHttp;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.pojo.UserToken;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.maintain.service.UserTokenService;
import com.xmsmartcity.util.Constant;

@Service
public class ApiHelper {
	@Autowired
	private UserService userService;
	@Autowired
	private UserTokenService userTokenService;

	/**
	 * 接口验证token
	 * @param userId
	 * @param token
	 * @throws IOException
	 * 
	 */
	public void validateToken(Integer userId, String token){
//		Assert.state(null != userId&&userId > 0,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(token),Constant.EMPTY_TOKEN);
//		User user = userService.selectByPrimaryKey(userId);
//		Assert.notNull(user,Constant.PARAMS_ERROR);
		UserToken userToken = userTokenService.getTokenInfoByToken(null,token);
		if(userToken == null){
			Assert.state(false,"isOtherDeviceLogin_您的帐号已在其他设备上登录。");
		}else{
			User user = userService.selectByPrimaryKey(userToken.getUserId());
			if(user != null){
				if(user.getState().equals(1)){
					Assert.state(false,"accountDeprecated_您的账号已被禁用，请联系客服人员。");
				}
			}else{
				Assert.state(false,"isOtherDeviceLogin_您的帐号已在其他设备上登录。");
			}
			
		}
	}
	
	public void validateToken(String token){
		Assert.state(StringUtils.isNotBlank(token),Constant.EMPTY_TOKEN);
		UserToken userToken = userTokenService.getTokenInfoByToken(null,token);
		if(userToken == null){
			Assert.state(false,"isOtherDeviceLogin_您的帐号已在其他设备上登录。");
		}else{
			User user = userService.selectByPrimaryKey(userToken.getUserId());
			if(user != null){
				if(user.getState().equals(1)){
					Assert.state(false,"accountDeprecated_您的账号已被禁用，请联系客服人员。");
				}
			}else{
				Assert.state(false,"isOtherDeviceLogin_您的帐号已在其他设备上登录。");
			}
			
		}
	}
	
	
	/**
	 *向用户发送对应的模板信息
	 * @param content
	 * @param templateId
	 * @param userIdList
	 * @param dynamicId 有值时加上详情链接
	 */
	public void sendConten(TemplateEnum template,List<Integer> userIdList,String ...params){
		List<String> openIds = userService.getUserOpenIdsById(userIdList);
	    String content = WxHttp.replaceContent(template.getName(), params);
		//String jsonText="{\"touser\":\""+openId+"\",\"template_id\":\""+templateId+"\",\"url\":\"http://weixin.qq.com/download\",\"topcolor\":\"#FF0000\",\"data\":{\"first\": {\"value\":\"firstData\",\"color\":\"#173177\"},\"name\": {\"value\":\"您的【XXX合同号】的【某某款式】的【上线节点】过两天就到了，麻烦做好准备谢谢\",\"color\":\"#173177\"},\"expDate\": {\"value\":\"priceData\",\"color\":\"#173177\"},\"remark\": {\"value\":\"remarkData\",\"color\":\"#173177\"}}}";
		String url="http://weixin.qq.com/download";
		
	    for (String openId : openIds) {
	    	try {
	    		String jsonText="{\"touser\":\""+openId+"\",\"template_id\":\""+template.getId()+"\",\"url\":\""+url+openId+"\",\"topcolor\":\"#FF0000\","+content+"}";
	    		System.out.println("openId="+openId+"...token="+WxConstant.SYS.ACCESS_TOKEN.getAccessToken());
				JSONObject jsonObject = WxHttp.hrefPost(WxConstant.URL.SEND_TEMPLATE_URL, "POST",jsonText,WxConstant.SYS.ACCESS_TOKEN.getAccessToken());
				System.out.println(jsonObject.toString());
				
				if(jsonObject.getInt("errcode")!=0&&jsonObject.getString("errmsg").contains("invalid credential, access_token is invalid")){
					System.out.println("token 过期，重试一次，当前token值为"+WxConstant.SYS.ACCESS_TOKEN.getAccessToken());
					JSONObject jsonToken = WxHttp.href(URL.GEN_SYS_ACCESS_TOKEN,"GET",SYS.APPID,SYS.APP_SECRET);
					WxConstant.SYS.ACCESS_TOKEN.changeSysToken(jsonToken);
					System.out.println("重试一次后token值为"+WxConstant.SYS.ACCESS_TOKEN.getAccessToken());
					WxHttp.hrefPost(WxConstant.URL.SEND_TEMPLATE_URL, "POST",jsonText,WxConstant.SYS.ACCESS_TOKEN.getAccessToken());
				}	
			} catch (Exception e) {
				e.printStackTrace();
			      System.out.println(e.getMessage());
			}
			
			
		}
	}

}
