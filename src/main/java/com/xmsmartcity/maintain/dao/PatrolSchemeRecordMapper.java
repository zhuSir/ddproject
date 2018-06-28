package com.xmsmartcity.maintain.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.PatrolSchemeRecord;

public interface PatrolSchemeRecordMapper extends BaseDao<PatrolSchemeRecord> {
	
	/**
	 * 查询巡检记录（巡检负责人、公司负责人）
	 * @param record
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @param userId
	 * @return
	 */
    List<PatrolSchemeRecord> selectByRecord(@Param("record")PatrolSchemeRecord record,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("startIndex") Integer startIndex,@Param("pageSize")Integer pageSize,@Param("userId")Integer userId);
    
    /**
     *  查询巡检记录（公司人员权限)
     * @param record
     * @param startTime
     * @param endTime
     * @param startIndex
     * @param pageSize
     * @param userId
     * @return
     */
    List<PatrolSchemeRecord> selectByRecordPermisson(@Param("record")PatrolSchemeRecord record,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("startIndex") Integer startIndex,@Param("pageSize")Integer pageSize,@Param("userId")Integer userId);
    
    
    List<PatrolSchemeRecord> selectByState(@Param("state")Integer state,@Param("userId")Integer userId,@Param("equipId")String equipId);
    
    Map<String, Object> selectSummarySum(@Param("userId")Integer userId,@Param("equipId")String equipId);
}