package com.xmsmartcity.maintain.dao;

import java.util.List;

import com.xmsmartcity.maintain.pojo.SchemeItemEquip;

public interface SchemeItemEquipMapper extends BaseDao<SchemeItemEquip> {

	/**
	 * 批量保存巡检明细设备关联
	 * @param patrolSchemeItems
	 */
	void insertBatch(List<SchemeItemEquip> schemeItemEquips);
	
	
	/**
	 * 根据patrolSchemeItemId删除关联
	 * @param patrolSchemeItemId
	 */
	void deleteByPatrolSchemeItemId(Integer patrolSchemeItemId);
	
}