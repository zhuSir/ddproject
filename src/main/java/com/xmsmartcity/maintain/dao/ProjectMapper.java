package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.Project;

@Repository
public interface ProjectMapper extends BaseDao<Project>{

    int updateByPrimaryKeyWithBLOBs(Project record);

    List<Project> selectByName(@Param("companyId") Integer companyId,@Param("projectName")String projectName,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);

    /**
     * 根据用户id获取相关联项目
     * @author:zhugw
     * @time:2017年4月11日 下午7:03:23
     * @param userId
     * @param startIndex
     * @param pageSize
     * @return
     */
	List<Project> selectProjectList(@Param("userId")Integer userId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);

	/**
	 * 根据经纬度获取范围内项目
	 * @author:zhugw
	 * @time:2017年4月24日 上午10:49:05
	 * @param userId
	 * @param lf_down_lng
	 * @param lf_down_lat
	 * @param rg_up_lng
	 * @param rg_up_lat
	 * @return
	 */
	List<Project> selectProjectListByPosition(@Param("userId")Integer userId,@Param("lf_down_lng") Double lf_down_lng,@Param("lf_down_lat") Double lf_down_lat,@Param("rg_up_lng") Double rg_up_lng,@Param("rg_up_lat")Double rg_up_lat);
	
	/**
	 * 首页获取附近项目
	 * @author:zhugw
	 * @time:2017年5月16日 下午4:21:48
	 * @param userId
	 * @param lf_down_lng
	 * @param lf_down_lat
	 * @param rg_up_lng
	 * @param rg_up_lat
	 * @return
	 */
	List<Project> selectByPosition(@Param("userId")Integer userId,@Param("lf_down_lng") Double lf_down_lng,@Param("lf_down_lat") Double lf_down_lat,@Param("rg_up_lng") Double rg_up_lng,@Param("rg_up_lat")Double rg_up_lat);

	/**
	 * 获取团队相关项目
	 * @author:zhugw
	 * @time:2017年5月8日 上午10:00:09
	 * @param projectName
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Project> selectCompanyProject(@Param("userId") Integer userId,@Param("projectName") String projectName,@Param("companyId") Integer companyId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);

	/**
	 * 获取项目维保概览
	 * @author:zhugw
	 * @time:2017年5月9日 下午7:18:56
	 * @param projectId
	 * @return
	 */
	Map<String, Object> selectCollect(Integer projectId);

	Integer selectCount();

	/**
	 * 获取我的团队项目分页
	 * @author:zhugw
	 * @time:2017年5月24日 下午2:42:04
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Project> selectListByCompanyId(@Param("companyId")Integer companyId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);
	
	Project selectCountNum(Integer projectId);

	List<Project> selectByOwnerPhone(String phone);

	/**
	 * 团队负责人查询所有相关项目
	 * @time:2017年7月7日 下午5:46:33
	 * @param id
	 * @param userId
	 * @return
	 */
	List<Project> selectByCompanyPrincipal(@Param("companyId")Integer companyId,@Param("userId") Integer userId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);

	Map<String, Object> selectCollectByParam(@Param("userId")Integer userId,@Param("projectId") Integer projectId);
	
	/**
	 * 根据用户ID查询是否第三方报障（直接报障）
	 * @param projectId
	 * @param userId
	 * @return
	 */
	Integer getFaultType(@Param("projectId")Integer projectId,@Param("userId")Integer userId);

	/*
	 * web项目页面
	 */
	List<Map<String,Object>> selectListByUserId(@Param("userId") Integer userId,@Param("companyId") Integer companyId,RowBounds rowBounds);

	/**
	 * 分页搜索项目列表
	 * @author:zhugw
	 * @time:2017年11月29日 上午10:12:01
	 * @param userId
	 * @param projectName
	 * @param companyId
	 * @param rowBounds
	 * @return
	 */
	List<Project> searchCompanyProjectByPage(@Param("userId")Integer userId,@Param("projectName") String projectName,@Param("companyId") Integer companyId,
			RowBounds rowBounds);


	/**
	 * 根据项目名称搜索项目列表
	 * @author:zhugw
	 * @time:2017年12月1日 上午9:02:37
	 * @param company
	 * @param userId
	 * @param projectName
	 * @param rowBounds
	 * @return
	 */
	List<Map<String, Object>> selectProjectListByPage(@Param("companyId")Integer company,@Param("userId") Integer userId,@Param("projectName") String projectName, RowBounds rowBounds);
	
	/**
	 * 根据项目名称搜索项目
	 * @author:zhugw
	 * @time:2017年12月4日 下午4:23:21
	 * @param projectName
	 * @param companyId
	 * @param type
	 * @return
	 */
    Map<String,Object> selectProjectByName(@Param("projectName")String projectName,@Param("companyId") Integer companyId,@Param("type") Integer type);
}