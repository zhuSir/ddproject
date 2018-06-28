package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.PatrolSchemeItemMapper;
import com.xmsmartcity.maintain.pojo.PatrolSchemeItem;
import com.xmsmartcity.maintain.service.PatrolSchemeItemService;

@Service
public class PatrolSchemeItemImpl extends BaseServiceImpl<PatrolSchemeItem> implements PatrolSchemeItemService {

	@Autowired
	private PatrolSchemeItemMapper patrolSchemeItemDao;
	
	public PatrolSchemeItemImpl(BaseDao<PatrolSchemeItem> dao) {
		super(dao);
	}

	@Override
	public List<PatrolSchemeItem> getListByPatrolSchemeId(Integer Id) {
		PatrolSchemeItem patrolSchemeItem=new PatrolSchemeItem();
		patrolSchemeItem.setPatrolSchemeId(Id);
		List<PatrolSchemeItem> list=patrolSchemeItemDao.selectByRecord(patrolSchemeItem);
		return list;
	}
}
