package com.xmsmartcity.maintain.dao;

import java.util.List;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecordItem;

public interface PatrolSchemeRecordItemMapper extends BaseDao<PatrolSchemeRecordItem> {
	/**
	 * 批量保存巡检记录明细
	 * @param patrolSchemeItems
	 */
	void insertBatch(List<PatrolSchemeRecordItem> patrolSchemeRecordItems);
	
	/**
	 * 根据巡检记录ID删除明细
	 * @param id
	 */
	void deleteByPatrolSchemeRecordId(Integer id);
	
	/**
	 * 根据巡检记录查询明细
	 * @param record
	 * @return
	 */
	List<PatrolSchemeRecordItem> selectByRecord(PatrolSchemeRecordItem record);
}