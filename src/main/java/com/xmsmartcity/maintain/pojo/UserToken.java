package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class UserToken implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserToken(){}
    
    public UserToken(Integer userId, String pushToken, String sysToken,Integer type) {
    	super();
    	this.userId = userId;
    	this.pushToken = pushToken;
    	this.sysToken = sysToken;
    	this.type = type;
	}
	
    private Integer id;

    private Integer userId;

    private String sysToken;

    private String pushToken;

    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSysToken() {
        return sysToken;
    }

    public void setSysToken(String sysToken) {
        this.sysToken = sysToken == null ? null : sysToken.trim();
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken == null ? null : pushToken.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}