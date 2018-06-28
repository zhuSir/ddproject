package com.xmsmartcity.maintain.pojo;

import java.util.Date;

/**
 * 巡检任务、记录列表返回类
 * @author chenbinyi
 *
 */
public class PatrolSchemeReturn {
	
    private Integer patrolSchemeRecordId;

    private Integer patrolSchemeId;
    
    private Integer patrolSchemeName;

    private Date patrolTime;

    private String position;

    private String lng;

    private String lat;

    private Integer state;
    
    private Integer stateStr;

    private String summarize;

    private String pics;

	public Integer getPatrolSchemeRecordId() {
		return patrolSchemeRecordId;
	}

	public void setPatrolSchemeRecordId(Integer patrolSchemeRecordId) {
		this.patrolSchemeRecordId = patrolSchemeRecordId;
	}

	public Integer getPatrolSchemeId() {
		return patrolSchemeId;
	}

	public void setPatrolSchemeId(Integer patrolSchemeId) {
		this.patrolSchemeId = patrolSchemeId;
	}

	public Integer getPatrolSchemeName() {
		return patrolSchemeName;
	}

	public void setPatrolSchemeName(Integer patrolSchemeName) {
		this.patrolSchemeName = patrolSchemeName;
	}

	public Date getPatrolTime() {
		return patrolTime;
	}

	public void setPatrolTime(Date patrolTime) {
		this.patrolTime = patrolTime;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getStateStr() {
		return stateStr;
	}

	public void setStateStr(Integer stateStr) {
		this.stateStr = stateStr;
	}

	public String getSummarize() {
		return summarize;
	}

	public void setSummarize(String summarize) {
		this.summarize = summarize;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}
    
    
    
}
