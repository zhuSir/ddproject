package com.xmsmartcity.maintain.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.xmsmartcity.maintain.pojo.FaultInfo;

@Repository
public interface FaultInfoMapper extends BaseDao<FaultInfo>{

	/**
	 * 获取报修单列表（维保方）
	 * @param companyId
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectMaintainList(@Param("companyId") Integer companyId,@Param("state") Integer state, @Param("userId") Integer userId,
			@Param("type") Integer type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 获取报修单列表（报修方）
	 * @param companyId
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectFaultList(@Param("companyId") Integer companyId,@Param("state") Integer state, @Param("userId") Integer userId,
			@Param("type") Integer type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 根据ID获取报修单详情
	 * @param id
	 * @return
	 */
	FaultInfo selectFaultInfoById(Integer id);

	Integer checkUnfinished(@Param("userId") Integer userId, @Param("projectId") Integer projectId,
			@Param("serviceTypeId") Integer serviceTypeId);

	/**
	 * 获取报障列表 (维保超时待处理，已接收提醒)  type进行区分  0 为已接收  1,2分别为请求转派和增援
	 * @return
	 */
	List<Map<String, Object>> selectListByRecive();

	/**
	 * 获取报障列表 (维保超时待接收提醒)
	 * @return
	 */
	List<Map<String, Object>> selectListByToRecive();

	/**
	 * 项目所关联的未完成报障
	 * 
	 * @author:zhugw
	 * @time:2017年5月10日 上午9:35:18
	 * @param projectId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectFaultListForProject(@Param("projectId") Integer projectId, @Param("type") Integer type,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 获取团队最大工单号
	 * 
	 * @author:zhugw
	 * @time:2017年5月10日 下午4:41:20
	 * @param companyId
	 * @return
	 */
	String selectMaxCode(Integer companyId);

	/**
	 * 附近报障
	 * 
	 * @author:zhugw
	 * @time:2017年5月16日 下午4:40:26
	 * @param userId
	 * @param lf_down_lng
	 * @param lf_down_lat
	 * @param rg_up_lng
	 * @param rg_up_lat
	 * @return
	 */
	List<Map<String, Object>> selectByPosition(@Param("userId") Integer userId, @Param("lf_down_lng") Double lf_down_lng,
			@Param("lf_down_lat") Double lf_down_lat, @Param("rg_up_lng") Double rg_up_lng,
			@Param("rg_up_lat") Double rg_up_lat);
	
	/**
	 * 获取报障相关信息
	 * @author:zhugw
	 * @time:2017年5月18日 下午6:34:05
	 * @param faultInfoId
	 * @return
	 */
	Map<String, Object> selectByFaultCount(Integer faultInfoId);

	/**
	 * 根据ServiceTypeId查询
	 * @author:zhugw
	 * @time:2017年5月19日 下午2:12:51
	 * @param state
	 * @param serviceTypeId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectListByServiceTypeId(@Param("state")Integer state,@Param("faultInfoId") Integer faultInfoId,@Param("startIndex") Integer startIndex,
			@Param("pageSize")Integer pageSize);

	/**
	 * 获取
	 * @author:zhugw
	 * @time:2017年5月27日 上午9:43:21
	 * @param state
	 * @param type
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectListByStateType(@Param("faultState")Integer faultState,@Param("state")Integer state,@Param("type") Integer type,@Param("companyId") Integer companyId,@Param("startIndex") Integer startIndex,
			@Param("pageSize")Integer pageSize);
	
	/**
	 * 首页汇总进入报障维保列表
	 * @author:cby
	 * @param faultState
	 * @param type
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectListByHome(@Param("faultState")Integer faultState,@Param("type") Integer type,@Param("userId") Integer userId,@Param("startIndex") Integer startIndex,
			@Param("pageSize")Integer pageSize);
	
	/**
	 * 设备进入报障维保列表
	 * @author:cby 
	 * @param faultState
	 * @param type
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectListByEquip(@Param("faultState")Integer faultState,@Param("state")Integer state,@Param("userId") Integer userId,@Param("startIndex") Integer startIndex,
			@Param("pageSize")Integer pageSize,@Param("equipId")Integer equipId);
	
	/**
	 * 项目进入报障维保列表
	 * @author:cby 
	 * @param faultState
	 * @param type
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectListByProject(@Param("faultState")Integer faultState,@Param("state")Integer state,@Param("type") Integer type,@Param("userId") Integer userId,@Param("startIndex") Integer startIndex,
			@Param("pageSize")Integer pageSize,@Param("projectId")Integer projectId);

	List<FaultInfo> selectByFaultUserPhone(String phone);

	/**
	 * 获取团队负责人相关报障列表
	 * @time:2017年7月10日 下午2:46:36
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectFaultListByCompanyPrincipal(@Param("companyId")Integer companyId, @Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
	
	/**
	 * 根据报障code前缀获取最大报障单编号
	 * @param headCode
	 * @return
	 */
	int selectFaultCodeMax(@Param("headCode")String headCode);
	
	/**
	 * 获取报障列表(小程序)
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<FaultInfo> selectFaultListByXcx(@Param("state") Integer state, @Param("userId") Integer userId,
			@Param("type") Integer type, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	List<FaultInfo> selectFaultListForProjectByParam(@Param("projectId") Integer projectId,@Param("userId") Integer userId, @Param("type") Integer type,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
	
	/**
	 * 故障统计（新增故障、维修完成）
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FaultInfo> selectFaultStatistics(@Param("userId") Integer userId, @Param("startTime") Date startTime,@Param("endTime") Date endTime,@Param("state") Integer state);
	
	/**
	 * 根据报修单ID和用户ID 查询是否可以验收和评价
	 * @param userId 用户ID
	 * @param id 报修单ID
	 * @return
	 */
	int getIsOperate(@Param("userId") Integer userId,@Param("id") Integer id,@Param("type") Integer type);
	
	/**
	 * PC端打印报修单
	 * @param time
	 * @param companyId
	 * @return
	 */
	List<FaultInfo> selectFaultListByPC(@Param("time") Date time,@Param("companyId") Integer companyId);
	
	/*
	 * web 项目详情页面 历史故障列表
	 */
	List<Map<String, Object>> selectProjectHistoryList(@Param("projectId")Integer projectId, RowBounds rowBounds);

	/**
	 * 根据时间查询设备历史报障单
	 * @param startDate
	 * @param endDate
	 * @param equipId
	 * @return
	 */
    List<FaultInfo> selectListByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime,@Param("equipId") Integer equipId);

	/*
     * web 报障单列表(个人权限)
     */
	List<Map<String, Object>> selectWebFaultList(@Param("companyId")Integer companyId,@Param("projectId")Integer projectId,@Param("userId")Integer userId,@Param("state")String state,@Param("startTime") Date startTime, @Param("endTime") Date endTime, RowBounds rowBounds);

	/*
     * web 报障单列表（公司权限）
     */
	List<Map<String, Object>> selectWebFaultListByEquip(@Param("projectId")Integer projectId,@Param("equipId")Integer equipId,@Param("userId")Integer userId,@Param("state")String state,@Param("startTime") Date startTime, @Param("endTime") Date endTime, RowBounds rowBounds);

}