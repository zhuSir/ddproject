package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class Company implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8233231104538755850L;

	private Integer id;

    private String name;
    
    private String shortName;

    private Integer state;

    private String telephone;

    private String province;

    private String city;

    private String area;

    private String address;

    private String lng;

    private String lat;

    private String headPic;

    private Integer responserId;

    private String responserName;

    private String responserPhone;

    private Date createtime;

    private String threeCertificates;

    private Integer checkState;

    private Date updatetime;

    private Integer isDel;

    private String content;
    
    private List<User> users;//用户
    
    private Integer userSize;//用户个数
    
    private Integer faultIngCount;//处理中报障
    
    private Integer faultMonthCount;//本月报障
    
    private Integer faultSumCount;//总报障
    
    private Integer maintainIngCount;//处理中维保
    
    private Integer maintainMonthCount;//本月维保
    
    private Integer maintainSumCount;//总维保

	private String certificatesA;//三证之一
	private String certificatesB;//三证之二
	private String certificatesC;//三证之三
    
	private Integer isLeader;//0非负责人  1负责人
	private String timeStr;
	private String createtimeStr; 
	private List<String> picList = new ArrayList<String>();
	
    public Integer getFaultIngCount() {
		return faultIngCount;
	}

	public void setFaultIngCount(Integer faultIngCount) {
		this.faultIngCount = faultIngCount;
	}

	public Integer getFaultMonthCount() {
		return faultMonthCount;
	}

	public void setFaultMonthCount(Integer faultMonthCount) {
		this.faultMonthCount = faultMonthCount;
	}

	public Integer getFaultSumCount() {
		return faultSumCount;
	}

	public void setFaultSumCount(Integer faultSumCount) {
		this.faultSumCount = faultSumCount;
	}

	public Integer getMaintainIngCount() {
		return maintainIngCount;
	}

	public void setMaintainIngCount(Integer maintainIngCount) {
		this.maintainIngCount = maintainIngCount;
	}

	public Integer getMaintainMonthCount() {
		return maintainMonthCount;
	}

	public void setMaintainMonthCount(Integer maintainMonthCount) {
		this.maintainMonthCount = maintainMonthCount;
	}

	public Integer getMaintainSumCount() {
		return maintainSumCount;
	}

	public void setMaintainSumCount(Integer maintainSumCount) {
		this.maintainSumCount = maintainSumCount;
	}

	public Integer getUserSize() {
		return userSize;
	}

	public void setUserSize(Integer userSize) {
		this.userSize = userSize;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
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
    
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
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

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic == null ? null : headPic.trim();
    }

    public Integer getResponserId() {
        return responserId;
    }

    public void setResponserId(Integer responserId) {
        this.responserId = responserId;
    }

    public String getResponserName() {
        return responserName;
    }

    public void setResponserName(String responserName) {
        this.responserName = responserName == null ? null : responserName.trim();
    }

    public String getResponserPhone() {
        return responserPhone;
    }

    public void setResponserPhone(String responserPhone) {
        this.responserPhone = responserPhone == null ? null : responserPhone.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getThreeCertificates() {
        return threeCertificates;
    }

    public void setThreeCertificates(String threeCertificates) {
    	this.threeCertificates = threeCertificates;
		if(StringUtils.isNotBlank(threeCertificates)){
			String[] ss = threeCertificates.split(",");
			for (String s : ss) {
				this.picList.add(s);
			}
		}
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
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

    public String getContent() {
    	if(content == null){
    		return "";
    	}
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public String getCertificatesA() {
		return certificatesA;
	}

	public void setCertificatesA(String certificatesA) {
		this.certificatesA = certificatesA;
	}

	public String getCertificatesB() {
		return certificatesB;
	}

	public void setCertificatesB(String certificatesB) {
		this.certificatesB = certificatesB;
	}

	public String getCertificatesC() {
		return certificatesC;
	}

	public void setCertificatesC(String certificatesC) {
		this.certificatesC = certificatesC;
	}

	public Integer getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getCreatetimeStr() {
		return createtimeStr;
	}

	public void setCreatetimeStr(String createtimeStr) {
		this.createtimeStr = createtimeStr;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}
    
    
}