package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.PatrolScheme;
import org.springframework.stereotype.Repository;

@Repository
public interface PatrolSchemeMapper extends BaseDao<PatrolScheme> {
	
	/**
	 * 插入巡检计划，并返回数据
	 * @param record
	 * @return
	 */
	int insertBackEntity(PatrolScheme record);
	
	/**
	 * 根据巡检字段查找巡检计划，可分页
	 * @param record  巡检计划实体类
	 * @param startIndex 开始值
	 * @param pageSize 页码
	 * @return
	 */
	List<PatrolScheme> selectByRecord(@Param("record")PatrolScheme record,@Param("ids")String ids,@Param("startIndex") Integer startIndex,@Param("pageSize")Integer pageSize);

	/**
	 * 根据Ids获取巡检任务列表(巡检负责人、公司负责人权限)
	 * @param ids
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> selectListByIds(@Param("ids")String ids,@Param("userId")Integer userId,@Param("equipId")String equipId);

	/**
	 * 根据Ids获取巡检任务列表(公司成员权限)
	 * @param ids
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> selectListByIdsPermisson(@Param("ids")String ids,@Param("userId")Integer userId,@Param("equipId")String equipId);

	/**
	 * 根据Ids获取巡检任务列表
	 * （排除今日已生成的巡检记录） -
	 * @param ids
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> selectListByIdsNoToday(@Param("ids")String ids,@Param("userId")Integer userId,@Param("equipId")String equipId);

}