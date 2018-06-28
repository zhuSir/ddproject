package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.MaintainInfo;

@Repository
public interface MaintainInfoMapper extends BaseDao<MaintainInfo>{

	List<MaintainInfo> selectByFaultInfoId(@Param("faultInfoId")Integer faultInfoId,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);

	List<MaintainInfo> selectByFaultInfoPage(@Param("faultInfoId")Integer faultInfoId, RowBounds rowBounds);
}