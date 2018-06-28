package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class FaultInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer companyId;

    private Integer userId;

    private String code;

    private Integer level;

    private String faultUserName;

    private String faultUserMobile;

    private Integer projectId;

    private Integer serviceTypeId;

    private String introduce;

    private String position;

    private Date createtime;

    private Date receivetime;
    
    private Date appointmentTime;

    private Integer equipId;

    private String pics;

    private Integer faultType;

    private Integer state;

    private Integer isAdd;

    private Integer maintainState;

    private Date updatetime;

    private Integer isDel;
    
    private String lng;//经度
    
    private String lat;//维度
    
    private String projectPic;//项目头像
    
    private String projectName;//项目名称
    
    private String serviceTypeName;//服务内容ID
    
    private String userName;//报障提交人名称
    
    private String headPic;//报障提交人头像
    
    private String userPhone;//提交人手机号
    
    private String maintainUserName;//维保负责人
    
    private String maintainUserMobile;//维保负责人手机号
    
    private List<User> maintainUsers;//维修工程师
    
    private Integer faultState;//0:普通人 1:转派  2：增援
    
    private Integer role;//0：维修工程师  1：乙方负责人  2：其他人员
    
    private Evaluate evaluate;//评价
    
    private float aveEvaluate;//报障单平均评分
    
    private String faultCode;//报障单code
    
    private String equipName;//设备名称

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public float getAveEvaluate() {
		return aveEvaluate;
	}

	public void setAveEvaluate(float aveEvaluate) {
		this.aveEvaluate = aveEvaluate;
	}

	public Evaluate getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(Evaluate evaluate) {
		this.evaluate = evaluate;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Integer getFaultState() {
		if(faultState == null){
			faultState = 0;
		}
		return faultState;
	}

	public void setFaultState(Integer faultState) {
		this.faultState = faultState;
	}

	public String getProjectPic() {
		return projectPic;
	}

	public void setProjectPic(String projectPic) {
		this.projectPic = projectPic;
	}

	public String getMaintainUserMobile() {
		return maintainUserMobile;
	}

	public void setMaintainUserMobile(String maintainUserMobile) {
		this.maintainUserMobile = maintainUserMobile;
	}

	public String getMaintainUserName() {
		return maintainUserName;
	}

	public void setMaintainUserName(String maintainUserName) {
		this.maintainUserName = maintainUserName;
	}

	public List<User> getMaintainUsers() {
		return maintainUsers;
	}

	public void setMaintainUsers(List<User> maintainUsers) {
		this.maintainUsers = maintainUsers;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getFaultUserName() {
        return faultUserName;
    }

    public void setFaultUserName(String faultUserName) {
        this.faultUserName = faultUserName == null ? null : faultUserName.trim();
    }

    public String getFaultUserMobile() {
        return faultUserMobile;
    }

    public void setFaultUserMobile(String faultUserMobile) {
        this.faultUserMobile = faultUserMobile == null ? null : faultUserMobile.trim();
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(Date receivetime) {
        this.receivetime = receivetime;
    }
    
    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getEquipId() {
        return equipId;
    }

    public void setEquipId(Integer equipId) {
        this.equipId = equipId;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    public Integer getFaultType() {
        return faultType;
    }

    public void setFaultType(Integer faultType) {
        this.faultType = faultType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(Integer isAdd) {
        this.isAdd = isAdd;
    }

    public Integer getMaintainState() {
        return maintainState;
    }

    public void setMaintainState(Integer maintainState) {
        this.maintainState = maintainState;
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

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
    
    
}