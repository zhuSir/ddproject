package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;
@Entity
public class Evaluate implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer faultInfoId;

    private Integer resSpeed;

    private Integer maintainQuality;

    private String remark;

    private Integer userId;

    private Date createtime;

    private Date updatetime;

    private Integer isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFaultInfoId() {
        return faultInfoId;
    }

    public void setFaultInfoId(Integer faultInfoId) {
        this.faultInfoId = faultInfoId;
    }

    public Integer getResSpeed() {
        return resSpeed;
    }

    public void setResSpeed(Integer resSpeed) {
        this.resSpeed = resSpeed;
    }

    public Integer getMaintainQuality() {
        return maintainQuality;
    }

    public void setMaintainQuality(Integer maintainQuality) {
        this.maintainQuality = maintainQuality;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}