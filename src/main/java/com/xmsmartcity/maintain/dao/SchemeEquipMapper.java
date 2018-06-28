package com.xmsmartcity.maintain.dao;

import java.util.List;

import com.xmsmartcity.maintain.pojo.SchemeEquip;

public interface SchemeEquipMapper extends BaseDao<SchemeEquip> {
	
	/**
	 * 批量保存巡检设备关联
	 * @param patrolSchemeItems
	 */
	void insertBatch(List<SchemeEquip> schemeEquips);
	
	/**
	 * 根据patrolSchemeId删除关联
	 * @param patrolSchemeId
	 */
	void deleteByPatrolSchemeId(Integer patrolSchemeId);
	
}