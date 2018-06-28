package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.xmsmartcity.maintain.common.Entity;

@Entity
public class MaintainOperateLog implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer faultInfoId;

    private String userName;
    
    private Integer userId;
    
    private String userHeadPic;

    private String title;

    private Integer type;

    private Integer state;
    
    private Integer faultState;

    private String content;
    
    private String pics;
    
    private List<String> pictures;

    private Date createtime;

    private Date updatetime;

    private Integer isDel;
    
    public MaintainOperateLog(){}
    
    public MaintainOperateLog(Integer faultInfoId, String content, String title,
			Integer type,Integer state,Integer fault_state, Date createtime,String userName,Integer userId) {
		super();
		this.faultInfoId = faultInfoId;
		this.content = content;
		this.title = title;
		this.type = type;//0甲方 1乙方
		this.state = state;// 0:不可查看详情  1:查看详情 2:查看评价
		this.faultState = fault_state;//1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
		this.createtime = createtime;
		this.userName = userName;
		this.userId = userId;
	}

    public List<String> getPictures() {
//    	if(this.pics != null && StringUtils.isNotBlank(this.pics)){
//    		this.pictures = new ArrayList<String>();
//    		String[] ps = this.pics.split(",");
//    		for(String p : ps){
//    			if(p.indexOf(OssUtil.IMG_ACCESS_URL) == -1){
//    	    		p = OssUtil.IMG_ACCESS_URL+"/"+p;
//    	    		this.pictures.add(p);
//    	    	}
//    		}
//    	}
		return this.pictures;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public String getUserHeadPic() {
		return userHeadPic;
	}

	public void setUserHeadPic(String userHeadPic) {
		this.userHeadPic = userHeadPic;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFaultState() {
		return faultState;
	}

	public void setFaultState(Integer faultState) {
		this.faultState = faultState;
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