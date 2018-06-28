package com.xmsmartcity.maintain.service;

import java.util.List;

import com.xmsmartcity.maintain.pojo.SysType;

public interface SysTypeService extends BaseService<SysType>{
	List<SysType> getListByRecord(SysType record,Integer startIndex,Integer pageSize);
}
