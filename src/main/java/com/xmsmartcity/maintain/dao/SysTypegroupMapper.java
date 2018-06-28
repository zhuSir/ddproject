package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.SysTypegroup;

public interface SysTypegroupMapper extends BaseDao<SysTypegroup> {
    
	List<SysTypegroup> selectByRecord(@Param("record")SysTypegroup record,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
}