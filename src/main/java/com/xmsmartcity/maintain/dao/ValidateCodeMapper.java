package com.xmsmartcity.maintain.dao;

import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.ValidateCode;

@Repository
public interface ValidateCodeMapper extends BaseDao<ValidateCode>{
    int selectByPhoneAndCode(ValidateCode record);
}