package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.PatrolSchemeModel;

public interface PatrolSchemeModelMapper extends BaseDao<PatrolSchemeModel> {
	/**
	 * 插入数据  并返回数据
	 * @param record
	 * @return
	 */
	int insertBackEntity(PatrolSchemeModel record);
	
	/**
	 * 根据巡检模板字段查询
	 * @param record
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<PatrolSchemeModel> selectByRecord(@Param("record")PatrolSchemeModel record,@Param("userId")Integer userId,@Param("startIndex") Integer startIndex,@Param("pageSize")Integer pageSize);
}