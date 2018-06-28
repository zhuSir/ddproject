package com.xmsmartcity.maintain.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.ServiceTypeMapper;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.ServerTypeService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.Constant;

@Service
public class ServerTypeServiceImpl extends BaseServiceImpl<ServiceType> implements ServerTypeService{

	public ServerTypeServiceImpl(BaseDao<ServiceType> dao) {
		super(dao);
	}

	@Autowired
	private ServiceTypeMapper dao;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 获取服务内容列表
	 * @author:zhugw
	 * @time:2017年4月12日 上午10:18:47
	 * @param projectId
	 * @param type
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.ServerTypeService#selectServerTypeByProjectId(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<ServiceType> selectServerTypeByProjectId(Integer projectId,Integer type) {
		List<ServiceType> results = dao.selectByProjectId(projectId);
		return results;
	}

	@Override
	@Deprecated
	public ServiceType saveServerType(ServiceType serverType) {
		Assert.state(StringUtils.isNotBlank(serverType.getMaintainUserMobile())
				&& StringUtils.isNotBlank(serverType.getMaintainUserName())
				&& StringUtils.isNotBlank(serverType.getName())
				,Constant.PARAMS_ERROR);
		serverType.setState(Constant.ONE);//默认不需要确认
		Integer res = dao.insertSelective(serverType);
		Assert.state(res == Constant.ONE,Constant.SERVICE_ERROR);
		User maintainUser = userService.getUserByPhone(serverType.getMaintainUserMobile());
		if(maintainUser == null){
			//TODO 短信通知
		}else{
			//TO DO 个推，微信 通知
		}
		return serverType;
	}

	@Override
	public ServiceType getServerType(Integer id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public void updateServerType(ServiceType serverType) {
		dao.updateByPrimaryKeySelective(serverType);
	}
	
	public Map<String, Object> getSericeById(Integer id) {
		return dao.getSericeById(id);
	}
}
