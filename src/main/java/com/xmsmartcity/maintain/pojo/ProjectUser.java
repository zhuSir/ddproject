package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class ProjectUser implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer isLeader;

    private Integer projectId;

    private Integer userId;
    
    private Integer type;

    private Integer serviceTypeId;

    private Date createtime;

    private Integer isDel;
    
    private String userName; //用户名称
    
    private String userHeadPic;//用户头像
    
    public String getUserHeadPic() {
//    	if(this.userHeadPic != null &&
//    			this.userHeadPic.indexOf(OssUtil.IMG_ACCESS_URL) == -1){
//    		this.userHeadPic = OssUtil.IMG_ACCESS_URL+"/"+userHeadPic;
//    	}
		return userHeadPic;
	}

	public void setUserHeadPic(String userHeadPic) {
		this.userHeadPic = userHeadPic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ProjectUser(){}
	
    public ProjectUser(Integer isLeader,Integer projectId,Integer userId,Integer type,Integer serviceTypeId){
		super();
		this.isLeader = isLeader;
		this.projectId = projectId;
		this.userId = userId;
		this.type = type;
		this.serviceTypeId = serviceTypeId;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(Integer isLeader) {
        this.isLeader = isLeader;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}