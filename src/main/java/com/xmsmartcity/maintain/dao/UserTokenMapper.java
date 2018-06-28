package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.UserToken;

@Repository
public interface UserTokenMapper extends BaseDao<UserToken>{
    
    UserToken getTokenInfo(@Param("userId") Integer userId,@Param("pushToken") String pushToken,@Param("sysToken") String sysToken);

	Integer removeByParams(@Param("userId")Integer userId,@Param("sysToken") String sysToken);
	
	List<UserToken> getTokenInfoByParams(@Param("userId")Integer userId,@Param("pushToken")String pushToken,@Param("types")Integer[] types);

	void deleteForIds(@Param("ids")Integer[] ids);
}