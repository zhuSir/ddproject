package com.xmsmartcity.maintain.dao;

import java.util.Map;

import com.xmsmartcity.maintain.pojo.WorkOrder;

public interface WorkOrderMapper extends BaseDao<WorkOrder>{

	WorkOrder selectByFaultInfoId(Integer faultInfoId);
	
	Map<String,Object> selectMapByFaultInfoId(Integer faultInfoId);
}