package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.SysTypegroupMapper;
import com.xmsmartcity.maintain.pojo.SysTypegroup;
import com.xmsmartcity.maintain.service.SysTypegroupService;

@Service
public class SysTypegroupImpl extends BaseServiceImpl<SysTypegroup> implements SysTypegroupService {

	@Autowired
	private SysTypegroupMapper sysTypegroupDao;
	
	public SysTypegroupImpl(BaseDao<SysTypegroup> dao) {
		super(dao);
	}

	@Override
	public List<SysTypegroup> getListByRecord(SysTypegroup record,Integer startIndex,Integer pageSize) {
		return sysTypegroupDao.selectByRecord(record,startIndex,pageSize);
	}
	
}
