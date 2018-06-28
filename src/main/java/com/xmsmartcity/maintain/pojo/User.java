package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;
import com.xmsmartcity.maintain.common.Entity;

@Entity
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User(){}
	public User(String phone,String password,Date lastLogintime,Date registerTime,String headPic,String name){
		super();
		this.phone = phone;
		this.password = password;
		this.lastLogintime = lastLogintime;
		this.registerTime = registerTime;
		this.headPic = headPic;
		this.name = name;
	}
	
    private Integer id;

    private String phone;

    private String name;

    private String password;

    private String headPic;

    private Integer companyId;

    private String mail;

    private Integer sex;

    private String contactInfo;

    private String openId;

    private Date lastLogintime;

    private Integer loginTimes;

    private Date registerTime;

    private Integer state;

    private Integer registerFlatform;

    private String title;

    private Date joinTime;

    private Date updatetime;

    private Integer isDel;

    private String token;

    private Integer role;

    private String remark;
    
    private String xcxOpenId;
    
    private String unionid;

    //-----------------业务字段
    private String pushToken;//个推用户唯一id
    
    private String companyName;
    
    public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPushToken() {
		return pushToken;
	}
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getHeadPic() {
//    	if(this.headPic != null &&
//    			this.headPic.indexOf(OssUtil.IMG_ACCESS_URL) == -1){
//    		this.headPic = OssUtil.IMG_ACCESS_URL+"/"+headPic;
//    	}
        return this.headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic == null ? null : headPic.trim();
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail == null ? null : mail.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo == null ? null : contactInfo.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public Date getLastLogintime() {
        return lastLogintime;
    }

    public void setLastLogintime(Date lastLogintime) {
        this.lastLogintime = lastLogintime;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRegisterFlatform() {
        return registerFlatform;
    }

    public void setRegisterFlatform(Integer registerFlatform) {
        this.registerFlatform = registerFlatform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
	public String getXcxOpenId() {
		return xcxOpenId;
	}
	public void setXcxOpenId(String xcxOpenId) {
		this.xcxOpenId = xcxOpenId;
	}
    
}