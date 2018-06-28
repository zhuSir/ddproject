package com.xmsmartcity.maintain.controller.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.PatrolScheme;
import com.xmsmartcity.maintain.pojo.PatrolSchemeItem;
import com.xmsmartcity.maintain.pojo.PatrolSchemeModel;
import com.xmsmartcity.maintain.pojo.PatrolSchemeModelItem;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecord;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecordItem;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.PatrolSchemeItemService;
import com.xmsmartcity.maintain.service.PatrolSchemeModelItemService;
import com.xmsmartcity.maintain.service.PatrolSchemeModelService;
import com.xmsmartcity.maintain.service.PatrolSchemeRecordItemService;
import com.xmsmartcity.maintain.service.PatrolSchemeRecordService;
import com.xmsmartcity.maintain.service.PatrolSchemeService;
import com.xmsmartcity.maintain.service.SchedulerService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/patrol")
public class PatrolApiController {

	@Autowired
	private PatrolSchemeService patrolSchemeService;
	@Autowired
	private PatrolSchemeItemService patrolSchemeItemService;
	@Autowired
	private PatrolSchemeModelService patrolSchemeModelService;
	@Autowired
	private PatrolSchemeModelItemService patrolSchemeModelItemService;
	@Autowired
	private PatrolSchemeRecordService patrolSchemeRecordService;
	@Autowired
	private PatrolSchemeRecordItemService patrolSchemeRecordItemService;
	@Autowired
	private UserService userService;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private CompanyService companyService;

	/**
	 * 新建巡检任务
	 * 
	 * @param userId
	 * @param patrolScheme
	 * @return
	 */
	@RequestMapping(value = "/add/patrolScheme")
	@ResponseBody
	@Transactional
	public String addPatrolScheme(Integer userId,PatrolScheme patrolScheme, String checkTimeStr, String patrolSchemeItemStr) {
		Assert.state(patrolScheme.getName() != null && !patrolScheme.getName().equals("") && checkTimeStr != null
				&& patrolScheme.getPeriod() != null && patrolScheme.getPatrolAbnormal() != null
				&& patrolScheme.getPatrolUserMobile() != null && patrolScheme.getPatrolUserName() != null
				&& patrolScheme.getEquipId() != null, Constant.PARAMS_ERROR);
		User user = userService.selectByPhone(patrolScheme.getPatrolUserMobile());
		Assert.state(user != null, "查无此人员，请使用注册用户");
		patrolScheme.setPatrolUserId(user == null ? 0 : user.getId());
		patrolScheme.setCreateUserId(userId);
		patrolScheme.setUpdateUserId(userId);
		List<PatrolSchemeItem> items = JSON.parseArray(patrolSchemeItemStr, PatrolSchemeItem.class);
		patrolScheme.setPatrolSchemeItems(items);
		patrolScheme.setCheckTime(DateUtils.StringToDate(checkTimeStr, DateUtils.formatStr_yyyyMMddHHmm));
		PatrolScheme res = patrolSchemeService.saveOrUpdate(patrolScheme);
		if (patrolScheme.getIsmodel()) {
			String jsonStr = JSON.toJSONString(patrolScheme);
			PatrolSchemeModel model = JSON.parseObject(jsonStr, PatrolSchemeModel.class);
			model.setId(null);
			model.setIsOpen(false);
			List<PatrolSchemeItem> patrolSchemeItems = patrolScheme.getPatrolSchemeItems();
			List<PatrolSchemeModelItem> patrolSchemeModelItems = new ArrayList<PatrolSchemeModelItem>();
			for (PatrolSchemeItem patrolSchemeItem : patrolSchemeItems) {
				PatrolSchemeModelItem patrolSchemeModelItem = new PatrolSchemeModelItem();
				patrolSchemeModelItem.setIntroduce(patrolSchemeItem.getIntroduce());
				patrolSchemeModelItem.setName(patrolSchemeItem.getName());
				patrolSchemeModelItem.setNumber(patrolSchemeItem.getNumber());
				patrolSchemeModelItems.add(patrolSchemeModelItem);
			}
			model.setPatrolSchemeModelItems(patrolSchemeModelItems);
			patrolSchemeModelService.saveOrUpdate(model);
		}
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 编辑巡检任务
	 * 
	 * @param userId
	 * @param patrolScheme
	 * @return
	 */
	@RequestMapping(value = "/update/patrolScheme")
	@ResponseBody
	public String updatePatrolScheme(Integer userId, PatrolScheme patrolScheme,String checkTimeStr,String patrolSchemeItemStr) {
		Assert.state(patrolScheme.getId() != null && patrolScheme.getName() != null && !patrolScheme.getName().equals("") && checkTimeStr != null
				&& patrolScheme.getPeriod() != null && patrolScheme.getPatrolAbnormal() != null
				&& patrolScheme.getPatrolUserMobile() != null && patrolScheme.getPatrolUserName() != null
				&& patrolScheme.getEquipId() != null, Constant.PARAMS_ERROR);
		User user = userService.selectByPhone(patrolScheme.getPatrolUserMobile());
		Assert.state(user != null, "查无此人员，请使用注册用户");
		User loginUser = userService.selectByPrimaryKey(userId);
		PatrolScheme oldPatrol=patrolSchemeService.selectByPrimaryKey(patrolScheme.getId());
		Assert.state(oldPatrol != null, "查无此巡检任务");
		Company company = companyService.selectByPrimaryKey(loginUser.getCompanyId());
		Assert.state((company != null && company.getResponserId().equals(loginUser.getId())) || oldPatrol.getCreateUserId().equals(userId), "只有创建人和公司负责人才能编辑");
		patrolScheme.setPatrolUserId(user == null ? 0 : user.getId());
		patrolScheme.setUpdateUserId(userId);
		List<PatrolSchemeItem> items = JSON.parseArray(patrolSchemeItemStr, PatrolSchemeItem.class);
		patrolScheme.setPatrolSchemeItems(items);
		patrolScheme.setCheckTime(DateUtils.StringToDate(checkTimeStr, DateUtils.formatStr_yyyyMMddHHmm));
		PatrolScheme res = patrolSchemeService.saveOrUpdate(patrolScheme);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 查询巡检计划
	 * 
	 * @param userId
	 * @param patrolScheme
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeList")
	@ResponseBody
	public String getPatrolSchemeList(Integer userId, PatrolScheme patrolScheme, Integer startIndex, Integer pageSize) {
		List<PatrolScheme> list = patrolSchemeService.getPatrolSchemeList(patrolScheme,null, startIndex, pageSize);
		return Utils.toSuccessJson(list);
	}

	/**
	 * 根据巡检计划获取巡检内容明细
	 * 
	 * @param userId
	 * @param patrolSchemeId
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeItemList")
	@ResponseBody
	public String getPatrolSchemeList(Integer userId, Integer patrolSchemeId) {
		List<PatrolSchemeItem> list = patrolSchemeItemService.getListByPatrolSchemeId(patrolSchemeId);
		return Utils.toSuccessJson(list);
	}

	/**
	 * 查询巡检模板
	 * 
	 * @param userId
	 * @param patrolSchemeId
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeModelList")
	@ResponseBody
	public String getPatrolSchemeList(Integer userId, PatrolSchemeModel patrolSchemeModel, Integer startIndex,
			Integer pageSize) {
		List<PatrolSchemeModel> list = patrolSchemeModelService.getPatrolSchemeModelList(patrolSchemeModel,userId, startIndex,
				pageSize);
		return Utils.toSuccessJson(list);
	}

	/**
	 * 根据巡检模板ID获取巡检内容明细
	 * 
	 * @param userId
	 * @param patrolSchemeId
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeModelItemList")
	@ResponseBody
	public String getPatrolSchemeModelList(Integer userId, Integer patrolSchemeModelId) {
		List<PatrolSchemeModelItem> list = patrolSchemeModelItemService
				.getListByPatrolSchemeModelId(patrolSchemeModelId);
		return Utils.toSuccessJson(list);
	}

	/**
	 * 查询巡检记录
	 * 
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeRecordListByTime")
	@ResponseBody
	public String getPatrolSchemeListByTime(Integer userId, String startTime, String endTime) {
		Date newTime = new Date();
		Assert.state(startTime != null && endTime != null, Constant.PARAMS_ERROR);
		Date startTimeD = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
		Date endTimeD = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
		boolean isToday=(DateUtils.compareDate(newTime, startTimeD) && DateUtils.compareDate(DateUtils.plusDay(endTimeD, 1), newTime));
		// 查询日期为今天，则从调度任务中获取
		List<Map<String, Object>> patrolSchemes = new ArrayList<>();
		if (isToday) {
			JSONArray arry = schedulerService.getSchedulingList();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fmt1 = new SimpleDateFormat("HH:mm:ss");
			String ids = "";
			for (Object object : arry) {
				JSONObject jsonObject = (JSONObject) object;
				if (Constant.PATROLSCHEME_JOB_GROUP.equals(jsonObject.getString("jobGroup"))) {
					Date nextTime = jsonObject.getDate("nextFireTime");
					String id = jsonObject.getString("jobName");
					// 下一次巡检任务时间为今天
					boolean isNextToday = false;
					isNextToday = fmt.format(newTime).toString().equals(fmt.format(nextTime).toString());
					if (isNextToday) {
						ids += (id+",");
					}
				}
			}
			
			if (!ids.equals("")) {
				patrolSchemes = patrolSchemeService.getPatrolSchemeListByIds(ids.substring(0, ids.length()-1),userId,null);
			}
			for(int i=0;i<patrolSchemes.size();i++){
				String checkTime=fmt.format(newTime).toString()+" "+fmt1.format((Date)patrolSchemes.get(i).get("checkTime")).toString();
				Date itemDate=DateUtils.StringToDate(checkTime, DateUtils.formatStr_yyyyMMddHHmmss);
				patrolSchemes.get(i).put("patrolTime", itemDate);
			}
		}
		List<PatrolSchemeRecord> list = patrolSchemeRecordService.getListByRecord(new PatrolSchemeRecord(), startTimeD,
				endTimeD,userId, null, null);
		List<PatrolSchemeRecord> todayList=JSON.parseArray(JSON.toJSONString(patrolSchemes), PatrolSchemeRecord.class);
		list.addAll(todayList);
		return Utils.toSuccessJson(list);
	}

	/**
	 * 根据巡检状态查询巡检记录
	 * @param userId
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeRecordListByState")
	@ResponseBody
	public String getPatrolSchemeListByState(Integer userId, Integer state,String equipId) {
		Assert.state(state != null && userId != null, Constant.PARAMS_ERROR);
		List<PatrolSchemeRecord> list = patrolSchemeRecordService.getListByState(state,userId,equipId);
		return Utils.toSuccessJson(list);
	}
	
	/**
	 * 根据巡检月份获取巡检日历状态
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeRecordCalendar")
	@ResponseBody
	public String getParolSchemeRecordCalendar (String time) {
		
		return null;
	}
	
	/**
	 * 插入巡检记录
	 * @param userId
	 * @param patrolSchemeRecord
	 * @param patrolSchemeRecordItem
	 * @return
	 */
	@RequestMapping(value = "/add/patrolSchemeRecord")
	@ResponseBody
	public String addPatrolSchemeRecord(Integer userId,PatrolSchemeRecord patrolSchemeRecord,String patrolSchemeRecordItemList) {
		patrolSchemeRecord=patrolSchemeRecordService.saveOrUpdate(userId,patrolSchemeRecord,patrolSchemeRecordItemList);
		return Utils.toSuccessJsonResults(patrolSchemeRecord);
	}
	
	/**
	 * 根据巡检记录ID查询记录
	 * @param userId
	 * @param patrolSchemeRecord
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeRecordById")
	@ResponseBody
	public String getPatrolSchemeRecordById(Integer userId,PatrolSchemeRecord patrolSchemeRecord) {
		List<PatrolSchemeRecord> list = patrolSchemeRecordService.getListByRecordPermisson(patrolSchemeRecord, null,null,userId, null, null);
		Assert.state(list.size()>0,"查无此记录");
		PatrolSchemeRecordItem patrolSchemeRecordItem= new PatrolSchemeRecordItem();
		patrolSchemeRecordItem.setPatrolSchemeRecordId(list.get(0).getId());
		List<PatrolSchemeRecordItem> listItem=patrolSchemeRecordItemService.selectByRecord(patrolSchemeRecordItem);
		list.get(0).setPatrolSchemeRecordItems(listItem);
		return Utils.toSuccessJsonResults(list.get(0));
	}
	
	/**
	 * 巡检概要（统计数据）
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeRecordSummary")
	@ResponseBody
	public String getPatrolSchemeRecordSummary(Integer userId,String equipId){
		Map<String, Object> map=patrolSchemeRecordService.getSummarySum(userId,equipId);
		return Utils.toSuccessJsonResults(map);
	}
	
	/**
	 * 根据巡检记录字段查询记录
	 * @param userId
	 * @param patrolSchemeRecord
	 * @return
	 */
	@RequestMapping(value = "/get/patrolSchemeRecordByRecord")
	@ResponseBody
	public String getPatrolSchemeRecordByRecord(Integer userId,PatrolSchemeRecord patrolSchemeRecord) {
		List<PatrolSchemeRecord> list = patrolSchemeRecordService.getListByRecordPermisson(patrolSchemeRecord, null,null,userId, null, null);
		return Utils.toSuccessJson(list);
	}
}
