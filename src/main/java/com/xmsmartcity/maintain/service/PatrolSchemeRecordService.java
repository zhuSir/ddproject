package com.xmsmartcity.maintain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.PatrolSchemeRecord;

public interface PatrolSchemeRecordService extends BaseService<PatrolSchemeRecord>{
	
	public List<PatrolSchemeRecord> getListByRecord(PatrolSchemeRecord record,Date startTime,Date endTime,Integer userId,Integer startIndex,Integer pageSize);

	public List<PatrolSchemeRecord> getListByRecordPermisson(PatrolSchemeRecord record,Date startTime,Date endTime,Integer userId,Integer startIndex,Integer pageSize);

	public List<PatrolSchemeRecord> getListByState(Integer state,Integer userId,String equipId);
	
	public PatrolSchemeRecord saveOrUpdate(Integer userId,PatrolSchemeRecord patrolSchemeRecord,String recordItem);
	
	public Map<String, Object> getSummarySum(Integer userId,String equipId);
}
