package com.xmsmartcity.maintain.dao;

import java.util.List;

import com.xmsmartcity.maintain.pojo.PatrolSchemeItem;

public interface PatrolSchemeItemMapper extends BaseDao<PatrolSchemeItem> {
	
	/**
	 * 批量保存巡检明细
	 * @param patrolSchemeItems
	 */
	void insertBatch(List<PatrolSchemeItem> patrolSchemeItems);
	
	/**
	 * 查询item
	 * @param patrolSchemeItem
	 * @return
	 */
	List<PatrolSchemeItem> selectByRecord(PatrolSchemeItem patrolSchemeItem);
	
	/**
	 * 根据巡检主表ID删除明细
	 * @param patrolSchemeId
	 */
	void deleteByPatrolSchemeId(Integer patrolSchemeId);
}