package com.xmsmartcity.maintain.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.xmsmartcity.maintain.pojo.Evaluate;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.MaintainInfo;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.pojo.WorkOrder;

public interface FaultInfoService extends BaseService<FaultInfo>{

	/**
	 * 获取报障列表
	 * 
	 * @param state
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<FaultInfo> getFaultInfoList(Integer state, Integer userId,Integer type, Integer startIndex,
			Integer pageSize);

	/**
	 * 添加报障信息
	 * 
	 * @param faultInfo
	 * @return
	 */
	public FaultInfo saveFaultInfo(FaultInfo faultInfo) throws IOException;

	/**
	 * 获取报障单详情
	 * 
	 * @author:zhugw
	 * @time:2017年4月24日 下午8:21:14
	 * @param faultInfoId
	 * @return
	 */
	public FaultInfo getFaultInfoDetailById(Integer faultInfoId,Integer userId);

	/**
	 * 获取维修列表
	 * 
	 * @author:zhugw
	 * @time:2017年4月25日 下午3:27:49
	 * @param faultInfoId
	 * @return
	 */
	public List<MaintainInfo> getMaintainInfoList(Integer faultInfoId,Integer startIndex,Integer pageSize);

	/**
	 * 保存工单及费用
	 * 
	 * @author:zhugw
	 * @time:2017年4月25日 下午3:33:00
	 * @param materia
	 * @param wordOrder
	 */
	public void getMaterialLabourAndWorkOrder(WorkOrder wordOrder) throws IOException;

	/**
	 * 执行报障流程
	 * @author:zhugw
	 * @time:2017年5月3日 下午2:30:13
	 * @param faultInfoId  报障单id
	 * @param type	报障流程操作
	 * 		1待接收	0:乙方负责人接收分派报障单 1:乙方负责人接收报障单  2:乙方负责人退回报障单 
	 * 		3待处理	3:乙方负责人转派报障单  4:乙方处理报障单  5:乙方负责人拒绝转派  6:乙方负责人增援  7:乙方负责人拒绝增援
	 *      2已退回	8:甲方同意乙方退回  9:甲方重新发起报障单  
	 *      4已接收	10:乙方请求转派  11:填写工单 12:无需工单
	 *      5待确认	13:工单不通过 14:工单通过 
	 *      6维修中	15:请求增援 16:维修说明 17:完成维修
	 *      7待验收	18:验收合格 19:验收不合格  
	 *      8待评价	20:工单评价
	 * @param reason  理由
	 * @param reUserId  转派/增援 给谁
	 * @param userId	当前用户id
	 */
	public void faultInfoOperate(Integer faultInfoId, Integer type,String appointmentTime, String reason, String reUserId,String referUserPhone,String pics,Integer userId)throws IOException;

	/*
	 * 保存维修说明
	 */
	public void saveMaintainInfo(Integer userId, MaintainInfo maintainInfo) throws IOException;

	/*
	 * 保存评价
	 */
	public void saveEvaluate(Evaluate evaluate) throws IOException ;

	/*
	 * 获取查看现场日志
	 */
	public List<MaintainOperateLog> getMaintainOperateLogList(Integer faultInfoId,Integer startIndex,Integer pageSize);

	/**
	 * 获取报障列表 (维保超时待接收提醒)
	 * @param state
	 * @return
	 */
	public List<Map<String, Object>> selectListByToRecive();
	
	/**
	 * 获取报障列表 (维保超时待处理，已接收提醒)  type进行区分  0 为已接收  1,2分别为请求转派和增援
	 * @return
	 */
	public List<Map<String, Object>> selectListByRecive();

	/**
	 * 设置预约维保时间
	 * @author:zhugw
	 * @time:2017年5月11日 上午9:07:09
	 * @param appointmentTime
	 * @param faultInfoId
	 * @param userId
	 */
	public void setAppointmentTime(String appointmentTime, Integer faultInfoId, Integer userId);

	/**
	 * 获取评分信息
	 * @author:zhugw
	 * @time:2017年5月18日 下午5:06:41
	 * @param faultInfoId
	 * @return
	 */
	public Evaluate selectEvaluate(Integer faultInfoId);

	/**
	 * 获取报障详情相关
	 * @author:zhugw
	 * @time:2017年5月18日 下午6:42:30
	 * @param faultInfoId
	 * @param userId
	 * @return
	 */
	public Map<String, Object> selectFaultInfo(Integer faultInfoId, Integer userId);

	/**
	 * 查询费用清单
	 * @author:zhugw
	 * @time:2017年5月18日 下午7:08:35
	 * @param faultInfoId
	 * @param userId
	 * @return
	 */
	public Map<String,Object> selectWorkOrder(Integer faultInfoId, Integer userId);

	/**
	 * 获取报障库信息
	 * @author:zhugw
	 * @time:2017年5月18日 下午7:43:18
	 * @param faultInfoId
	 * @return
	 */
	public List<Map<String, Object>> selectFaultStoreList(Integer faultInfoId);

	/**
	 * 获取报障库
	 * @author:zhugw
	 * @time:2017年5月19日 上午9:33:07
	 * @param faultStoreId
	 * @return
	 */
	public Map<String,Object> selectFaultStoreDetail(Integer faultStoreId);

	/**
	 * 查询报障历史列表
	 * @author:zhugw
	 * @time:2017年5月19日 下午2:10:54
	 * @param state
	 * @param userId
	 * @param faultInfoId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public Map<String,Object> getFaultInfoHistoryList(Integer state, Integer userId, Integer faultInfoId,
			Integer startIndex, Integer pageSize);

	/**
	 * 根据类型状态获取报障单
	 * @author:zhugw
	 * @time:2017年5月27日 上午9:34:06
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<FaultInfo> getSumFaultInfoList(Integer faultState,Integer state, Integer userId, Integer type, Integer startIndex,
			Integer pageSize);
	
	/**
	 * 首页汇总进入报障维保列表
	 * @author:cby
	 * @param faultState
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<FaultInfo> getFaultInfoListByHome(Integer faultState, Integer userId, Integer type, Integer startIndex,
			Integer pageSize); 
	
	/**
	 * 设备进入报障维保列表
	 * @author:cby
	 * @param faultState
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<FaultInfo> getFaultInfoListByEquip(Integer faultState,Integer state, Integer userId, Integer startIndex,
			Integer pageSize,Integer equipId);
	
	/**
	 * 项目进入报障维保列表
	 * @author:cby
	 * @param faultState
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<FaultInfo> getFaultInfoListByProject(Integer faultState,Integer state, Integer userId, Integer type, Integer startIndex,
			Integer pageSize,Integer projectId);

	/**
	 * 获取维保人员列表
	 * @author:zhugw
	 * @time:2017年6月8日 下午2:48:26
	 * @param faultInfoId
	 * @param userId
	 * @return
	 */
	public List<User> getMaintainUserList(Integer faultInfoId, Integer userId);

	/**
	 * 团队负责人获取相关报障单列表
	 * @time:2017年7月10日 下午2:44:22
	 * @param state
	 * @param userId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public Map<String,Object> getcompanyPrincipalfaultList(Integer userId,Integer startIndex,Integer pageSize);

	
	/**
	 * 微信小程序新建报障信息（用户无团队）
	 * 
	 * @param faultInfo
	 * @return
	 */
	public FaultInfo saveXcxFaultInfo(FaultInfo faultInfo) throws IOException;
	
	/**
	 * 获取报障列表(小程序)
	 * 
	 * @param state
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<FaultInfo> getFaultInfoListByXcx(Integer state, Integer userId,Integer type, Integer startIndex,
			Integer pageSize);

	/**
	 * 故障统计（新增故障、维修完成）
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @return
	 */
	public List<FaultInfo> getFaultStatistics(Integer userId,Date startTime, Date endTime,Integer state);
	
	/**
	 *
	 *
     */
	public List<FaultInfo> getFaultListByPC(Integer userId,String time);
	
	/**
	 * 根据报修单ID和用户ID 查询是否可以验收和评价
	 * @param userId 用户ID
	 * @param id 报修单ID
	 * @param type 0:直接报修  1:第三方报修
	 * @return
	 */
	public int getIsOperate(Integer userId,Integer id,Integer type);

	/**
	 * 根据时间查询设备历史报障单
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:55:01
	 * @param startDate
	 * @param endDate
	 * @param equipId
	 * @return
	 */
    List<FaultInfo> selectListByEquipIdAndTime(Date startDate, Date endDate, Integer equipId);

	/**
	 * web 报障单列表(个人权限)
	 * @param projectId
	 * @param userId
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> selectWebFaultList(Integer projectId,Integer userId,String state,Date startTime,Date endTime,Integer pageNo,Integer pageSize);

	/**
	 * web 报障单列表(公司权限)
	 * @param projectId
	 * @param equipId
	 * @param userId
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> selectWebFaultListByEquip(Integer projectId,Integer equipId,Integer userId,String state,Date startTime,Date endTime,Integer pageNo,Integer pageSize);

	/**
	 * 报障维修说明
	 * @author:zhugw
	 * @time:2017年11月29日 下午1:56:04
	 * @param faultInfoId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Object getMaintainInfoByPage(Integer faultInfoId, Integer pageNum, Integer pageSize);

}
