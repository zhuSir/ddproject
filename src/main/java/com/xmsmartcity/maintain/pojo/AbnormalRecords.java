package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class AbnormalRecords implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2136850110941592093L;
	
    private Integer id;

    private Integer referId;

    private Integer type;

    private Integer faultState;

    private Integer overTime;

    private Date createtime;

    private Integer createUserId;

    private Date updatetime;

    private Integer updateUserId;

    private String remark;

    private Integer isDel;
    
    public AbnormalRecords(){}
    
    public AbnormalRecords(Integer referId,Integer type,Integer faultState,Integer overTime,Date createTime,Integer createUserId,
    		Integer updateUserId,String remark){
    	this.referId = referId;
    	this.type = type;
    	this.faultState=faultState;
    	this.overTime=overTime;
    	this.createtime = createTime;
    	this.createUserId = createUserId;
    	this.updateUserId = updateUserId;
    	this.remark = remark;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReferId() {
        return referId;
    }

    public void setReferId(Integer referId) {
        this.referId = referId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFaultState() {
        return faultState;
    }

    public void setFaultState(Integer faultState) {
        this.faultState = faultState;
    }

    public Integer getOverTime() {
        return overTime;
    }

    public void setOverTime(Integer overTime) {
        this.overTime = overTime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}