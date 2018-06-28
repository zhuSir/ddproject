package com.xmsmartcity.maintain.service;

import com.xmsmartcity.maintain.pojo.UserToken;

public interface UserTokenService extends BaseService<UserToken>{

	/**
	 * 获取用户token对象（根据系统token）
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:10:00
	 * @param userId
	 * @param token
	 * @return
	 */
	public UserToken getTokenInfoByToken(Integer userId,String token);

	/**
	 * 获取用户token对象（根据pushtoken）
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:10:13
	 * @param pushToken
	 * @param userId
	 * @return
	 */
	public UserToken getTokenInfo(String pushToken,Integer userId);

	/**
	 * 设置pushtoken
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:10:51
	 * @param userId
	 * @param pushToken
	 */
	public void setPushToken(Integer userId, String pushToken);
	
}
