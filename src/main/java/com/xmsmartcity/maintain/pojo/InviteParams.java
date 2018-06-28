package com.xmsmartcity.maintain.pojo;

import com.xmsmartcity.maintain.common.Entity;

/*
 * 获取邀请url接口传参对象
 */
@Entity
public class InviteParams{
	private Integer companyId; 
	private Integer userId; 
	private Integer projectId; 
	private Integer serviceTypeId; 
	private Integer userType; //甲乙方
	private Integer type;   //类型  1 项目  0 团队
	private String openId;
	private String unionid;
	
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Integer getServiceTypeId() {
		return serviceTypeId;
	}
	public void setServiceTypeId(Integer serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}


