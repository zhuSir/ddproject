package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.File;

public interface FileMapper extends BaseDao<File> {
    
    List<File> selectByRecord(@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize,@Param("userId") Integer userId,@Param("file")File file);
        
}