package com.xmsmartcity.maintain.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Null;

import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.PatrolSchemeMapper;
import com.xmsmartcity.maintain.dao.PatrolSchemeRecordItemMapper;
import com.xmsmartcity.maintain.dao.PatrolSchemeRecordMapper;
import com.xmsmartcity.maintain.dao.SchemeRecordItemEquipMapper;
import com.xmsmartcity.maintain.pojo.PatrolScheme;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecord;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecordItem;
import com.xmsmartcity.maintain.pojo.SchemeRecordItemEquip;
import com.xmsmartcity.maintain.service.PatrolSchemeRecordService;
import com.xmsmartcity.maintain.service.SchedulerService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;

@Service
public class PatrolSchemeRecordImpl extends BaseServiceImpl<PatrolSchemeRecord> implements PatrolSchemeRecordService {

	@Autowired
	private PatrolSchemeRecordMapper patrolSchemeRecordDao;
	@Autowired
	private PatrolSchemeMapper patrolSchemeDao;
	@Autowired
	private PatrolSchemeRecordItemMapper patrolSchemeRecordItemDao;
	@Autowired
	SchedulerService schedulerService;
	@Autowired
	SchemeRecordItemEquipMapper schemeRecordItemEquipDao;
	
	public PatrolSchemeRecordImpl(BaseDao<PatrolSchemeRecord> dao) {
		super(dao);
	}

	@Override
	public List<PatrolSchemeRecord> getListByRecord(PatrolSchemeRecord record, Date startTime, Date endTime,
			Integer userId, Integer startIndex, Integer pageSize) {
		return patrolSchemeRecordDao.selectByRecord(record, startTime, endTime, startIndex, pageSize, userId);
	}

	@Override
	public List<PatrolSchemeRecord> getListByRecordPermisson(PatrolSchemeRecord record, Date startTime, Date endTime,
			Integer userId, Integer startIndex, Integer pageSize) {
		return patrolSchemeRecordDao.selectByRecordPermisson(record, startTime, endTime, startIndex, pageSize, userId);
	}
	
	@Override
	public List<PatrolSchemeRecord> getListByState(Integer state, Integer userId,String equipId) {
		if (state==1) {
			List<Map<String, Object>> list= getRecentList(userId, equipId);
			return JSON.parseArray(JSON.toJSONString(list), PatrolSchemeRecord.class);
		}else {
			return patrolSchemeRecordDao.selectByState(state, userId,equipId);
		}
	}

	@Override
	public PatrolSchemeRecord saveOrUpdate(Integer userId, PatrolSchemeRecord patrolSchemeRecord, String recordItem) {
		Date newTime = new Date();
		PatrolScheme patrolScheme = patrolSchemeDao.selectByPrimaryKey(patrolSchemeRecord.getPatrolSchemeId());
		Assert.state(patrolScheme != null, "查无此巡检任务！");
		String today = DateUtils.DateToString(newTime, DateUtils.formatStr_yyyyMMdd);
		String checkTimeHour = DateUtils.DateToString(patrolScheme.getCheckTime(), DateUtils.formatStr_HHmmss);
		Date compareTime = DateUtils.StringToDate(today + " " + checkTimeHour, DateUtils.formatStr_yyyyMMddHHmmss);
		if (newTime.getTime() >= compareTime.getTime()) {
			patrolSchemeRecord.setState(1);
		} else {
			patrolSchemeRecord.setState(2);
		}
		patrolSchemeRecord.setUpdatetime(newTime);
		patrolSchemeRecord.setUpdateUserId(userId);
		//巡检记录明细
		List<PatrolSchemeRecordItem> patrolSchemeRecordItems = JSON.parseArray(recordItem,
				PatrolSchemeRecordItem.class);
		for (PatrolSchemeRecordItem patrolSchemeRecordItem : patrolSchemeRecordItems) {
			if (patrolSchemeRecordItem.getIsNormal()==1) {
				patrolSchemeRecord.setIsNormal(1);
				break;
			}
		}
		//查询今天是否已存在记录，如果已存在，则更新、否则则插入。防止在巡检时间节点点击巡检出现异常！start
		PatrolSchemeRecord select=new PatrolSchemeRecord();
		select.setPatrolSchemeId(patrolScheme.getId());
		List<PatrolSchemeRecord> patrolSchemeRecords=patrolSchemeRecordDao.selectByRecord(select, new Date(), new Date(), patrolScheme.getPatrolUserId(), null, null);
		if (patrolSchemeRecords.size()>0) {
			patrolSchemeRecord.setId(patrolSchemeRecords.get(0).getId());
		}
		//end
		if (patrolSchemeRecord.getId()!=null && patrolSchemeRecord.getId() != 0) {
			PatrolSchemeRecord record = patrolSchemeRecordDao.selectByPrimaryKey(patrolSchemeRecord.getId());
			Assert.state(record != null, "查无此巡检记录！");
			patrolSchemeRecord.setPatrolTime(newTime);
			patrolSchemeRecordDao.updateByPrimaryKeySelective(patrolSchemeRecord);
		} else {
			patrolSchemeRecord.setCreateUserId(userId);
			patrolSchemeRecord.setCreatetime(newTime);
			patrolSchemeRecord.setPatrolTime(newTime);
			patrolSchemeRecordDao.insertSelective(patrolSchemeRecord);
		}
		for (PatrolSchemeRecordItem patrolSchemeRecordItem : patrolSchemeRecordItems) {
			patrolSchemeRecordItem.setPatrolSchemeRecordId(patrolSchemeRecord.getId());
		}
		// 先删除之前的巡检记录明细，再添加
		patrolSchemeRecordItemDao.deleteByPatrolSchemeRecordId(patrolSchemeRecord.getId());
		patrolSchemeRecordItemDao.insertBatch(patrolSchemeRecordItems);
		// 插入巡检记录明细设备关联
		for (PatrolSchemeRecordItem patrolSchemeRecordItem : patrolSchemeRecordItems) {
			schemeRecordItemEquipDao.deleteByPatrolSchemeRecordItemId(patrolSchemeRecordItem.getId());
			if (patrolSchemeRecordItem.getEquipId()!=null && !patrolSchemeRecordItem.getEquipId().trim().equals("")) {
				String[] equips=patrolSchemeRecordItem.getEquipId().split(",");
				List<SchemeRecordItemEquip> list=new ArrayList<>();
				for (String string : equips) {
					SchemeRecordItemEquip itemEquip=new SchemeRecordItemEquip();
					itemEquip.setCreatetime(newTime);
					itemEquip.setCreateUserId(userId);
					itemEquip.setEquipId(Integer.parseInt(string));
					itemEquip.setPatrolSchemeRecordItemId(patrolSchemeRecordItem.getId());
					list.add(itemEquip);
				}
				schemeRecordItemEquipDao.insertBatch(list);
			}
		}
		patrolSchemeRecord.setPatrolSchemeRecordItems(patrolSchemeRecordItems);
		return patrolSchemeRecord;
	}

	/**
	 * 
	 * 
	 * @param 
	 * @return
	 */
	@Override
	public Map<String, Object> getSummarySum(Integer userId,String equipId) {
		Map<String, Object> map = patrolSchemeRecordDao.selectSummarySum(userId,equipId);
		List<Map<String, Object>> recentList = getRecentList(userId, equipId);
		map.put("recentSum", recentList.size());
		return map;
	}

	/**
	 * 获取近期巡检
	 * @param userId
	 * @param equipId
	 * @return
	 */
	private List<Map<String, Object>> getRecentList(Integer userId, String equipId) {
		JSONArray array = schedulerService.getSchedulingList();
		Date nowDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, 7); // 设置为7天后
		Date after7days = calendar.getTime(); // 得到7天后的时间
		String ids = "";
		List<Map<String, Object>> list=new ArrayList<>();
		List<Map<String, Object>> recentList=new ArrayList<>();
		for (Object object : array) {
			JSONObject jsonObject = (JSONObject) object;
			if (Constant.PATROLSCHEME_JOB_GROUP.equals(jsonObject.getString("jobGroup"))) {
				Date nextTime = jsonObject.getDate("nextFireTime");
				String id = jsonObject.getString("jobName");
				// 下一次巡检任务时间为7天内，则拼凑
				if (nextTime.getTime() < after7days.getTime()) {
					ids += (id + ",");
				}
			}
		}
		if (!ids.equals("")) {
			list = patrolSchemeDao.selectListByIdsPermisson(ids.substring(0, ids.length()-1),userId,equipId);
		}
		for(int i=0;i<list.size();i++){
			Date checkTime=(Date)list.get(i).get("checkTime");
			String nowStr= DateUtils.DateToString(nowDate, DateUtils.formatStr_yyyyMMdd);
			String checkWeak=DateUtils.DateToString(checkTime, DateUtils.formatStr_HHmmss);
			String checkTimeStr=nowStr+" "+checkWeak;
			Date patrolTime=DateUtils.StringToDate(checkTimeStr, DateUtils.formatStr_yyyyMMddHHmmss);
			list.get(i).put("checkTime", patrolTime);
			recentList.add(list.get(i));
			int period=(int)list.get(i).get("period");
			//只有每天的需要添加新记录，每周以上的不需要
			if (period==1) {
				for (int j = 1; j < 7; j++) {
					Map<String, Object> addMap=new HashMap<String, Object>(list.get(i));
					addMap.put("checkTime", DateUtils.plusDay(patrolTime, j));
					recentList.add(addMap);
				}
			}
		}
		return recentList;
	}

	
	
}
