package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;
@Entity
public class EquipsType implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String equipsTypeName;

    private String equipsTypeCode;

    private Integer createUserId;

    private Date createtime;

    private Integer updateUserId;

    private Date updatetime;
    
    //该类别下设备数量
    private Integer equipSum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEquipsTypeName() {
        return equipsTypeName;
    }

    public void setEquipsTypeName(String equipsTypeName) {
        this.equipsTypeName = equipsTypeName == null ? null : equipsTypeName.trim();
    }

    public String getEquipsTypeCode() {
        return equipsTypeCode;
    }

    public void setEquipsTypeCode(String equipsTypeCode) {
        this.equipsTypeCode = equipsTypeCode == null ? null : equipsTypeCode.trim();
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public Integer getEquipSum() {
		return equipSum;
	}

	public void setEquipSum(Integer equipSum) {
		this.equipSum = equipSum;
	}
    
}