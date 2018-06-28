package com.xmsmartcity.extra.weixin.pojo;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import com.xmsmartcity.extra.weixin.WxConstant.SYS;
import com.xmsmartcity.util.MD5;

//import com.zhishan.extra.weixin.WxConstant.SYS;
//import com.zhishan.util.MD5;

public class WxJsConfig {
	
	private String appId;
	private String timestamp;
	private String nonceStr;
	private String signature;
	private String jsApiList;
	
	public WxJsConfig(String url){
		this.appId = SYS.APPID;
		this.timestamp = new Date().getTime() + "";
		this.nonceStr = genNonceStr();
		this.signature = genSign(this.nonceStr,this.timestamp, SYS.JS_API_TICKET.getTicket(), url);
	}
	
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getJsApiList() {
		return jsApiList;
	}
	public void setJsApiList(String jsApiList) {
		this.jsApiList = jsApiList;
	}
	
	
	/**
	 * 获得加密字符串
	 * 
	 * @author felix  @date 2015-4-14 下午8:45:42
	 * @param nonceStr
	 * @param time
	 * @param ticket
	 * @param url
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String genSign(String nonceStr,String time,String ticket,String url){
		StringBuffer sb = new StringBuffer("jsapi_ticket=")
		.append(ticket)
		.append("&noncestr=").append(nonceStr).append("&timestamp=").append(time).append("&url=").append(url);
		return MD5.sha1(sb.toString());
	}
	
	/**
	 * 获得随机字符串
	 * 
	 * @author felix  @date 2015-4-14 下午8:45:49
	 * @return
	 */
	private String genNonceStr(){
		char[] Lower = {'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m','Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M','0','1','2','3','4','5','6','7','8','9'};
		String nonceStr = "";
		Random rd = new Random();
		for (int i = 0; i < 16; i++) {
			int num = rd.nextInt(Lower.length - 1);
			nonceStr += Lower[num];
		}
		return nonceStr;
	}
	

	
	
	
}
