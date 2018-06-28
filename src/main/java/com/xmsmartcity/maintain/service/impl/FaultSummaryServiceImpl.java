package com.xmsmartcity.maintain.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.CompanyMapper;
import com.xmsmartcity.maintain.dao.EvaluateMapper;
import com.xmsmartcity.maintain.dao.FaultSummaryMapper;
import com.xmsmartcity.maintain.dao.MessageMapper;
import com.xmsmartcity.maintain.dao.UserMapper;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.FaultSummary;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.FaultSummaryService;

@Service
public class FaultSummaryServiceImpl extends BaseServiceImpl<FaultSummary> implements FaultSummaryService {

	public FaultSummaryServiceImpl(BaseDao<FaultSummary> dao) {
		super(dao);
	}

	@Autowired
	private FaultSummaryMapper faultSummaryDao;
	
	@Autowired
	private CompanyMapper companyDao;
	
	@Autowired
	private EvaluateMapper evaluateDao;
	
	@Autowired
	private MessageMapper messageDao;
	
	@Autowired
	private UserMapper userDao;
	
	/**
	 * 获取维保汇总列表
	 */
	@Override
	public List<FaultSummary> getmaintenanList(Integer userId,Date receivetimeStart,Date receivetimeEnd) {
		return faultSummaryDao.selectFaultList(userId, receivetimeStart, receivetimeEnd,1);
	}

	/**
	 * 获取报障(维保)汇总列表
	 * type 0:甲方  1：乙方
	 */
	@Override
	public List<FaultSummary> getFaultList(Integer userId,Date startTime,Date endTime,String type) {
		if (type.equals("0")) {
			return faultSummaryDao.selectFaultList(userId, startTime,endTime,0);			
		}else {
			return faultSummaryDao.selectFaultList(userId, startTime, endTime,1);
		}
	}

	@Override
	public Map<String, Object> getSummaryNum(Integer userId,Integer type) {
		return faultSummaryDao.selectSummaryNum(userId,type);
	}

	
	@Override
	public Map<String,Object> selectHomeStatInfo(Integer userId) {
		Map<String,Object> res = new HashMap<String,Object>();
		Company company = companyDao.selectByResponserId(userId);
		Integer companyId = company != null ? company.getId() : null; 
		Map<String,Long> maintainSumRes = faultSummaryDao.selectMaintainSummaryNum(userId,companyId);
		if(maintainSumRes.get("maintain_num") == 0){
			Map<String,Integer> faultSumRes = faultSummaryDao.selectFaultSummaryNum(userId,companyId);
			res.put("faultNum", faultSumRes.get("fault_num"));
			res.put("faultSumNum", faultSumRes.get("fault_sum"));
			res.put("faultCurrentMonthNum", faultSumRes.get("month_fault_num"));			
		}
		Integer projectNum = faultSummaryDao.selectProjectSummaryNum(userId,companyId);
		List<Map<String, Object>> abnormalTimeOutRes =  evaluateDao.getAbnormalByTimeout(userId);
		User user = userDao.selectByPrimaryKey(userId);
		Integer unreadNum = messageDao.selectUnReadMessageByPhone(user.getPhone());
		res.put("maintainNum", maintainSumRes.get("maintain_num"));
		res.put("maintainSumNum", maintainSumRes.get("maintain_sum"));
		res.put("maintainCurrentMonthNum", maintainSumRes.get("month_maintain_num"));
		res.put("projectNum", user.getCompanyId().equals(0) ? 0 : projectNum);
		res.put("abnormalNum", abnormalTimeOutRes.size());
		res.put("unreadNum", unreadNum);
		return res;
	}

	@Override
	public List<FaultSummary> getStatisticsByUser(Integer userId, Date startTime, Date endTime, String type) {
		if (type.equals("0")) {
			return faultSummaryDao.selectStatisticsByUser(userId, startTime,endTime,0);			
		}else {
			return faultSummaryDao.selectStatisticsByUser(userId, startTime, endTime,1);
		}
	}

	@Override
	public List<Map<String, Object>> excelByProjectId(Integer userId, Date startTime, Date endTime, String type,
			Integer projectId) {
		Integer typei =type.equals("0")?0:1;
		return faultSummaryDao.excelByProjectId(userId, startTime, endTime, typei, projectId);
	}

	@Override
	public List<Map<String, Object>> excelByUserId(Integer userId, Date startTime, Date endTime, String type) {
		Integer typei =type.equals("0")?0:1;
		return faultSummaryDao.excelByUserId(userId, startTime, endTime, typei);
	}
	
}
