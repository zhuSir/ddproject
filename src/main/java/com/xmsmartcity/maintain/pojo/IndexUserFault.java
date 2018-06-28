package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;
@Entity
public class IndexUserFault implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer userId;

    private Integer faultInfoId;

    private Integer isDel;

    private Integer type;
    
    private Date createtime;

    private Integer createUserId;

    private Date updatetime;

    private Integer updateUserId;

    private String remark;
    
    public IndexUserFault(){}
	public IndexUserFault(Integer userId,Integer faultInfoId,Integer type,Date createtime,Integer createUserId){
		super();
		this.userId = userId;
		this.faultInfoId = faultInfoId;
		this.type = type;
		this.createtime = createtime;
		this.createUserId = createUserId;
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

    public Integer getFaultInfoId() {
        return faultInfoId;
    }

    public void setFaultInfoId(Integer faultInfoId) {
        this.faultInfoId = faultInfoId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
		this.remark = remark;
	}
    
}