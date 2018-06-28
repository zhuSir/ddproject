package com.xmsmartcity.maintain.controller.mobile;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xmsmartcity.maintain.controller.util.ApiHelper;
import com.xmsmartcity.maintain.pojo.Evaluate;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.MaintainInfo;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.MaterialLabour;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.pojo.WorkOrder;
import com.xmsmartcity.maintain.service.FaultInfoService;
import com.xmsmartcity.maintain.service.RedisCacheService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;
import com.xmsmartcity.util.Utils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/api/fault")
public class FaultApiController {

	@Autowired
	private ApiHelper apiHelper;

	@Autowired
	private FaultInfoService service;

	@Autowired RedisCacheService redisCacheService;
	
	/**
	 * 获取报障列表
	 * 
	 * @param state
	 *            0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/fault-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String faultList(Integer state, Integer userId, String token, Integer type, Integer startIndex,
			Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		Assert.state(state != null && state >= 0, Constant.PARAMS_ERROR);
		List<FaultInfo> results = service.getFaultInfoList(state, userId, type, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 获取团队历史报障列表
	 * 
	 * @param state
	 *            1维修中 2本月维保 3总维保
	 * @param faultState
	 *            0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 * @param type
	 *            0甲方 1乙方
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/sum-fault-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String faultSumList(Integer state, Integer faultState, Integer userId, String token, Integer type,
			Integer startIndex, Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		Assert.state(state != null && state >= 0, Constant.PARAMS_ERROR);
		List<FaultInfo> results = service.getSumFaultInfoList(faultState, state, userId, type, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 获取历史报障列表
	 * 
	 * @param state
	 *            0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/fault-history-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String faultListByServiceTypeId(Integer state, Integer userId, String token, Integer faultInfoId,
			Integer startIndex, Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		state = state == 0 ? null : state;
		Map<String, Object> results = service.getFaultInfoHistoryList(state, userId, faultInfoId, startIndex, pageSize);
		return Utils.toSuccessJsonResults(results);
	}

	/**
	 * 新建报障单
	 * 
	 * @param faultInfo
	 * @param token
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/new-fault-info", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String newFaultInfo(FaultInfo faultInfo, String token) throws IOException {
		Assert.notNull(faultInfo, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getLevel() >= 0 && faultInfo.getLevel() < 3, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getProjectId() != null && faultInfo.getProjectId() > 0, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getServiceTypeId() != null && faultInfo.getServiceTypeId() > 0, Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(faultInfo.getIntroduce()), Constant.EMPTY_INTRODUCE);
		FaultInfo rsult = service.saveFaultInfo(faultInfo);
		return Utils.toSuccessJsonResults(rsult);
	}

	/**
	 * 获取报障单详情
	 * 
	 * @author:zhugw
	 * @time:2017年4月24日 下午8:21:51
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/fault-info-detail")
	@ResponseBody
	public String getFaultInfoDetail(Integer faultInfoId, Integer userId, String token) {
		Assert.state(faultInfoId != null && faultInfoId > 0, Constant.PARAMS_ERROR);
		FaultInfo faultInfo = service.getFaultInfoDetailById(faultInfoId, userId);
		return Utils.toSuccessJsonResults(faultInfo);
	}

	/**
	 * 获取维修人员
	 * 
	 * @author:zhugw
	 * @time:2017年4月24日 下午8:21:51
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/maintain-user-list")
	@ResponseBody
	public String getMaintainUser(Integer faultInfoId, Integer userId, String token) {
		Assert.state(faultInfoId != null && faultInfoId > 0, Constant.PARAMS_ERROR);
		List<User> users = service.getMaintainUserList(faultInfoId, userId);
		return Utils.toSuccessJson(users);
	}

	/**
	 * 报障维修说明
	 * 
	 * @author ZYP @date 2016-9-5
	 * @param faultInfoId
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/maintainInfo-list")
	@ResponseBody
	public String getMaintainInfoList(Integer faultInfoId, Integer userId, String token, Integer startIndex,
			Integer pageSize) {
		Assert.state(faultInfoId != null && faultInfoId > 0, Constant.PARAMS_ERROR);
		List<MaintainInfo> results = service.getMaintainInfoList(faultInfoId, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 报障现场
	 * 
	 * @author ZYP @date 2016-9-5
	 * @param faultInfoId
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/maintainOperate-list")
	@ResponseBody
	public String getMaintainOperateLogList(Integer faultInfoId, Integer userId, String token, Integer startIndex,
			Integer pageSize) {
		Assert.state(faultInfoId != null && faultInfoId > 0, Constant.PARAMS_ERROR);
		List<MaintainOperateLog> results = service.getMaintainOperateLogList(faultInfoId, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 保存费用工单(Android) 请求类型application/json
	 * 
	 * @author:zhugw
	 * @time:2017年4月25日 下午3:53:44
	 * @param materias
	 * @param wordOrder
	 * @param userId
	 * @param token
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/save-materia")
	@ResponseBody
	public String saveMateria(@RequestBody WorkOrder wordOrder) throws IOException {
		apiHelper.validateToken(wordOrder.getUserId(), wordOrder.getToken());
		Assert.state(wordOrder.getFaultInfoId() != null && wordOrder.getFaultInfoId() > 0, Constant.PARAMS_ERROR);
		service.getMaterialLabourAndWorkOrder(wordOrder);
		return Utils.toSuccessJson("保存成功");
	}

	/**
	 * 保存费用工单(IOS)
	 * 
	 * @author:zhugw
	 * @time:2017年6月1日 上午9:28:26
	 * @param wordOrder
	 * @param materiastr
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/materia-save")
	@ResponseBody
	public String saveMaterias(WorkOrder wordOrder, String materiastr) throws IOException {
		Assert.state(wordOrder.getFaultInfoId() != null && wordOrder.getFaultInfoId() > 0, Constant.PARAMS_ERROR);
		JSONArray serverArr = JSONArray.fromObject(materiastr);
		List<MaterialLabour> serverList = JSONArray.toList(serverArr, new MaterialLabour(), new JsonConfig());
		if (materiastr != null && materiastr.length() > 0) {
			wordOrder.setMaterias(serverList);
		}
		service.getMaterialLabourAndWorkOrder(wordOrder);
		return Utils.toSuccessJsonRes("保存成功");
	}

	/**
	 * 报障操作
	 * 
	 * @param faultInfoId
	 * @param type
	 *            0:乙方负责人接收分派报障单 1:乙方负责人接收报障单 2:乙方负责人退回报障单 3:乙方负责人转派报障单
	 *            4:乙方处理报障单 5:乙方负责人拒绝转派 6:乙方负责人增援 7:乙方负责人拒绝增援 8:甲方同意乙方退回
	 *            9:甲方重新发起报障单 10:乙方请求转派 11:填写工单 12:无需工单 13:工单不通过 14:工单通过 15:请求增援
	 *            16:维修说明 17:完成维修 18:验收合格 19:验收不合格 20:工单评价
	 * @param reason
	 *            理由 乙方撤销时为必填
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/fault-info-operate")
	@ResponseBody
	public String faultOperation(Integer faultInfoId, Integer type, String appointmentTime, String reason,
			String referUserId, String referUserPhone, Integer userId, String token, String pics) throws IOException {
		Assert.state(faultInfoId != null && faultInfoId > 0 && type != null, Constant.PARAMS_ERROR);
		Assert.state(type != 11 || type != 16 || type != 20, "paramsError_请调用具体流程接口.");
		service.faultInfoOperate(faultInfoId, type, appointmentTime, reason, referUserId, referUserPhone, pics, userId);
		return Utils.toSuccessJson("操作成功");
	}

	/**
	 * 保存维修说明
	 * 
	 * @author:zhugw
	 * @time:2017年4月29日 下午12:11:10
	 * @param userId
	 * @param token
	 * @param faultInfoId
	 * @param content
	 * @param equipName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/save-maintaininfo")
	@ResponseBody
	public String saveMaintainInfo(Integer userId, String token, MaintainInfo maintainInfo) throws IOException {
		service.saveMaintainInfo(userId, maintainInfo);
		return Utils.toSuccessJson("保存成功");
	}

	/**
	 * 保存评价
	 * 
	 * @author:zhugw
	 * @time:2017年4月29日 下午12:11:10
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/save-evaluate")
	@ResponseBody
	public String saveEvaluate(Evaluate evaluate, String token) throws IOException {
		service.saveEvaluate(evaluate);
		return Utils.toSuccessJson("评价成功");
	}

	/**
	 * 获取评价列表
	 * 
	 * @author:zhugw
	 * @time:2017年4月29日 下午12:11:10
	 * @return
	 */
	@RequestMapping(value = "/get-evaluate")
	@ResponseBody
	public String getEvaluateList(Integer userId, String token, Integer faultInfoId) {
		Evaluate res = service.selectEvaluate(faultInfoId);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 设置预约维保时间
	 * 
	 * @author:zhugw
	 * @time:2017年5月11日 上午9:56:54
	 * @param appointmentTime
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/set-appointment-time")
	@ResponseBody
	public String setAppointmentTime(String appointmentTime, Integer faultInfoId, Integer userId, String token) {
		service.setAppointmentTime(appointmentTime, faultInfoId, userId);
		return Utils.toSuccessJson("设置成功");
	}

	/**
	 * 获取报障单相关信息
	 * 
	 * @author:zhugw
	 * @time:2017年5月18日 下午6:44:32
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/fault-info-count-detail")
	@ResponseBody
	public String getFaultInfo(Integer faultInfoId, Integer userId, String token) {
		Map<String, Object> res = service.selectFaultInfo(faultInfoId, userId);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 查询费用清单
	 * 
	 * @author:zhugw
	 * @time:2017年5月18日 下午7:04:27
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/work-order-detail")
	@ResponseBody
	public String getWordOrderDetail(Integer faultInfoId, Integer userId, String token) {
		Map<String, Object> res = service.selectWorkOrder(faultInfoId, userId);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 获取报障库列表
	 * 
	 * @author:zhugw
	 * @time:2017年5月18日 下午7:46:19
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/fault-store-list")
	@ResponseBody
	public String getFaultStoreList(Integer faultInfoId, Integer userId, String token) {
		List<Map<String, Object>> res = service.selectFaultStoreList(faultInfoId);
		return Utils.toSuccessJson(res);
	}

	/**
	 * 获取报障库详情
	 * 
	 * @author:zhugw
	 * @time:2017年5月18日 下午7:46:19
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/fault-store-Detail")
	@ResponseBody
	public String getFaultStoreDetail(Integer faultStoreId, Integer userId, String token) {
		Map<String, Object> res = service.selectFaultStoreDetail(faultStoreId);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 首页获取报障中列表
	 * 
	 * @param faultState
	 *            0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 * @param type
	 *            0甲方 1乙方
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/home-fault-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String homeFaultList(Integer faultState, Integer userId, String token, Integer type, Integer startIndex,
			Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		List<FaultInfo> results = service.getFaultInfoListByHome(faultState, userId, type, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 设备进入报障维保列表
	 * 
	 * @param faultState
	 *            0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 * @param type
	 *            0甲方 1乙方
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/equip-fault-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String equipFaultList(Integer faultState, Integer state, Integer userId, String token, Integer type,
			Integer startIndex, Integer pageSize, Integer equipId) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		List<FaultInfo> results = service.getFaultInfoListByEquip(faultState, state, userId, startIndex, pageSize,
				equipId);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 项目进入报障维保列表
	 * 
	 * @param faultState
	 *            0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 * @param type
	 *            0甲方 1乙方
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/project-fault-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String projectFaultList(Integer faultState, Integer state, Integer userId, String token, Integer type,
			Integer startIndex, Integer pageSize, Integer projectId) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		List<FaultInfo> results = service.getFaultInfoListByProject(faultState, state, userId, type, startIndex,
				pageSize, projectId);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 团队负责人获取所有相关报障单列表
	 * 
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/company_principal_fault-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String companyPrincipalfaultList(Integer userId, String token, Integer startIndex, Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		Map<String, Object> results = service.getcompanyPrincipalfaultList(userId, startIndex, pageSize);
		return Utils.toSuccessJsonResults(results);
	}

	/**
	 * 微信小程序新建报障单
	 * 
	 * @param faultInfo
	 * @param token
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/xcx/new-fault-info", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String newFaultInfo(FaultInfo faultInfo) throws IOException {
		Assert.notNull(faultInfo, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getLevel() >= 0 && faultInfo.getLevel() < 3, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getProjectId() != null && faultInfo.getProjectId() > 0, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getServiceTypeId() != null && faultInfo.getServiceTypeId() > 0, Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(faultInfo.getIntroduce()), Constant.EMPTY_INTRODUCE);
		String faultUserName = URLDecoder.decode(faultInfo.getFaultUserName(), "UTF-8");
		String position = URLDecoder.decode(faultInfo.getPosition(), "UTF-8");
		String introduce = URLDecoder.decode(faultInfo.getIntroduce(), "UTF-8");
		faultInfo.setFaultUserName(faultUserName);
		faultInfo.setPosition(position);
		faultInfo.setIntroduce(introduce);
		FaultInfo rsult = service.saveXcxFaultInfo(faultInfo);
		return Utils.toSuccessJsonResults(rsult);
	}

	/**
	 * 获取报障列表
	 * 
	 * @param state
	 *            0全部 1待接收 2已退回 3待处理 4已接收 5待确认 6维修中 7待验收 8待评价 9已完成
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/xcx/fault-list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String faultListByXcx(Integer state, Integer userId, String token, Integer type, Integer startIndex,
			Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		Assert.state(state != null && state >= 0, Constant.PARAMS_ERROR);
		List<FaultInfo> results = service.getFaultInfoListByXcx(state, userId, type, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 故障统计
	 * @time:2017年11月29日 上午11:20:01
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/fault-statistics")
	@ResponseBody
	public String faultStatistics(Integer userId, String startTime, String endTime, Integer state) {
		Assert.state(userId != null, Constant.PARAMS_ERROR);
		Assert.state(startTime != null && endTime != null && !startTime.equals("") && !endTime.equals(""), Constant.PARAMS_ERROR);
		try {
			Date startTimeD = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
			Date endTimeD = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
			int day = DateUtils.getDateDifferent(startTimeD, DateUtils.getAfterDate(1, endTimeD));
			Assert.state(day<=7, "只允许查询7天的数据");
			List<Map<String, Object>> newList=new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> finishList=new ArrayList<Map<String, Object>>();
			for(int i=0;i<day;i++){
				Date dayStart=DateUtils.getAfterDate(i, startTimeD);
				Date dayEnd=DateUtils.getAfterDate(i+1, startTimeD);
				List<FaultInfo> newFaultList=service.getFaultStatistics(userId,dayStart,dayEnd, 0);
				List<FaultInfo> finishFaultList=service.getFaultStatistics(userId, dayStart, dayEnd, 1);
				Map<String, Object> newFaultmap=new HashMap<String,Object>();
				Map<String, Object> finishFaultmap=new HashMap<String,Object>();
				newFaultmap.put("time", DateUtils.getYear(dayStart)+"年"+DateUtils.getMonth(dayStart)+"月"+DateUtils.getDay(dayStart)+"日");
				newFaultmap.put("list", newFaultList);
				newList.add(newFaultmap);
				finishFaultmap.put("time", DateUtils.getYear(dayStart)+"年"+DateUtils.getMonth(dayStart)+"月"+DateUtils.getDay(dayStart)+"日");
				finishFaultmap.put("list", finishFaultList);
				finishList.add(finishFaultmap);
			}
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("newList", newList);
			map.put("finishList", finishList);
			return Utils.toSuccessJsonResults(map);
		} catch (Exception e) {
			return Utils.toFailJson(e.getMessage(), e);
		}
	}
	
	/**
	 * 小程序现场日志
	 * @param faultInfoId
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/xcx/maintainOperate-list")
	@ResponseBody
	public String getXcxMaintainOperateLogList(Integer faultInfoId, Integer userId, String token, Integer startIndex,
			Integer pageSize) {
		Assert.state(faultInfoId != null && faultInfoId > 0, Constant.PARAMS_ERROR);
		FaultInfo faultInfo=service.selectByPrimaryKey(faultInfoId);
		List<MaintainOperateLog> results = service.getMaintainOperateLogList(faultInfoId, startIndex, pageSize);
		int state=service.getIsOperate(userId, faultInfoId, faultInfo.getFaultType());
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("results", results);
		map.put("state", state);
		map.put("faultstate",faultInfo.getState());
		return Utils.toSuccessJsonResults(map);
	}
	
	/**
	 * 小程序保存评价
	 * 
	 * @author:zhugw
	 * @time:2017年4月29日 下午12:11:10
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/xcx/save-evaluate")
	@ResponseBody
	public String saveXcxEvaluate(Evaluate evaluate, String token,String remark) throws IOException {
		remark= URLDecoder.decode(remark, "UTF-8");
		evaluate.setRemark(remark);
		service.saveEvaluate(evaluate);
		return Utils.toSuccessJson("评价成功");
	}
	
	@RequestMapping(value = "/pc/print-faultList")
	@ResponseBody
	public String faultListByPrint(Integer userId,String time,HttpServletRequest request) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String key="pc_"+userId+"_print";
		redisCacheService.putObject(key, df.format(new Date()));
		List<FaultInfo> results = service.getFaultListByPC(userId,time);
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("faultList", results);
		map.put("lastTime", df.format(new Date()));
		return Utils.toSuccessJsonResults(map);
	}
}
