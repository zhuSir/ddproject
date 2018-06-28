package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.SysType;

public interface SysTypeMapper extends BaseDao<SysType>{
    List<SysType> selectByRecord(@Param("record")SysType record,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
}