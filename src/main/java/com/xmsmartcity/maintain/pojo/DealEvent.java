package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;
import com.xmsmartcity.maintain.common.Entity;

@Entity
public class DealEvent implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6009770636277021404L;

	private Integer id;

    private Integer userId;

    private Integer hasCompany;

    private Integer hasProject;

    private Integer hasInfo;

    private Integer hasFaultInfo;

    private Integer isDel;

    private Date updatetime;
    
    private Integer unReadNum;
    
    private Integer isLeader;
    
    private Date company_createtime;

    public Date getCompany_createtime() {
		return company_createtime;
	}

	public void setCompany_createtime(Date company_createtime) {
		this.company_createtime = company_createtime;
	}

	public Integer getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}

	public Integer getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(Integer unReadNum) {
		this.unReadNum = unReadNum;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHasCompany() {
        return hasCompany;
    }

    public void setHasCompany(Integer hasCompany) {
        this.hasCompany = hasCompany;
    }

    public Integer getHasProject() {
        return hasProject;
    }

    public void setHasProject(Integer hasProject) {
        this.hasProject = hasProject;
    }

    public Integer getHasInfo() {
        return hasInfo;
    }

    public void setHasInfo(Integer hasInfo) {
        this.hasInfo = hasInfo;
    }

    public Integer getHasFaultInfo() {
        return hasFaultInfo;
    }

    public void setHasFaultInfo(Integer hasFaultInfo) {
        this.hasFaultInfo = hasFaultInfo;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}