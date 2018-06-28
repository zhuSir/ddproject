package com.xmsmartcity.maintain.service;

import java.util.List;

import com.xmsmartcity.maintain.pojo.SysTypegroup;

public interface SysTypegroupService extends BaseService<SysTypegroup>{
	
	List<SysTypegroup> getListByRecord(SysTypegroup record,Integer startIndex,Integer pageSize);
	
}
