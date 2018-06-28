package com.xmsmartcity.maintain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.ValidateCodeMapper;
import com.xmsmartcity.maintain.pojo.ValidateCode;
import com.xmsmartcity.maintain.service.RedisCacheService;
import com.xmsmartcity.maintain.service.ValidateCodeService;

@Service
public class ValidateCodeServiceImpl extends BaseServiceImpl<ValidateCode> implements ValidateCodeService{

	public ValidateCodeServiceImpl(BaseDao<ValidateCode> dao) {
		super(dao);
	}

	@Autowired
	private ValidateCodeMapper dao;
	
	@Autowired
	private RedisCacheService redisCache;
	
	@Override
	public void newValidateCode(String phone, String code) {
		ValidateCode obj = new ValidateCode(phone,code);
		dao.insert(obj);
	}
	
	/**
	 * 验证code是否正确
	 */
	@Override
	public boolean validateCode(String phone, String code) {
		//redis 获取code
		if(redisCache.getObject(phone)!=null){
			String oldCode = redisCache.getObject(phone).toString();
			if(oldCode.equals(code)){
				return true;
			}else{
				return false;
			}
		}
		ValidateCode obj = new ValidateCode();
		obj.setPhone(phone);
		obj.setCode(code);
		int res = dao.selectByPhoneAndCode(obj);
		if(res > 0 ){
			return true;
		}
		return false;
	}

}
