package com.xmsmartcity.maintain.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.xmsmartcity.maintain.dao.AbnormalRecordsMapper;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.EvaluateMapper;
import com.xmsmartcity.maintain.pojo.AbnormalRecords;
import com.xmsmartcity.maintain.pojo.Evaluate;
import com.xmsmartcity.maintain.service.EvaluateService;
import com.xmsmartcity.util.Constant;

@Service
public class EvaluateServiceImpl extends BaseServiceImpl<Evaluate> implements EvaluateService {

	public EvaluateServiceImpl(BaseDao<Evaluate> dao) {
		super(dao);
	}

	@Autowired
	private EvaluateMapper EvaluateDao;
	
	@Autowired
	private AbnormalRecordsMapper AbnormalRecordsDao;
	
	@Override
	public List<Map<String, Object>> getAbnormalThings(Integer userId) {
		// TODO Auto-generated method stub
		return EvaluateDao.getAbnormalThings(userId);
	}

	@Override
	public List<Map<String, Object>> getAbnormalByTimeout(Integer userId) {
		// TODO Auto-generated method stub
		return EvaluateDao.getAbnormalByTimeout(userId);
	}

	@Override
	public List<Map<String, Object>> getAbnormalByScore(Integer userId) {
		// TODO Auto-generated method stub
		return EvaluateDao.getAbnormalByScore(userId);
	}

	@Override
	public List<Map<String, Object>> getAbnormalRecords(Integer userId, Integer startIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return AbnormalRecordsDao.getAbnormalRecords(userId, startIndex, pageSize);
	}

	@Override
	public int insertAbnormalRecords(AbnormalRecords record,Integer userId) {
		// TODO Auto-generated method stub
		AbnormalRecords abnormalRecords=AbnormalRecordsDao.selectByRecord(record.getReferId(),userId);
		Assert.state(abnormalRecords ==null, "该异常事项已处理。");
		Assert.state(record.getReferId()!=null, Constant.PARAMS_ERROR);
		record.setIsDel(0);
		return AbnormalRecordsDao.insert(record);
	}

}
