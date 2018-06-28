package com.xmsmartcity.maintain.service.impl;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.CompanyMapper;
import com.xmsmartcity.maintain.dao.SignInMapper;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.SignIn;
import com.xmsmartcity.maintain.service.SignInService;
import com.xmsmartcity.orm.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenby on 2017/4/27.
 */
@Service
public class SignInServiceImpl extends BaseServiceImpl<SignIn> implements SignInService{

    public SignInServiceImpl(BaseDao<SignIn> dao) {
		super(dao);
	}

	@Autowired
    private SignInMapper signInDao;
	
	@Autowired
	private CompanyMapper companyDao;

    @Override
    public List<SignIn> getSignInByDate(Date date,Integer userId) {
        return  signInDao.getSignInByDate(date,userId);
    }

	@Override
	public List<Date> getSignDayByMonth(Date date,Integer userId) {
		return signInDao.getSignDayByMonth(date,userId);
	}

	@Override
	public  List<Map<String, Object>> getSignInForCollect(Date checkTime, Integer userId) {
		Company company = companyDao.selectByResponserId(userId);
		Assert.state(company != null ,"您不是团队负责人，不能查看签到汇总情况。");
		List<Map<String, Object>> results = signInDao.getSignInForCollect(company.getId(),checkTime);
		return results;
	}

	@Override
	public List<Date> getCollectDayByMonth(Date date, Integer userId) {
		Company company = companyDao.selectByResponserId(userId);
		Assert.state(company != null ,"您不是团队负责人，不能查看签到汇总情况。");
		List<Date> results = signInDao.getCollectDayByMonth(date,company.getId());
		return results;
	}

	/*
	 * web签到页面
	 */
	@Override
	public Object selectListPageByDate(Integer type, Integer userId,Integer pageNo,Integer pageSize) {
		List<Map<String, Object>> result = signInDao.selectListByDate(type,userId,new Pagination<Map<String, Object>>(pageNo,pageSize).getRowBounds());
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(result);
		return pageInfo;
	}
}
