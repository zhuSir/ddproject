package com.xmsmartcity.maintain.service;

public interface BaseService<T> {
	Integer deleteByPrimaryKey(Integer id);

	Integer insert(T record);

	Integer insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    Integer updateByPrimaryKeySelective(T record);

    Integer updateByPrimaryKey(T record);
    
}
