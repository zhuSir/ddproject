package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.xmsmartcity.maintain.common.Entity;
@Entity
public class MaintainInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer faultInfoId;

    private String userName;

    private String title;

    private int type;

    private int state;

    private String content;

    private String pics;

    private String position;

    private Date createtime;

    private Date updatetime;

    private Integer isDel;
    
    private List<String> picList;

    public MaintainInfo(){}
    
    public MaintainInfo(Integer faultInfoId, String content, String title,
			Integer type,Integer state, Date createtime,String userName) {
		super();
		this.faultInfoId = faultInfoId;
		this.content = content;
		this.title = title;
		this.type = type;//0甲方 1乙方
		this.state = state;// 0:不可查看详情  1:查看详情 2:查看评价
		this.createtime = createtime;
		this.userName = userName;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics == null ? null : pics.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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