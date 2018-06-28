package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.pojo.Funcreg;
import com.xmsmartcity.maintain.service.FuncregService;

@Service
public class FuncregServiceImpl extends BaseServiceImpl<Funcreg> implements FuncregService {

	public FuncregServiceImpl(BaseDao<Funcreg> dao) {
		super(dao);
	}

	@Override
	public List<Funcreg> queryAllByRoleId(Integer roleId) {
		return null;
	}

}
