package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class Message implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String phone;

    private String content;

    private Integer type;

    private Integer referId;

    private Integer referUserId;

    private Date createtime;

    private Integer isRead;

    private Integer classify;

    private Integer state;

    private Integer projectId;

    private int jumpType;

    private Date updatetime;

    private int isDel;
    
    private Integer noReadSum;//未读消息数量
    
    private String referUserName;//邀请者/申请者手机号

    public String getReferUserName() {
		return referUserName;
	}

	public void setReferUserName(String referUserName) {
		this.referUserName = referUserName;
	}

	public Message(){}
    
    public Message(String phone, Integer referUserId, String content,
			Integer type, Integer referId, Integer classify, Date createtime,Integer projectId,Integer state) {
		super();
		this.phone = phone;
		this.referUserId = referUserId;
		this.content = content;
		this.type = type;
		this.referId = referId;
		this.classify = classify;
		this.createtime = createtime;
		this.projectId = projectId;
		this.state = state;
	}
    
    public Integer getNoReadSum() {
		return noReadSum;
	}

	public void setNoReadSum(Integer noReadSum) {
		this.noReadSum = noReadSum;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getReferId() {
        return referId;
    }

    public void setReferId(Integer referId) {
        this.referId = referId;
    }

    public Integer getReferUserId() {
        return referUserId;
    }

    public void setReferUserId(Integer referUserId) {
        this.referUserId = referUserId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getClassify() {
        return classify;
    }

    public void setClassify(Integer classify) {
        this.classify = classify;
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

    public int getJumpType() {
        return jumpType;
    }

    public void setJumpType(int jumpType) {
        this.jumpType = jumpType;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }
}