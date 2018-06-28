package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 操作日志表
 * @author Admin
 *
 */
public class OperatorLog implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;//操作日志id
	private String content;//操作内容
	private Integer creatUserId;//操作用户
	private Integer type;//操作类型
	private Integer referId;//操作对象id
	@JSONField(format="yyyy-MM-dd hh:mm:ss")
	private Date createtime;//创建时间
	
	private String adminName;//操作人名
	private String startTime;//开始时间
	private String stopTime;//结束时间
	
	public OperatorLog(){}
	//重构方法
	public OperatorLog(String content,Integer type,Integer creatUserId,Date createtime){
		super();
		this.content=content;
		this.type=type;
		this.creatUserId=creatUserId;
		this.createtime=createtime;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getCreatUserId() {
		return creatUserId;
	}
	public void setCreatUserId(Integer creatUserId) {
		this.creatUserId = creatUserId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getReferId() {
		return referId;
	}
	public void setReferId(Integer referId) {
		this.referId = referId;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStopTime() {
		return stopTime;
	}
	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}
	
}
