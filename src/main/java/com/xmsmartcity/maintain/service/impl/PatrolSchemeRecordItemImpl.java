package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.PatrolSchemeRecordItemMapper;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecordItem;
import com.xmsmartcity.maintain.service.PatrolSchemeRecordItemService;

@Service
public class PatrolSchemeRecordItemImpl extends BaseServiceImpl<PatrolSchemeRecordItem> implements PatrolSchemeRecordItemService {

	@Autowired
	private PatrolSchemeRecordItemMapper patrolSchemeRecordItemDao;
	
	public PatrolSchemeRecordItemImpl(BaseDao<PatrolSchemeRecordItem> dao) {
		super(dao);
	}

	@Override
	public List<PatrolSchemeRecordItem> selectByRecord(PatrolSchemeRecordItem record) {
		return patrolSchemeRecordItemDao.selectByRecord(record);
	}
	
}
