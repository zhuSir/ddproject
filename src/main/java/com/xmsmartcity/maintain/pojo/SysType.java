package com.xmsmartcity.maintain.pojo;

import java.util.Date;

public class SysType {
    private Integer id;

    private String typeName;

    private Integer typegroupId;

    private String typeCode;

    private Integer createUserId;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Integer getTypegroupId() {
        return typegroupId;
    }

    public void setTypegroupId(Integer typegroupId) {
        this.typegroupId = typegroupId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode == null ? null : typeCode.trim();
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