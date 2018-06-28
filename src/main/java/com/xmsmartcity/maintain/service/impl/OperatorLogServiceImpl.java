package com.xmsmartcity.maintain.service.impl;


import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.pojo.OperatorLog;
import com.xmsmartcity.maintain.service.OperatorLogService;

@Service
public class OperatorLogServiceImpl extends BaseServiceImpl<OperatorLog> implements OperatorLogService {

	public OperatorLogServiceImpl(BaseDao<OperatorLog> dao) {
		super(dao);
	}

}
