package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.IndexUserFault;

@Repository
public interface IndexUserFaultMapper extends BaseDao<IndexUserFault>{

    /*
     * 获取报障参与人员
     */
	IndexUserFault selectByParams(@Param("userId")Integer userId,@Param("faultInfoId")Integer faultInfoId,@Param("type") Integer type,@Param("isDel") Integer isDel);
	
	/**
	 * 获取报障参与人员
	 * @author:zhugw
	 * @time:2017年4月28日 下午2:26:23
	 * @param faultInfoId
	 * @param type
	 * @param isDel
	 * @return
	 */
	List<IndexUserFault> selectListByParams(@Param("faultInfoId")Integer faultInfoId,@Param("type") Integer type,@Param("isDel") Integer isDel);

	/*
	 * 根据type排序获取对象
	 */
	IndexUserFault selectLastObj(Integer faultInfoId);

	Integer selectMaintainCount(Integer userId);

	/**
	 * 获取报障单维修人员
	 * @author:zhugw
	 * @time:2017年5月24日 下午2:51:11
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<IndexUserFault> selectListByUserId(@Param("userId")Integer userId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);
}