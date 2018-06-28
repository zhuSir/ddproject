package com.xmsmartcity.maintain.service;

import java.util.List;

import com.xmsmartcity.maintain.pojo.PatrolSchemeItem;

public interface PatrolSchemeItemService extends BaseService<PatrolSchemeItem>{
	List<PatrolSchemeItem> getListByPatrolSchemeId(Integer Id);
}
