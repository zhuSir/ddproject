package com.xmsmartcity.maintain.pojo;

import java.util.Date;
import java.util.List;

public class PatrolSchemeRecord {
    private Integer id;

    private Integer patrolSchemeId;

    private Date patrolTime;

    private String position;

    private String lng;

    private String lat;

    private Integer state;

    private String summarize;
    
    private Integer isNormal;

    private String pics;

    private Date createtime;

    private Integer createUserId;

    private Date updatetime;

    private Integer updateUserId;

    private String remark;

    private Integer isDel;

    private String patrolSchemeName;
    
    private String equipId;
    
    private String equipName;
    
    private Integer patrolUserId;

    private String patrolUserName;

    private String patrolUserMobile;
    
    private Date checkTime;
    
    private List<PatrolSchemeRecordItem> patrolSchemeRecordItems;
    
    private String periodName;
    
    private String patrolAbnormalName;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatrolSchemeId() {
        return patrolSchemeId;
    }

    public void setPatrolSchemeId(Integer patrolSchemeId) {
        this.patrolSchemeId = patrolSchemeId;
    }

    public Date getPatrolTime() {
        return patrolTime;
    }

    public void setPatrolTime(Date patrolTime) {
        this.patrolTime = patrolTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? null : lng.trim();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getSummarize() {
        return summarize;
    }

    public void setSummarize(String summarize) {
        this.summarize = summarize == null ? null : summarize.trim();
    }

    public Integer getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(Integer isNormal) {
		this.isNormal = isNormal;
	}

	public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
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

	public String getPatrolSchemeName() {
		return patrolSchemeName;
	}

	public void setPatrolSchemeName(String patrolSchemeName) {
		this.patrolSchemeName = patrolSchemeName;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public List<PatrolSchemeRecordItem> getPatrolSchemeRecordItems() {
		return patrolSchemeRecordItems;
	}

	public void setPatrolSchemeRecordItems(List<PatrolSchemeRecordItem> patrolSchemeRecordItems) {
		this.patrolSchemeRecordItems = patrolSchemeRecordItems;
	}

	public Integer getPatrolUserId() {
		return patrolUserId;
	}

	public void setPatrolUserId(Integer patrolUserId) {
		this.patrolUserId = patrolUserId;
	}

	public String getPatrolUserName() {
		return patrolUserName;
	}

	public void setPatrolUserName(String patrolUserName) {
		this.patrolUserName = patrolUserName;
	}

	public String getPatrolUserMobile() {
		return patrolUserMobile;
	}

	public void setPatrolUserMobile(String patrolUserMobile) {
		this.patrolUserMobile = patrolUserMobile;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
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