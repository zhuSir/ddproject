package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.SysParams;

@Repository
public interface SysParamsMapper extends BaseDao<SysParams> {
	public List<SysParams> selectListByParamName(String paramName);
}