package com.xmsmartcity.maintain.dao;

import java.util.List;

import com.xmsmartcity.maintain.pojo.SchemeRecordItemEquip;

public interface SchemeRecordItemEquipMapper extends BaseDao<SchemeRecordItemEquip>{
	
	/**
	 * 批量保存巡检记录明细关联
	 * @param schemeRecordItemEquips
	 */
	void insertBatch(List<SchemeRecordItemEquip> schemeRecordItemEquips);
	
	/**
	 * 根据patrolSchemeRecordItemId删除关联
	 * @param patrolSchemeRecordItemId
	 */
	void deleteByPatrolSchemeRecordItemId(Integer patrolSchemeRecordItemId);
}