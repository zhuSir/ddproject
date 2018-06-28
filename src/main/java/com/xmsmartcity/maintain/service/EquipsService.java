package com.xmsmartcity.maintain.service;

import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.Equips;
import com.xmsmartcity.maintain.pojo.EquipsType;

public interface EquipsService extends BaseService<Equips>{

	/**
	 * 创建或更新设备
	 * @param equips
	 * @return
	 */
	public Equips saveOrUpdateEquips(Equips equips);
	
	/**
	 * 获取设备列表
	 * @param startIndex
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public List<Equips> getEquipsList(Integer startIndex,Integer pageSize,Equips equips,Integer userId);
	/**
	 * 删除设备
	 * @param equips
	 */
	public void deleteEquips(Equips equips);
	/**
	 * 获取设备类型列表
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<EquipsType> selectEquipsTypeList(Integer userId,Integer startIndex,Integer pageSize,String name);
	
	/**
	 * 通过ID查找设备
	 */
	public Equips selectByPrimaryKey(Integer id);
	
	/**
	 * 通过设备查找项目
	 * @param id
	 * @param userId
	 * @return
	 */
	public Map<String, Object> selectProjectByEquip(Integer id,Integer userId);
	
	/**
	 * 设备概览处理中
	 * @param userId
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getProcessingByEquip(Integer userId,Integer id,Integer startIndex, Integer pageSize);
	
	/**
	 * 设备概览数量统计
	 * @param userId
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSummaryByEquip(Integer userId,Integer id);
	
	/**
	 * 获取设备评分
	 * @param id
	 * @return
	 */
	public Integer getEquipsAvg(Integer id);
	
	/**
	 * 获取设备维修时长
	 * @param id
	 * @return
	 */
	public Integer getEquipsSumTime(Integer id);
	
	/**
	 * 根据code查询设备情况
	 * @param equipsCode
	 * @return
	 */
	public Map<String, Object>getServiceByEquipCode(String equipCode);

	/**
	 * web 设备列表页面
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Object selectList(Integer userId,Integer projectId, Integer pageNo, Integer pageSize);

	/**
	 * 批量更新设备信息
	 * @author:zhugw
	 * @time:2017年11月17日 下午2:48:34
	 * @param equipIds
	 * @param projectId
	 * @return
	 */
	public void updateByPrimaryKeyBatch(String equipIds, Integer projectId);
	
	/**
	 * 删除设备列表
	 * @param equipsType
	 */
	public void deleteEquips(EquipsType equipsType);
	
	/**
	 * 更新设备类别
	 * @param equipsType
	 * @param userId
	 * @return
	 */
	public EquipsType saveOrUpdateEquipsType(EquipsType equipsType,Integer userId);

	/**
	 * 取消设备关联项目
	 * @param userId
	 * @param equipId
	 * @param projectId
	 * @return
	 */
    public void cancelJoinProject(Integer userId, Integer equipId, Integer projectId);

	/**
	 * 获取设备类别列表（web分页加载）
	 * @param userId
	 * @param name
	 * @param code
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    public Object selectEquipsTypeListByPage(Integer userId,String name,String code, Integer pageNo, Integer pageSize);

	/**
	 * 删除设备类别
	 * @param id
	 */
	public void deleteEquipsType(Integer id);

	/**
	 * 获取下一个编码
	 * @param PYname
	 * @return
	 */
	public Integer getNewCode(String PYname);

	/**
	 * 批量插入设备
	 * @param equips
	 */
	public void insertEquipBatch(List<Equips> equips);

	/**
	 * 批量插入设备类别
	 * @param equipsTypes
	 */
	public void insertEquipTypeBatch(List<EquipsType> equipsTypes);

	/**
	 * 根据名称查询设备类别（多个以逗号隔开）
	 * @param names
	 * @return
	 */
	public List<EquipsType> getEquipTypeListByName(String names);
	
	/**
	 * 查询关联设备
	 * @author:zhugw
	 * @param equipName 
	 * @time:2017年12月6日 上午9:28:45
	 * @param userId
	 * @param projectId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Object selectListByRelevance(String equipName, Integer userId, Integer projectId, Integer pageNo, Integer pageSize);
	
	/**
	 * 根据服务ID获取设备列表
	 * @param serviceTypeId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<Equips> getByServiceTypeId(Integer serviceTypeId, Integer startIndex, Integer pageSize);
	
	/**
	 * 获取设备列表（通过搜索）
	 * @param equipName
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<Equips> listBySearch(String equipName,Integer serviceTypeId,Integer startIndex, Integer pageSize);
}
