package com.xmsmartcity.maintain.service;

import java.util.List;

import com.xmsmartcity.maintain.pojo.PatrolSchemeModel;

public interface PatrolSchemeModelService extends BaseService<PatrolSchemeModel>{
	
	PatrolSchemeModel saveOrUpdate(PatrolSchemeModel patrolSchemeModel);
	
	List<PatrolSchemeModel> getPatrolSchemeModelList(PatrolSchemeModel patrolSchemeModel,Integer userId,Integer startIndex,Integer pageSize);
}
