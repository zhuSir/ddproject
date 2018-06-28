package com.xmsmartcity.extra.weixin;

import static com.xmsmartcity.extra.weixin.WxHttp.href;
import static com.xmsmartcity.extra.weixin.WxHttp.replaceURL;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.xmsmartcity.extra.weixin.WxConstant.SYS;
import com.xmsmartcity.extra.weixin.WxConstant.URL;
import com.xmsmartcity.extra.weixin.pojo.WxJsConfig;
import com.xmsmartcity.extra.weixin.pojo.WxToken;
import com.xmsmartcity.extra.weixin.pojo.WxUser;
import com.xmsmartcity.maintain.controller.util.ControllerHelper;


public class WxUtil {
	private static Logger logger = LoggerFactory.getLogger(WxUtil.class);
	/**
	 * 验证是否微信浏览器
	 * @author linweiqin
	 * @createDate 2015-4-8 下午2:30:34
	 * @param request
	 * @return
	 */
	public static boolean isWeixinBrowser(HttpServletRequest request){
		return request.getHeader("user-agent").toLowerCase().indexOf("micromessenger") > 0;
	}
	
	
	/**
	 * 跳转用户同意授权页面(授权获得用户所有信息,会跳出授权页面)
	 */
	public static String goUserAuthorizeURL(String uri,String state){
		System.out.println("uri="+uri);
		return replaceURL(URL.GO_USER_AUTHORIZE,SYS.APPID,uri,state);
	}
	
	
	/**
	 * 跳转用户同意授权页面(获得用户openId,不会跳出授权页面)
	 */
	public static String goUserBaseAuthorizeURL(String uri,String state){
		return replaceURL(URL.GO_USER_BASE_AUTHORIZE,SYS.APPID,uri,state);
	}
	
	
	/**
	 * 获得用户 access_token & openId
	 * 
	 * @author felix  @date 2015-4-14 下午12:18:17
	 * @param code
	 * @return
	 */
	public static WxToken getWxToken(String code) {
		JSONObject jsonToken = href(URL.USER_ACCESS_TOKEN_AND_OPENID,"GET",SYS.APPID,SYS.APP_SECRET,code);
		if(null == jsonToken || StringUtils.isEmpty(jsonToken.getString("access_token"))){
			jsonToken = href(URL.USER_ACCESS_TOKEN_AND_OPENID,"GET",SYS.APPID,SYS.APP_SECRET,code);
			logger.info("error ,then reget token success; json token is :" +jsonToken);
		}
		return new WxToken(jsonToken, false);
	}

	
	
	/**
	 * 刷新用户access_token,使得token有更长的过期时间
	 * 
	 * @author felix  @date 2015-4-14 下午1:07:25
	 * @param refreshToken
	 * @return
	 */
	public static WxToken refressUserAccessToken(String refreshToken) {
		JSONObject jsonToken = href(URL.REFRESH_USER_ACCESS_TOKEN,"GET",SYS.APPID,refreshToken);
		if(null == jsonToken){
			jsonToken = href(URL.REFRESH_USER_ACCESS_TOKEN,"GET",SYS.APPID,refreshToken);
		}
		return new WxToken(jsonToken, false);
	}
	
	
	/**
	 * 获得微信用户所有信息
	 * 
	 * @author felix  @date 2015-4-14 下午1:15:10
	 * @param wxToken
	 * @return
	 */
	public static WxUser getWxUser(WxToken wxToken) {
		JSONObject jsonUser = href(URL.GET_UER_INFO,"GET",wxToken.getAccessToken(),wxToken.getOpenId());
		return new WxUser(jsonUser);
	}
	
	
	/**
	 * 生成微信配置文件，放入model中
	 * 
	 * @author felix  @date 2015-4-16 下午5:37:35
	 * @param request
	 * @param model
	 */
	public static void putWxConfig(HttpServletRequest request, Model model){
		String url = ControllerHelper.getAllPath(request)  + request.getServletPath();      //请求页面或其他地址  
		url += StringUtils.isNotBlank(request.getQueryString()) ? ("?" + (request.getQueryString())) : "";
		System.out.println(url);
		WxJsConfig wxConfig = new WxJsConfig(url);
		model.addAttribute("wxconfig", wxConfig);
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static JSONObject getUserOpenIdByCode(String code){
		JSONObject jsonUser = href(URL.XCX_GET_USER_OPENID,"GET",SYS.XCXAPPID,SYS.XCX_SECRET,code);
		return jsonUser;
	}

	//======================================JS sdk start ========================================================
	//======================================JS sdk end ========================================================
	
}
