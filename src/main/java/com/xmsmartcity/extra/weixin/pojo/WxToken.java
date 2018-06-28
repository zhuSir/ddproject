package com.xmsmartcity.extra.weixin.pojo;

import java.util.Date;

import net.sf.json.JSONObject;

/**
 * 微信token数据类
 * 
 * @author linweiqin
 * @Date 2014-11-14 下午1:47:48
 */
public class WxToken {
	
	public WxToken() {
		super();
	}

	public WxToken(JSONObject jsonToken,boolean isSysToken) {
		accessToken = jsonToken.getString("access_token");
		expiresIn = Long.parseLong(jsonToken.getString("expires_in"));
		loadDate = new Date();
		if(isSysToken){
			return;
		}
		openId = jsonToken.getString("openid");
		scope = jsonToken.getString("scope");
		refreshToken = jsonToken.getString("refresh_token");
	}
	
	
	
	public void changeSysToken(JSONObject jsonToken) {
		this.accessToken = jsonToken.getString("access_token");
		this.expiresIn = Long.parseLong(jsonToken.getString("expires_in"));
		this.loadDate = new Date();
	}

	
	
	
	
	private String accessToken; // 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	private Long expiresIn; // access_token接口调用凭证超时时间，单位（秒）
	private String refreshToken; // 用户刷新access_token
	private String openId; // 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	private String scope; // 用户授权的作用域，使用逗号（,）分隔
	private Date loadDate; // 获取到token的时间。

	public Boolean isOverdue() {
		Long currDate = new Date().getTime();
		Long overdueDate = loadDate.getTime() + expiresIn * 1000;
		return currDate - overdueDate >= 0;
	}

	public Date getLoadDate() {
		return loadDate;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	public String getOpenId() {
		return openId;
	}

	public String getScope() {
		return scope;
	}
}
