package com.xmsmartcity.maintain.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.DealEvent;

@Repository
public interface DealEventMapper extends BaseDao<DealEvent>{
    
    DealEvent selectByUserId(Integer userId);

	DealEvent selectByCompanyPrincipal(@Param("companyId") Integer companyId,@Param("userId") Integer userId);
    
}