package com.xmsmartcity.maintain.dao;

import java.util.List;
import com.xmsmartcity.maintain.pojo.PatrolSchemeModelItem;

public interface PatrolSchemeModelItemMapper extends BaseDao<PatrolSchemeModelItem> {
	/**
	 * 批量保存巡检模板明细
	 * @param patrolSchemeItems
	 */
	void insertBatch(List<PatrolSchemeModelItem> patrolSchemeModelItems);
	
	/**
	 * 根据巡检模板ID删除明细
	 * @param patrolSchemeId
	 */
	void deleteByPatrolSchemeModelId(Integer patrolSchemeModelId);
	
	/**
	 * 根据字段查询巡检模板明细
	 * @param patrolSchemeModelItem
	 * @return
	 */
	List<PatrolSchemeModelItem> selectByRecord(PatrolSchemeModelItem patrolSchemeModelItem);
}