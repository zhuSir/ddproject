package com.xmsmartcity.maintain.pojo;

import java.util.Date;

public class SchemeItemEquip {
    private Integer id;

    private Integer patrolSchemeItemId;

    private Integer equipId;

    private Date createtime;

    private Integer createUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatrolSchemeItemId() {
        return patrolSchemeItemId;
    }

    public void setPatrolSchemeItemId(Integer patrolSchemeItemId) {
        this.patrolSchemeItemId = patrolSchemeItemId;
    }

    public Integer getEquipId() {
        return equipId;
    }

    public void setEquipId(Integer equipId) {
        this.equipId = equipId;
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
}