package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.xmsmartcity.maintain.pojo.Equips;

public interface EquipsMapper extends BaseDao<Equips>{
	
    /**
     * 根据设备字段删除设备
     * @param record
     * @return
     */
    int deleteById(Equips record);
    
    /**
     * 获取设备列表
     * @param startIndex
     * @param pageSize
     * @param name
     * @param serviceTypeId
     * @param id
     * @param userId
     * @return
     */
    List<Equips> getEquipsList(@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize,@Param("record")Equips record,@Param("userId")Integer userId);
    
    /**
     * 根据公司名称拼音简称查找最大设备编码
     * @param headCode
     * @return
     */
    String selectEquipCodeMax(@Param("headCode")String headCode);
    
    /**
     * 获取项目列表通过设备
     * @param id
     * @param userId
     * @return
     */
    Map<String, Object> selectProjectByEquip(@Param("id")Integer id,@Param("userId")Integer userId);
    
    /**
     * 设备概览处理中
     * @param userId
     * @param id
     * @return
     */
    List<Map<String, Object>> getProcessingByEquip(@Param("userId")Integer userId,@Param("id")Integer id,@Param("startIndex") Integer startIndex,@Param("pageSize")Integer pageSize);
    
    /**
     * 设备概览数量统计
     * @param userId
     * @param id
     * @return
     */
    Map<String, Object> getSummaryByEquip(@Param("userId")Integer userId,@Param("id")Integer id);
    
    /**
     * 获取设备评分
     * @param id
     * @return
     */
    Integer getEquipsAvg(Integer id);
    
    /**
     * 获取设备维修时长
     * @param id
     * @return
     */
    Integer getEquipsSumTime(Integer id);
    
    /**
     *  根据code查询设备情况
     * @param equipCode
     * @return
     */
    Map<String, Object> getServiceByEquipCode(@Param("equipCode")String equipCode);
    
    /**
     * 根据设备ids查询设备列表
     * @param ids
     * @return
     */
    List<Equips> selectByIds(@Param("ids")String ids);

    /**
     * web 设备列表
     * @param userId
     * @param rowBounds
     * @return
     */
	List<Map<String, Object>> selectListByPage(@Param("userId") Integer userId,@Param("projectId")Integer project, RowBounds rowBounds);

	/**
	 * 批量设备关联项目
	 * @author:zhugw
	 * @time:2017年11月17日 下午2:54:34
	 * @param projectId
	 * @param equipId
	 */
	void updateByPrimaryKeyBatch(@Param("serviceTypeId")Integer serviceTypeId,@Param("list") List<String> equipId);

    /**
     * 根据参数查询设备对象
     * @param userId
     * @param equipId
     * @param serviceTypeId
     * @return
     */
	Equips selectByParams(@Param("userId") Integer userId,@Param("equipId") Integer equipId,@Param("serviceTypeId") Integer serviceTypeId);

	/**
	 * 批量插入设备
	 * @param equips
	 */
	void insertBatch(List<Equips> equips);
	
	/**
	 * 取消关联项目（设备类型ID）
	 * @param equipTypeId
	 */
	void unJoinByEquipTypeId(Integer equipTypeId);

	/**
	 * 查询关联设备
	 * @author:zhugw
	 * @param equipName 
	 * @time:2017年12月6日 上午9:39:13
	 * @param companyId
	 * @param projectId
	 * @param rowBounds
	 * @return
	 */
	List<Map<String, Object>> selectListByRelevance(@Param("equipName")String equipName, @Param("companyId")Integer companyId,@Param("projectId") Integer projectId,RowBounds rowBounds);
	
	/**
	 * 获取设备列表
	 * @param serviceTypeId
	 * @param startIndex
	 * @param pageSize
	 */
	 List<Equips> selectByServiceTypeId(@Param("serviceTypeId")Integer serviceTypeId,@Param("startIndex") Integer startIndex,@Param("pageSize")Integer pageSize);

	 /**
	  * 获取设备列表（通过搜索）
	  * @param equipName
	  * @param startIndex
	  * @param pageSize
	  * @return
	  */
	 List<Equips> listBySearch(@Param("equipName")String equipName,@Param("serviceTypeId")Integer serviceTypeId,@Param("startIndex") Integer startIndex,@Param("pageSize")Integer pageSize);
}
