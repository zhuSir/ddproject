package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.AbnormalRecords;

public interface AbnormalRecordsMapper extends BaseDao<AbnormalRecords> {
    
	/**
	 * 获取异常事项列表
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
    List<Map<String, Object>> getAbnormalRecords(@Param("userId")Integer userId,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
    
    /**
     * 获取异常详情
     * @param referId
     * @param userId
     * @return
     */
    AbnormalRecords selectByRecord(@Param("referId")Integer referId,@Param("userId")Integer userId);
}