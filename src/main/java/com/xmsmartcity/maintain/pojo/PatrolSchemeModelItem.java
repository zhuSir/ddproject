package com.xmsmartcity.maintain.pojo;

public class PatrolSchemeModelItem {
    private Integer id;

    private Integer patrolSchemeModelId;

    private Integer number;

    private String name;

    private String introduce;

    private String remark;

    private Integer isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatrolSchemeModelId() {
        return patrolSchemeModelId;
    }

    public void setPatrolSchemeModelId(Integer patrolSchemeModelId) {
        this.patrolSchemeModelId = patrolSchemeModelId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}