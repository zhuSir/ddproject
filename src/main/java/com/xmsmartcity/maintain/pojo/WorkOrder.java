package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class WorkOrder implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer faultInfoId;

    private String introduce;

    private String reason;

    private String remark;

    private BigDecimal materialPrice;

    private BigDecimal labourPrice;

    private BigDecimal otherPrice;

    private BigDecimal totalPrice;

    private String pics;

    private Boolean isNeedConfirm;

    private Boolean isUnderWarranty;

    private Date updatetime;
    
    private Date createtime;
    
    private Integer userId;//用户给ID
    
    private String token;//系统token
    
    private List<MaterialLabour> materias;//费用列表

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

	public List<MaterialLabour> getMaterias() {
    	if(materias == null){
    		this.materias = new ArrayList<MaterialLabour>();
    	}
		return materias;
	}

	public void setMaterias(List<MaterialLabour> materias) {
		this.materias = materias;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFaultInfoId() {
        return faultInfoId;
    }

    public void setFaultInfoId(Integer faultInfoId) {
        this.faultInfoId = faultInfoId;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    public BigDecimal getLabourPrice() {
        return labourPrice;
    }

    public void setLabourPrice(BigDecimal labourPrice) {
        this.labourPrice = labourPrice;
    }

    public BigDecimal getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(BigDecimal otherPrice) {
        this.otherPrice = otherPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    public Boolean getIsNeedConfirm() {
        return isNeedConfirm;
    }

    public void setIsNeedConfirm(Boolean isNeedConfirm) {
        this.isNeedConfirm = isNeedConfirm;
    }

    public Boolean getIsUnderWarranty() {
        return isUnderWarranty;
    }

    public void setIsUnderWarranty(Boolean isUnderWarranty) {
        this.isUnderWarranty = isUnderWarranty;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}