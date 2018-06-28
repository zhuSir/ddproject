package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;
@Entity
public class Equips implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer serviceTypeId;

    private String equipName;

    private String equipCode;

    private String equipModel;

    private String firm;

    private Integer equipTypeId;

    private String equipDetail;
    
    private String lng;

    private String lat;

    private String province;

    private String city;

    private String area;

    private String address;
    
    private String equipUserName;

    private String equipUseMobile;

    private String equipMaintainName;

    private String equipMaintainMobile;

    private String equipEqcodePath;

    private Integer isDel;

    private Date creattime;

    private Integer creatUserId;

    private Date updatetime;

    private Integer updateUserId;

    private String remark;

    private String equipTypeName;//设备类型名称
    
    private String sumTime;//设备总维修时长
    
    private String avgScore;//设备评分
    
    private Integer projectId;//项目ID
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName == null ? null : equipName.trim();
    }

    public String getEquipCode() {
        return equipCode;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode == null ? null : equipCode.trim();
    }

    public String getEquipModel() {
        return equipModel;
    }

    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel == null ? null : equipModel.trim();
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm == null ? null : firm.trim();
    }

    public Integer getEquipTypeId() {
        return equipTypeId;
    }

    public void setEquipTypeId(Integer equipTypeId) {
        this.equipTypeId = equipTypeId;
    }

    public String getEquipDetail() {
        return equipDetail;
    }

    public void setEquipDetail(String equipDetail) {
        this.equipDetail = equipDetail == null ? null : equipDetail.trim();
    }

    public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEquipUserName() {
        return equipUserName;
    }

    public void setEquipUserName(String equipUserName) {
        this.equipUserName = equipUserName == null ? null : equipUserName.trim();
    }

    public String getEquipUseMobile() {
        return equipUseMobile;
    }

    public void setEquipUseMobile(String equipUseMobile) {
        this.equipUseMobile = equipUseMobile == null ? null : equipUseMobile.trim();
    }

    public String getEquipMaintainName() {
        return equipMaintainName;
    }

    public void setEquipMaintainName(String equipMaintainName) {
        this.equipMaintainName = equipMaintainName == null ? null : equipMaintainName.trim();
    }

    public String getEquipMaintainMobile() {
        return equipMaintainMobile;
    }

    public void setEquipMaintainMobile(String equipMaintainMobile) {
        this.equipMaintainMobile = equipMaintainMobile == null ? null : equipMaintainMobile.trim();
    }

    public String getEquipEqcodePath() {
        return equipEqcodePath;
    }

    public void setEquipEqcodePath(String equipEqcodePath) {
        this.equipEqcodePath = equipEqcodePath == null ? null : equipEqcodePath.trim();
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getCreattime() {
        return creattime;
    }

    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    public Integer getCreatUserId() {
        return creatUserId;
    }

    public void setCreatUserId(Integer creatUserId) {
        this.creatUserId = creatUserId;
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

	public String getEquipTypeName() {
		return equipTypeName;
	}

	public void setEquipTypeName(String equipTypeName) {
		this.equipTypeName = equipTypeName;
	}

	public String getSumTime() {
		return sumTime;
	}

	public void setSumTime(String sumTime) {
		this.sumTime = sumTime;
	}

	public String getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(String avgScore) {
		this.avgScore = avgScore;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
    
}