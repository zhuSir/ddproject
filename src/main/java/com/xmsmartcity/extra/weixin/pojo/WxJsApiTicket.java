package com.xmsmartcity.extra.weixin.pojo;

import java.util.Date;

import net.sf.json.JSONObject;

public class WxJsApiTicket {
	private String ticket; // 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	private Long expiresIn; // access_token接口调用凭证超时时间，单位（秒）
	private String errmsg; // 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	private Integer errcode; // 用户授权的作用域，使用逗号（,）分隔
	private Date loadDate; // 第一次获取到token的时间。
	
	public WxJsApiTicket() {
		super();
	}
	
	public void change(JSONObject jsonToken) {
		this.ticket = jsonToken.getString("ticket");
		this.expiresIn = Long.parseLong(jsonToken.getString("expires_in"));
		this.loadDate = new Date();
		errmsg = jsonToken.getString("errmsg");
		errcode = jsonToken.getInt("errcode");
	}
	
	
	
	
	
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public Date getLoadDate() {
		return loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}

	public Boolean isOverdue() {
		Long currDate = new Date().getTime();
		Long overdueDate = loadDate.getTime() + expiresIn * 1000;
		return currDate - overdueDate >= 0;
	}


}
