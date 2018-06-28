package com.xmsmartcity.maintain.service;

import java.util.List;

import com.xmsmartcity.maintain.pojo.PatrolSchemeRecordItem;

public interface PatrolSchemeRecordItemService extends BaseService<PatrolSchemeRecordItem>{
	
	/**
	 * 根据巡检明细字段查询明细
	 * @param record
	 * @return
	 */
	List<PatrolSchemeRecordItem> selectByRecord(PatrolSchemeRecordItem record);
}
