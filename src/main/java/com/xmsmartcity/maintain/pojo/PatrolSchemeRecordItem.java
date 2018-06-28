package com.xmsmartcity.maintain.pojo;

public class PatrolSchemeRecordItem {
    private Integer id;

    private Integer patrolSchemeItemId;

    private Integer patrolSchemeRecordId;

    private Integer isNormal;

    private String introduce;

    private String remark;

    private Integer isDel;
    
    private Integer number;
    
    private String name;
    
    private String patrolSchemeItemIntroduce;
    
    private String equipId;
    
    private String equipName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatrolSchemeItemId() {
        return patrolSchemeItemId;
    }

    public void setPatrolSchemeItemId(Integer patrolSchemeItemId) {
        this.patrolSchemeItemId = patrolSchemeItemId;
    }

    public Integer getPatrolSchemeRecordId() {
        return patrolSchemeRecordId;
    }

    public void setPatrolSchemeRecordId(Integer patrolSchemeRecordId) {
        this.patrolSchemeRecordId = patrolSchemeRecordId;
    }

    public Integer getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(Integer isNormal) {
        this.isNormal = isNormal;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatrolSchemeItemIntroduce() {
		return patrolSchemeItemIntroduce;
	}

	public void setPatrolSchemeItemIntroduce(String patrolSchemeItemIntroduce) {
		this.patrolSchemeItemIntroduce = patrolSchemeItemIntroduce;
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
    
}