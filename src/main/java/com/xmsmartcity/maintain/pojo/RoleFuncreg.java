package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;

public class RoleFuncreg implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer roleId;

    private Integer funcregId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getFuncregId() {
        return funcregId;
    }

    public void setFuncregId(Integer funcregId) {
        this.funcregId = funcregId;
    }
}