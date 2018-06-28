package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.EquipsType;
import org.apache.ibatis.session.RowBounds;

public interface EquipsTypeMapper extends BaseDao<EquipsType>{

    /**
     * 获取设备类别列表
     * @param startIndex
     * @param pageSize
     * @param name
     * @return
     */
    List<EquipsType> selectEquipsTypeList(@Param("userId") Integer userId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize,@Param("name")String name);

    /**
     * 获取设备类别列表（web分页加载）
     * @param userId
     * @param name
     * @param code
     * @param rowBounds
     * @return
     */
    List<Map<String, Object>> selectEquipsTypeListByPage(@Param("userId") Integer userId, @Param("name")String name, @Param("code")String code, RowBounds rowBounds);

    /**
     * 批量插入设备类别
     * @param equipsTypes
     */
    void insertBatch(List<EquipsType> equipsTypes);

    /**
     * 根据名称查询设备类别（多个以逗号隔开）
     * @param names
     * @return
     */
    List<EquipsType> selectEquipsTypeListByNames(String names);
}