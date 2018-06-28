package com.xmsmartcity.maintain.service;

import java.util.List;

import com.xmsmartcity.maintain.pojo.PatrolSchemeModelItem;

public interface PatrolSchemeModelItemService extends BaseService<PatrolSchemeModelItem>{
	List<PatrolSchemeModelItem> getListByPatrolSchemeModelId(Integer Id);
}
