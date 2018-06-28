package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.FaultStore;

public interface FaultStoreMapper extends BaseDao<FaultStore>{
    
    List<Map<String,Object>> selectListByFaultInfoId(Integer faultInfoId);

	Map<String, Object> selectMapByprimarkId(Integer faultStoreId);
    
}