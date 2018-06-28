package com.xmsmartcity.maintain.service;

import java.util.List;
import com.xmsmartcity.maintain.pojo.Funcreg;

public interface FuncregService extends BaseService<Funcreg>{

	/**
	 * 获取用户的权限
	 *@param roleId
	 *@return
	 *2016-7-18
	 */
	public List<Funcreg> queryAllByRoleId(Integer roleId);
	
}
