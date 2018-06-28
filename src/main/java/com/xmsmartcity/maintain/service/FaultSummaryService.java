package com.xmsmartcity.maintain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.FaultSummary;

public interface FaultSummaryService extends BaseService<FaultSummary>{
     /**
      * 获取维保汇总列表
      * @return
      */
	public List<FaultSummary> getmaintenanList(Integer userId,Date receivetimeStart,Date receivetimeEnd);
	/**
	 * 获取报障汇总列表
	 * @return
	 */
	public List<FaultSummary> getFaultList(Integer userId,Date startTime,Date endTime,String type);
	
	/**
	 * 获取首页维保汇总
	 * @param startTime
	 * @param endTime
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getSummaryNum(Integer userId,Integer type);
	
	/**
	 * 首页工作面板
	 * @time:2017年7月26日 上午10:37:29
	 * @param userId
	 * @return
	 */
	public Map<String,Object> selectHomeStatInfo(Integer userId);
	
	/**
	 * 人员汇总
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	public List<FaultSummary> getStatisticsByUser(Integer userId,Date startTime,Date endTime,String type);
	
	/**
	 * 项目汇总Excel下载
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @param projectId
	 * @return
	 */
	public List<Map<String, Object>> excelByProjectId(Integer userId,Date startTime,Date endTime,String type,Integer projectId);
	
	/**
	 * 人员汇总Excel下载
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> excelByUserId(Integer userId,Date startTime,Date endTime,String type);
	
}
