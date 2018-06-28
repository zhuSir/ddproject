package com.xmsmartcity.maintain.pojo;

import java.util.Date;
import java.util.List;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class PatrolScheme {
    private Integer id;

    private String name;

    private Integer patrolUserId;

    private String patrolUserName;

    private String patrolUserMobile;

    private Date checkTime;

    private Integer period;

    private Integer patrolAbnormal;

    private String equipId;

    private Integer isCombine;
    
    private Date createtime;

    private Integer createUserId;

    private Date updatetime;

    private Integer updateUserId;

    private String remark;

    private Integer isDel;
    
    private Integer userId;

    private Boolean Ismodel; 
    
    private List<PatrolSchemeItem> patrolSchemeItems;
    
    private String patrolUserPic;

    private String equipName;
    
    private String periodName;
    
    private String patrolAbnormalName;
    
    private String position;
    
    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getIsmodel() {
		return Ismodel;
	}

	public void setIsmodel(Boolean ismodel) {
		Ismodel = ismodel;
	}

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
        this.patrolUserName = patrolUserName == null ? null : patrolUserName.trim();
    }

    public String getPatrolUserMobile() {
        return patrolUserMobile;
    }

    public void setPatrolUserMobile(String patrolUserMobile) {
        this.patrolUserMobile = patrolUserMobile == null ? null : patrolUserMobile.trim();
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

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId == null ? null : equipId.trim();
    }

    public Integer getIsCombine() {
		return isCombine;
	}

	public void setIsCombine(Integer isCombine) {
		this.isCombine = isCombine;
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

	public List<PatrolSchemeItem> getPatrolSchemeItems() {
		return patrolSchemeItems;
	}

	public void setPatrolSchemeItems(List<PatrolSchemeItem> patrolSchemeItems) {
		this.patrolSchemeItems = patrolSchemeItems;
	}

	public String getPatrolUserPic() {
		return patrolUserPic;
	}

	public void setPatrolUserPic(String patrolUserPic) {
		this.patrolUserPic = patrolUserPic;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
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

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
    
}