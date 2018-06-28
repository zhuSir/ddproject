package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.PatrolSchemeModelItemMapper;
import com.xmsmartcity.maintain.dao.PatrolSchemeModelMapper;
import com.xmsmartcity.maintain.pojo.PatrolSchemeModel;
import com.xmsmartcity.maintain.pojo.PatrolSchemeModelItem;
import com.xmsmartcity.maintain.service.PatrolSchemeModelService;

@Service
public class PatrolSchemeModelImpl extends BaseServiceImpl<PatrolSchemeModel> implements PatrolSchemeModelService {

	@Autowired
	private PatrolSchemeModelMapper patrolSchemeModelDao;
	@Autowired
	private PatrolSchemeModelItemMapper patrolSchemeModelItemDao;
	
	public PatrolSchemeModelImpl(BaseDao<PatrolSchemeModel> dao) {
		super(dao);
	}
	
	@Transactional
	@Override
	public PatrolSchemeModel saveOrUpdate(PatrolSchemeModel patrolSchemeModel) {
		if (patrolSchemeModel.getId()!=null) {
			patrolSchemeModelItemDao.deleteByPatrolSchemeModelId(patrolSchemeModel.getId());
			patrolSchemeModelItemDao.insertBatch(patrolSchemeModel.getPatrolSchemeModelItems());
			patrolSchemeModelDao.updateByPrimaryKeySelective(patrolSchemeModel);
		}else {
			patrolSchemeModelDao.insertSelective(patrolSchemeModel);
			List<PatrolSchemeModelItem> patrolSchemeModelItems=patrolSchemeModel.getPatrolSchemeModelItems();
			for (PatrolSchemeModelItem patrolSchemeModelItem : patrolSchemeModelItems) {
				patrolSchemeModelItem.setPatrolSchemeModelId(patrolSchemeModel.getId());
			}
			patrolSchemeModel.setPatrolSchemeModelItems(patrolSchemeModelItems);
			patrolSchemeModelItemDao.insertBatch(patrolSchemeModel.getPatrolSchemeModelItems());
		}
		return patrolSchemeModel;
	}

	@Override
	public List<PatrolSchemeModel> getPatrolSchemeModelList(PatrolSchemeModel patrolSchemeModel,Integer userId, Integer startIndex,
			Integer pageSize) {
		return patrolSchemeModelDao.selectByRecord(patrolSchemeModel,userId, startIndex, pageSize);
	}
	
}
