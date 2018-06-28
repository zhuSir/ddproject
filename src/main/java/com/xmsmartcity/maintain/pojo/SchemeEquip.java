package com.xmsmartcity.maintain.pojo;

import java.util.Date;

public class SchemeEquip {
    private Integer id;

    private Integer patrolSchemeId;

    private Integer equipId;

    private Date createtime;

    private Integer createUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatrolSchemeId() {
        return patrolSchemeId;
    }

    public void setPatrolSchemeId(Integer patrolSchemeId) {
        this.patrolSchemeId = patrolSchemeId;
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