package com.xmsmartcity.maintain.pojo;

import java.util.Date;
import java.util.List;

public class PatrolSchemeModel {
    private Integer id;

    private String name;

    private Date checkTime;

    private Integer period;

    private Integer patrolAbnormal;

    private Boolean isOpen;

    private Integer industry;

    private Date createtime;

    private Integer createUserId;

    private Date updatetime;

    private Integer updateUserId;

    private String remark;

    private Integer isDel;
    
    private List<PatrolSchemeModelItem> patrolSchemeModelItems;
    
    private String periodName;
    
    private String patrolAbnormalName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getPatrolAbnormal() {
        return patrolAbnormal;
    }

    public void setPatrolAbnormal(Integer patrolAbnormal) {
        this.patrolAbnormal = patrolAbnormal;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getIndustry() {
        return industry;
    }

    public void setIndustry(Integer industry) {
        this.industry = industry;
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

	public List<PatrolSchemeModelItem> getPatrolSchemeModelItems() {
		return patrolSchemeModelItems;
	}

	public void setPatrolSchemeModelItems(List<PatrolSchemeModelItem> patrolSchemeModelItems) {
		this.patrolSchemeModelItems = patrolSchemeModelItems;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	public String getPatrolAbnormalName() {
		return patrolAbnormalName;
	}

	public void setPatrolAbnormalName(String patrolAbnormalName) {
		this.patrolAbnormalName = patrolAbnormalName;
	}
    
}