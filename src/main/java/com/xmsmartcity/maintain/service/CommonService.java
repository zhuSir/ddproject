package com.xmsmartcity.maintain.service;

public interface CommonService {
	
	public String getAppVersion(Integer appType, String version) throws Exception;

	Object searchOverallData(Integer userId,String searchContent,Integer type,Integer pageNum,Integer pageSize);
}
