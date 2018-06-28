package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.xmsmartcity.maintain.common.Entity;

@Entity
public class ServiceType implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String name;

    private String maintainUserName;

    private String maintainUserMobile;

    private Integer state;//0：表示未生效（待确认），1：表示已生效,2：已拒绝

    private Integer projectId;

    private Date updatetime;

    private Integer isDel;
    
    private Integer is_leader; //是否负责人  否：0/是：1
    
    private List<ProjectUser> projectUsers;//乙方成员（服务内容下）
    
    private String userHeadPic;//用户头像
    
    private Integer canEdit;//判断乙方是否已加入到服务内容中，判断是否可以编辑
    
	public Integer getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Integer canEdit) {
		this.canEdit = canEdit;
	}

	public String getUserHeadPic() {
		return userHeadPic;
	}

	public void setUserHeadPic(String userHeadPic) {
		this.userHeadPic = userHeadPic;
	}

	public List<ProjectUser> getProjectUsers() {
		return projectUsers;
	}

	public void setProjectUsers(List<ProjectUser> projectUsers) {
		this.projectUsers = projectUsers;
	}

	public Integer getIs_leader() {
		return is_leader;
	}

	public void setIs_leader(Integer is_leader) {
		this.is_leader = is_leader;
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

    public String getMaintainUserName() {
        return maintainUserName;
    }

    public void setMaintainUserName(String maintainUserName) {
        this.maintainUserName = maintainUserName == null ? null : maintainUserName.trim();
    }

    public String getMaintainUserMobile() {
        return maintainUserMobile;
    }

    public void setMaintainUserMobile(String maintainUserMobile) {
        this.maintainUserMobile = maintainUserMobile == null ? null : maintainUserMobile.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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
}