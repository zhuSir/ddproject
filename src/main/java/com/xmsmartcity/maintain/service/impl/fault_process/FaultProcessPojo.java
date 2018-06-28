package com.xmsmartcity.maintain.service.impl.fault_process;

import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;

public class FaultProcessPojo {
	
	public FaultProcessPojo(Integer faultInfoId, Integer type,String appointmentTime, String reason,String reUserId,String reUserPhone,String pics,Integer userId){
		this.faultInfoId = faultInfoId;
		this.type = type;
		this.appointmentTime = appointmentTime;
		this.reason = reason;
		this.reUserId = reUserId;
		this.reUserPhone = reUserPhone;
		this.pics = pics;
		this.userId = userId;
	}
	
	private FaultInfoServiceImpl faultInfoImpl;
	private Integer userId;
	private Integer faultInfoId;
	private Integer type;
	private String appointmentTime; 
	private String reason;
	private String reUserId;
	private String reUserPhone;
	private String pics;
	
	public FaultInfoServiceImpl getFaultInfoImpl() {
		return faultInfoImpl;
	}
	public void setFaultInfoImpl(FaultInfoServiceImpl faultInfoImpl) {
		this.faultInfoImpl = faultInfoImpl;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getFaultInfoId() {
		return faultInfoId;
	}
	public void setFaultInfoId(Integer faultInfoId) {
		this.faultInfoId = faultInfoId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReUserId() {
		return reUserId;
	}
	public void setReUserId(String reUserId) {
		this.reUserId = reUserId;
	}
	public String getReUserPhone() {
		return reUserPhone;
	}
	public void setReUserPhone(String reUserPhone) {
		this.reUserPhone = reUserPhone;
	}
	public String getPics() {
		return pics;
	}
	public void setPics(String pics) {
		this.pics = pics;
	}
}
