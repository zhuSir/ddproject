package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class Project implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String name;

    private Date maintainContract;

    private Date createtime;

    private Integer createUserId;

    private Integer createResource;

    private Integer ownerId;

    private String ownerName;

    private String ownerPhone;

    private Integer itemType;

    private Integer state;

    private String pic;

    private String province;

    private String city;

    private String area;

    private String address;

    private String lng;

    private String lat;

    private Date updatetime;

    private Integer isDel;

    private String introduce;
    
    private Integer companyId;
    
    private String companyName;
    
    private Integer type;//甲方：0/乙方：1
    
    private Integer is_leader;//是否项目负责人  否：0/是：1
    
    private Integer is_company_leader;//是否团队负责人

    private List<ServiceType> services;//存放服务内容
    
    private List<ProjectUser> projectUsers;//用户
    
    private String maintain_contract;//合同时间
    
    private Integer userId; //用户id
    
    private String userHeadPic;//用户头像
    
    private String token;//系统token
    
    private Integer serviceTypeId;//服务类型id
    
    private Integer aveEvaluate;//项目报障单平均评分
    
    private Integer aveTimes;//项目报障单平均处理时长

	public Integer getIs_company_leader() {
		return is_company_leader;
	}

	public void setIs_company_leader(Integer is_company_leader) {
		this.is_company_leader = is_company_leader;
	}

	public String getUserHeadPic() {
		return userHeadPic;
	}

	public void setUserHeadPic(String userHeadPic) {
		this.userHeadPic = userHeadPic;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getAveEvaluate() {
		return aveEvaluate;
	}

	public void setAveEvaluate(Integer aveEvaluate) {
		this.aveEvaluate = aveEvaluate;
	}

	public Integer getAveTimes() {
		return aveTimes;
	}

	public void setAveTimes(Integer aveTimes) {
		this.aveTimes = aveTimes;
	}

	public Integer getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Integer serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getIs_leader() {
		if(is_leader == null){
			is_leader = 0;
		}
		return is_leader;
	}

	public void setIs_leader(Integer is_leader) {
		this.is_leader = is_leader;
	}

	public String getMaintain_contract() {
		return maintain_contract;
	}

	public void setMaintain_contract(String maintain_contract) {
		this.maintain_contract = maintain_contract;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<ProjectUser> getProjectUsers() {
		return projectUsers;
	}

	public void setProjectUsers(List<ProjectUser> projectUsers) {
		this.projectUsers = projectUsers;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<ServiceType> getServices() {
		if(this.services==null){
			this.services = new ArrayList<ServiceType>();
		}
		return services;
	}

	public void setServices(List<ServiceType> services) {
		this.services = services;
	}
    
//    public Integer getNoReadSum() {
//		return noReadSum;
//	}
//
//	public void setNoReadSum(Integer noReadSum) {
//		this.noReadSum = noReadSum;
//	}

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

    public Date getMaintainContract() {
        return maintainContract;
    }

    public void setMaintainContract(Date maintainContract) {
        this.maintainContract = maintainContract;
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

    public Integer getCreateResource() {
        return createResource;
    }

    public void setCreateResource(Integer createResource) {
        this.createResource = createResource;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName == null ? null : ownerName.trim();
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone == null ? null : ownerPhone.trim();
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getState() {
        return state;
    }

    /*
     * 项目状态，0：未确认，1：已确认 2：已拒绝
     */
    public void setState(Integer state) {
        this.state = state;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }
}