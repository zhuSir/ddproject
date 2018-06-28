package com.xmsmartcity.extra.weixin;

import java.util.HashMap;
import java.util.Map;

import com.xmsmartcity.extra.weixin.pojo.WxJsApiTicket;
import com.xmsmartcity.extra.weixin.pojo.WxToken;
import com.xmsmartcity.util.PropertiesHelper;

//import com.xmsmartcity.extra.weixin.pojo.WxJsApiTicket;
//import com.xmsmartcity.extra.weixin.pojo.WxToken;

public class WxConstant {
	
	public static PropertiesHelper helper = new PropertiesHelper("system.properties");
	public static boolean Debug = helper.getBoolean("wx_debug");
	
	public  interface SYS {
		
		public static String APPID = Debug ? "wxe9e7dab38ac0ea6b" : "wx9c8cf27dea4ec8e5";
		
		public static String APP_SECRET =  Debug ? "bf298c1d18b4f2fb2559e4f34cd3a8f7" : "c324bfcfb6ced20943c8c9b08763c0ff";
		
		public static WxToken ACCESS_TOKEN = new WxToken();
		
		public static String CALLBACK_TOKEN =  Debug ? "sswwwedfa33331aer51ss" : "tgdfg84fp12usd1f5c6keccx";
		
		public static WxJsApiTicket JS_API_TICKET  = new WxJsApiTicket();
		
		public static String XCXAPPID="wxe9df969cfbe1e49c"; //微信小程序APPID
		
		public static String XCX_SECRET = "08b8e463c4d09141dc08c5a0374b0a69";//微信小程序SECRET
	}
	
	
	
	public interface PAY{
		
		public static String SUCCESS_STR = "SUCCESS";
		
        public static String MCH_ID = Debug ? "1233604902" : "1235533102";
		
		public static String API_KEY =  Debug ? "zhishanshizhe8888888888888888888" : "qihongwenhuaximeijiedianshangpt8";
		
		public static String API_URL = Debug ? "https://api.mch.weixin.qq.com/pay/unifiedorder" : "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		public static String NOTIFY_URL = Debug? "http://xmj.daoyoumm.com/wxpay/pay-notify" : "http://xmj.zhishangsoft.com/wxpay/pay-notify";
		
	}
	
	public static Map<String,WxToken> wxTokens = new HashMap<String, WxToken>();
	
	
	
	public interface URL{
		
		//获得系统的ACCESS_TOKEN
		public static String GEN_SYS_ACCESS_TOKEN ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}";
		
		//用户授权,授权（获得openID,和用户信息,跳出授权页面）
		public static String GO_USER_AUTHORIZE = "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid={APPID}&redirect_uri={REDIRECT_URI}&response_type=code&scope=snsapi_userinfo&state={STATE}#wechat_redirect";
		
		//用户授权,授权（只获得openID,不跳出授权页面）
		public static String GO_USER_BASE_AUTHORIZE = "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid={APPID}&redirect_uri={REDIRECT_URI}&response_type=code&scope=snsapi_base&state={STATE}#wechat_redirect";
			
		//使用CODE获得用户的access_token和openId
		public static String USER_ACCESS_TOKEN_AND_OPENID ="https://api.weixin.qq.com/sns/oauth2/access_token?appid={appId}&secret={appSecret}&code={code}&grant_type=authorization_code";
		
		//延长用户的access_token
		public static String REFRESH_USER_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={appId}&grant_type=refresh_token&refresh_token={refreshToken}";
				
		//获得微信用户所有信息
		public static String GET_UER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={accessToken}&openid={openId}&lang=zh_CN";
		//获得微信JS的ticket
		public static String GET_JS_API_TICKET   = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={accesstoken}&type=jsapi";
		
		public static String SEND_TEMPLATE_URL  = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={accesstoken}";
		//域名
		public static String SERVE_NAME = Debug ? "http://maintainst.dweibao.com":"http://maintain.dweibao.com";//"http://maintain.test.dweibao.com";
		//微信小程序根据用户登陆CODE获取用户OPENID
		public static String XCX_GET_USER_OPENID="https://api.weixin.qq.com/sns/jscode2session?appid={APPID}&secret={SECRET}&js_code={JSCODE}&grant_type=authorization_code";
		
	}
	
	public interface tempalte{
		public static String content1="\"data\":{\"first\": {\"value\":\"{firstData}\",\"color\":\"#173177\"},\"keyword1\": {\"value\":\"{keyword1Data}\",\"color\":\"#173177\"},\"keyword2\": {\"value\":\"{keyword2Data}\",\"color\":\"#173177\"},\"keyword3\": {\"value\":\"{keyword3Data}\",\"color\":\"#173177\"},\"keyword4\": {\"value\":\"{keyword4Data}\",\"color\":\"#173177\"},\"remark\": {\"value\":\"{remarkData}\",\"color\":\"#173177\"}}";
	}
	
	public interface TEMPLATE_ID{
		public static String templateId1="";//微信对应的模板id
		public static String templateId2 = "";
	}


}
