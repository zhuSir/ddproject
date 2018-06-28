package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.Evaluate;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluateMapper extends BaseDao<Evaluate>{
    
	/**
	 * 获取异常事项列表
	 * @param userId
	 * @return
	 */
    List<Map<String, Object>> getAbnormalThings(@Param("userId")Integer userId);
    
    /**
     * 获取超时异常
     * @param userId
     * @return
     */
    List<Map<String, Object>> getAbnormalByTimeout(@Param("userId")Integer userId);
    
    /**
     * 获取评分异常
     * @param userId
     * @return
     */
    List<Map<String, Object>> getAbnormalByScore(@Param("userId")Integer userId);
    
    /**
     * 获取异常详情
     * @param record
     * @return
     */
    Evaluate selectByRecord(Evaluate record);

    /**
     * 获取异常详情（报障单ID）
     * @param faultInfoId
     * @return
     */
	Evaluate selectByFaultInfoId(Integer faultInfoId);

	/**
	 * 统计工单评价
	 * @author:zhugw
	 * @time:2017年5月24日 上午10:31:44
	 * @param otherUserId
	 * @return
	 */
	Integer selectAverageNumByFaultUserId(Integer otherUserId);
}