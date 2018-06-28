package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.SysTypeMapper;
import com.xmsmartcity.maintain.pojo.SysType;
import com.xmsmartcity.maintain.service.SysTypeService;

@Service
public class SysTypeImpl extends BaseServiceImpl<SysType> implements SysTypeService {

	@Autowired
	private SysTypeMapper sysTypeDao;
	
	public SysTypeImpl(BaseDao<SysType> dao) {
		super(dao);
	}

	@Override
	public List<SysType> getListByRecord(SysType record,Integer startIndex,Integer pageSize) {
		return sysTypeDao.selectByRecord(record,startIndex,pageSize);
	}
	
}
