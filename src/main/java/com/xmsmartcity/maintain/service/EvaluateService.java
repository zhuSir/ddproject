package com.xmsmartcity.maintain.service;

import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.AbnormalRecords;
import com.xmsmartcity.maintain.pojo.Evaluate;

public interface EvaluateService extends BaseService<Evaluate>{
	
	/**
	 * 根据用户ID获取首页异常事项
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getAbnormalThings(Integer userId);
	/**
	 * 维保超时异常事项
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getAbnormalByTimeout(Integer userId);
	/**
	 * 首页评分异常
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getAbnormalByScore(Integer userId);
	
	/**
	 * 已处理异常事项列表
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> getAbnormalRecords(Integer userId,Integer startIndex,Integer pageSize);
	
	/**
	 * 插入已处理异常事项记录
	 * @param record
	 * @return
	 */
	int insertAbnormalRecords(AbnormalRecords record,Integer userId);
}
