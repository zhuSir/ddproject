package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.xmsmartcity.maintain.common.Entity;

/**
 * 用于统计维保、报障汇总
 * @author chenby
 *
 */
@Entity
public class FaultSummary implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;//报障维修人员表ID

    private String userId;//维修人员ID

    private String userName;//维修人员名称
    
    private String userPic;//维修人员头像
    
    private String faultInfoId;//报障ID	

    private String projectId;//项目ID
    
    private String projectName;//项目名称

    private Integer faultSum;//报障次数统计

    private Integer maintenanSum;//维保次数统计

    private BigDecimal paySum;//支出汇总

    private BigDecimal incomeSum;//收入汇总

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	public String getFaultInfoId() {
		return faultInfoId;
	}

	public void setFaultInfoId(String faultInfoId) {
		this.faultInfoId = faultInfoId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getFaultSum() {
		return faultSum;
	}

	public void setFaultSum(Integer faultSum) {
		this.faultSum = faultSum;
	}

	public Integer getMaintenanSum() {
		return maintenanSum;
	}

	public void setMaintenanSum(Integer maintenanSum) {
		this.maintenanSum = maintenanSum;
	}

	public BigDecimal getPaySum() {
		return paySum;
	}

	public void setPaySum(BigDecimal paySum) {
		this.paySum = paySum;
	}

	public BigDecimal getIncomeSum() {
		return incomeSum;
	}

	public void setIncomeSum(BigDecimal incomeSum) {
		this.incomeSum = incomeSum;
	}
    
    
    

}