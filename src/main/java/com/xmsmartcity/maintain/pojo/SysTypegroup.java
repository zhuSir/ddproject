package com.xmsmartcity.maintain.pojo;

import java.util.Date;

public class SysTypegroup {
    private Integer id;

    private String typegroupName;

    private String typegroupCode;

    private Integer createUserId;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypegroupName() {
        return typegroupName;
    }

    public void setTypegroupName(String typegroupName) {
        this.typegroupName = typegroupName == null ? null : typegroupName.trim();
    }

    public String getTypegroupCode() {
        return typegroupCode;
    }

    public void setTypegroupCode(String typegroupCode) {
        this.typegroupCode = typegroupCode == null ? null : typegroupCode.trim();
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
}