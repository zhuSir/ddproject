package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.MaintainOperateLog;

@Repository
public interface MaintainOperateLogMapper extends BaseDao<MaintainOperateLog>{
    
    List<MaintainOperateLog> selectByFaultInfoId(@Param("faultInfoId")Integer faultInfoId,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);

    Integer selectOperateNumByUserId(Integer userId);
}