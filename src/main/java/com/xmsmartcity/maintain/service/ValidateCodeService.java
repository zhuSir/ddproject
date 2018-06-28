package com.xmsmartcity.maintain.service;

import com.xmsmartcity.maintain.pojo.ValidateCode;

public interface ValidateCodeService extends BaseService<ValidateCode>{

	public void newValidateCode(String phone,String code);
	
	public boolean validateCode(String phone, String code);
}
