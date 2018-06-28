package com.xmsmartcity.maintain.service;

import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.PatrolScheme;

public interface PatrolSchemeService extends BaseService<PatrolScheme>{
	
	/**
	 * 保存和更新巡检计划
	 * @param patrolScheme
	 * @return
	 */
	PatrolScheme saveOrUpdate(PatrolScheme patrolScheme);
	
	/**
	 * 获取巡检列表
	 * @param patrolScheme 巡检字段
	 * @param ids  巡检id集合，以逗号隔开
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<PatrolScheme> getPatrolSchemeList(PatrolScheme patrolScheme,String ids,Integer startIndex,Integer pageSize);
	
	/**
	 * 获取巡检列表通过id集合(带权限)
	 * @param ids 巡检id集合，以逗号隔开
	 * @param userId  用户ID
	 * @param equipId 设备ID
	 * @return
	 */
	List<Map<String, Object>> getPatrolSchemeListByIds(String ids,Integer userId,String equipId);
}
