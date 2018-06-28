package com.xmsmartcity.maintain.service;

import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.ServiceType;


public interface ServerTypeService extends BaseService<ServiceType>{

	/**
	 * 根据项目ID获取服务内容
	 * @param projectId
	 * @return
	 */
	public List<ServiceType> selectServerTypeByProjectId(Integer projectId,Integer type);

	/**
	 * 保存服务内容
	 * @param serverType
	 */
	@Deprecated
	public ServiceType saveServerType(ServiceType serverType);
	
	/**
	 * 更新服务内容
	 * @param serverType
	 * @return
	 */
	public void updateServerType(ServiceType serverType);

	/**
	 * 根据ID查询服务内容
	 * @param id
	 * @return
	 */
	public ServiceType getServerType(Integer id);

	/**
	 * 根据ID查询服务内容(带项目名称)
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSericeById(Integer id);
}
