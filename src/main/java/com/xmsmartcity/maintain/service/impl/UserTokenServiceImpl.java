package com.xmsmartcity.maintain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.UserTokenMapper;
import com.xmsmartcity.maintain.pojo.UserToken;
import com.xmsmartcity.maintain.service.UserTokenService;
import com.xmsmartcity.util.Constant;

@Service
public class UserTokenServiceImpl extends BaseServiceImpl<UserToken> implements UserTokenService{

	public UserTokenServiceImpl(BaseDao<UserToken> dao) {
		super(dao);
	}

	@Autowired
	private UserTokenMapper dao;
	
	@Override
	public UserToken getTokenInfoByToken(Integer userId, String token) {
		return dao.getTokenInfo(userId,null,token);
	}

	@Override
	public UserToken getTokenInfo(String pushToken, Integer userId) {
		return dao.getTokenInfo(userId,pushToken,null);
	}

	@Override
	public void setPushToken(Integer userId, String pushToken) {
		Assert.state(StringUtils.isNotBlank(pushToken),Constant.PARAMS_ERROR+":pushToken参数错误");
		UserToken tokenObj = dao.getTokenInfo(userId, null, null);
		if(tokenObj != null){
			String resPushToken = tokenObj.getPushToken();
			if(StringUtils.isBlank(resPushToken)){
				tokenObj.setPushToken(pushToken);
				dao.updateByPrimaryKeySelective(tokenObj);
			}
		}
	}
	
}
