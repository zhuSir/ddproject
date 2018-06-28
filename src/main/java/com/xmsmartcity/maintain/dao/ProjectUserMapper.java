package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.ProjectUser;

public interface ProjectUserMapper extends BaseDao<ProjectUser>{

    /**
     * 获取某个项目成员的信息
     * @param projectId
     * @param userId
     * @return
     */
	ProjectUser selectByUserIdAndProjectId(@Param("projectId")Integer projectId,@Param("userId") Integer userId,@Param("type")Integer type,@Param("isLeader")Integer isLeader);
	
	/**
	 * 根据参数查询项目成员
	 * @author:zhugw
	 * @time:2017年4月13日 下午5:11:05
	 * @param projectId
	 * @param userId
	 * @param type
	 * @param isLeader
	 * @param serviceTypeId
	 * @return
	 */
	ProjectUser selectByParames(
			@Param("projectId")Integer projectId,
			@Param("userId") Integer userId,
			@Param("type")Integer type,
			@Param("isLeader")Integer isLeader,
			@Param("serviceTypeId")Integer serviceTypeId);
	
	/**
	 * 根据参数回去数据列表 
	 * @author:zhugw
	 * @time:2017年4月16日 下午3:48:27
	 * @param projectId
	 * @param userId
	 * @param type
	 * @param isLeader
	 * @param serviceTypeId
	 * @return
	 */
	List<ProjectUser> selectListByParames(
			@Param("projectId")Integer projectId,
			@Param("userId") Integer userId,
			@Param("type")Integer type,
			@Param("isLeader")Integer isLeader,
			@Param("serviceTypeId")Integer serviceTypeId);

	List<ProjectUser> selectByCompanyPrincipal(
			@Param("projectId")Integer projectId,
			@Param("userId") Integer userId,
			@Param("type")Integer type,
			@Param("companyId")Integer companyId);

}