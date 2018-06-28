package com.xmsmartcity.maintain.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xmsmartcity.orm.Pagination;
import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.FaultSummary;

public interface FaultSummaryMapper extends BaseDao<FaultSummary> {
	/**
	 * 
	 * @param userId
	 * @param receivetimeStart
	 * @param receivetimeEnd
	 * @param createtimeStart
	 * @param createtimeEnd
	 * @param type
	 *            汇总类型 0 报障 1为维保
	 * @return
	 */
	public List<FaultSummary> selectFaultList(@Param("userId") Integer userId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	/**
	 * 获取汇总数据
	 * @param userId
	 * @param type
	 * @return
	 */
	public Map<String, Object> selectSummaryNum(@Param("userId") Integer userId, @Param("type") Integer type);

	/**
	 * 获取报修方汇总数据
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public Map<String, Integer> selectFaultSummaryNum(@Param("userId") Integer userId,@Param("companyId") Integer companyId);
	
	public Map<String, Long> selectMaintainSummaryNum(@Param("userId") Integer userId,@Param("companyId") Integer companyId);

	public Integer selectProjectSummaryNum(@Param("userId") Integer userId,@Param("companyId") Integer companyId);
	
	/**
	 * 人员汇总
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	public List<FaultSummary> selectStatisticsByUser(@Param("userId") Integer userId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);
	
	/**
	 * 下载Excel（项目下载）
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @param projectId
	 * @return
	 */
	public List<Map<String, Object>> excelByProjectId(@Param("userId") Integer userId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type,@Param("projectId") Integer projectId);
	
	/**
	 * 下载Excel（人员下载）
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> excelByUserId(@Param("userId") Integer userId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("type") Integer type);

	List<Map<String,Object>> searchOverallData(@Param("userId") Integer userId,@Param("content") String searchContent,@Param("type") Integer type, Pagination<Map<String, Object>> mapPagination);
}