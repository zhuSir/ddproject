package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmsmartcity.maintain.pojo.FileType;

public interface FileTypeMapper extends BaseDao<FileType>{
    
    List<FileType> selectFileType(@Param("userId") Integer userId,@Param("id") Integer id,@Param("fileTypeName")String fileTypeName,@Param("fileTypeCode")String fileTypeCode);
    
}