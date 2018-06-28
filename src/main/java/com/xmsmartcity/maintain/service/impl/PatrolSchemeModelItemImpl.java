package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.PatrolSchemeModelItemMapper;
import com.xmsmartcity.maintain.pojo.PatrolSchemeModelItem;
import com.xmsmartcity.maintain.service.PatrolSchemeModelItemService;

@Service
public class PatrolSchemeModelItemImpl extends BaseServiceImpl<PatrolSchemeModelItem> implements PatrolSchemeModelItemService {

	@Autowired
	PatrolSchemeModelItemMapper patrolSchemeModelItemDao;
	
	public PatrolSchemeModelItemImpl(BaseDao<PatrolSchemeModelItem> dao) {
		super(dao);
	}

	@Override
	public List<PatrolSchemeModelItem> getListByPatrolSchemeModelId(Integer id) {
		PatrolSchemeModelItem patrolSchemeModelItem=new PatrolSchemeModelItem();
		patrolSchemeModelItem.setPatrolSchemeModelId(id);
		return patrolSchemeModelItemDao.selectByRecord(patrolSchemeModelItem);
	}
	
}
