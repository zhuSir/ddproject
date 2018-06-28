package com.xmsmartcity.maintain.pojo;

import java.io.Serializable;
import java.util.Date;

import com.xmsmartcity.maintain.common.Entity;

@Entity
public class ValidateCode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidateCode(){};

	public ValidateCode(String phone,String code){
		super();
		this.phone = phone;
		this.code = code;
	}
	
    private Integer id;

    private String phone;

    private String code;

    private Date createtime;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}