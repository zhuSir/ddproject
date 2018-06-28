package com.xmsmartcity.maintain.controller.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.pojo.AbnormalRecords;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.FaultSummary;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.EvaluateService;
import com.xmsmartcity.maintain.service.FaultSummaryService;
import com.xmsmartcity.maintain.service.ProjectService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;
import com.xmsmartcity.util.ExcelUtil;
import com.xmsmartcity.util.Utils;

/**
 * Created by chenby on 2017/4/28.
 */
@Controller
@RequestMapping(value = "/api/faultSummary")
public class FaultSummaryApiController {
	@Autowired
	private FaultSummaryService faultSummaryService;
	@Autowired
	private EvaluateService evaluateService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private UserService userService;

	/**
	 * 获取维保汇总列表
	 * 旧接口，考虑IOS暂时无强制更新而保留，后续版本建议删除  2017/9/13
	 * @param userId
	 * @param token
	 * @param receivetimeStart
	 * @param receivetimeEnd
	 * @return
	 */
	@RequestMapping(value = "/select-maintenanList")
	@ResponseBody
	public String selectMaintenanList(Integer userId, String token, String receivetimeStart, String receivetimeEnd) {
		Assert.state((receivetimeStart != null && receivetimeEnd != null)
				|| (receivetimeStart == null && receivetimeEnd == null), Constant.PARAMS_ERROR);
		Date startTime = null;
		Date endTime = null;
		if (receivetimeStart != null && receivetimeEnd != null) {
			startTime = DateUtils.StringToDate(receivetimeStart, DateUtils.formatStr_yyyyMMdd);
			endTime = DateUtils.StringToDate(receivetimeEnd, DateUtils.formatStr_yyyyMMdd);
		}
		List<FaultSummary> faultSummaries = faultSummaryService.getmaintenanList(userId, startTime, endTime);
		return Utils.toSuccessJson(faultSummaries);
	}

	/**
	 * 获取报修汇总列表
	 * 旧接口，考虑IOS暂时无强制更新而保留，后续版本建议删除  2017/9/13
	 * @param userId
	 * @param token
	 * @param createtimeStart
	 * @param createtimeEnd
	 * @return
	 */
	@RequestMapping(value = "/select-faultList")
	@ResponseBody
	public String selectFaultList(Integer userId, String token, String createtimeStart, String createtimeEnd) {
		Assert.state((createtimeStart != null && createtimeEnd != null)
				|| (createtimeStart == null && createtimeEnd == null), Constant.PARAMS_ERROR);
		Date startTimeD = null;
		Date endTimeD = null;
		if (createtimeStart != null && createtimeEnd != null) {
			startTimeD = DateUtils.StringToDate(createtimeStart, DateUtils.formatStr_yyyyMMdd);
			endTimeD = DateUtils.StringToDate(createtimeEnd, DateUtils.formatStr_yyyyMMdd);
		}
		List<FaultSummary> faultSummaries = faultSummaryService.getFaultList(userId, startTimeD, endTimeD, "0");
		return Utils.toSuccessJson(faultSummaries);
	}

	/**
	 * 获取保修维保汇总列表
	 * 合并select-maintenanList和select-faultList为一个接口，参数名统一
	 * @param userId
	 * @param token
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/select-summaryList")
	@ResponseBody
	public String selectSummaryList(Integer userId, String token, String startTime, String endTime,String type) {
		Assert.state((startTime != null && endTime != null)
				|| (startTime == null && endTime == null), Constant.PARAMS_ERROR);
		Date startTimeD = null;
		Date endTimeD = null;
		if (startTime != null && endTime != null) {
			startTimeD = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
			endTimeD = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
		}
		List<FaultSummary> faultSummaries = faultSummaryService.getFaultList(userId, startTimeD, endTimeD, type);
		return Utils.toSuccessJson(faultSummaries);
	}
	
	/**
	 * 首页汇总
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/select-SummaryNum")
	@ResponseBody
	public String getSummaryNum(Integer userId, String token) {
		Map<String, Object> map = new HashMap<>();
		map.put("maintain", faultSummaryService.getSummaryNum(userId, 1));
		map.put("fault", faultSummaryService.getSummaryNum(userId, 2));
		return Utils.toSuccessJson(map);
	}

	/**
	 * 获取异常事项
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/select-AbnormalThings")
	@ResponseBody
	public String getAbnormalThings(Integer userId, String token) {
		List<Map<String, Object>> abnormalThings = evaluateService.getAbnormalThings(userId);
		return Utils.toSuccessJson(abnormalThings);
	}

	/**
	 * 维保超时异常
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/select-AbnormalByTimeout")
	@ResponseBody
	public String getAbnormalByTimeout(Integer userId, String token) {
		List<Map<String, Object>> abnormalThings = evaluateService.getAbnormalByTimeout(userId);
		return Utils.toSuccessJson(abnormalThings);
	}

	/**
	 * 评分异常
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/select-AbnormalByScore")
	@ResponseBody
	public String getAbnormalByScore(Integer userId, String token) {
		List<Map<String, Object>> abnormalThings = evaluateService.getAbnormalByScore(userId);
		return Utils.toSuccessJson(abnormalThings);
	}

	/**
	 * 已处理异常事项
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/select-AbnormalRecords")
	@ResponseBody
	public String getAbnormalRecords(Integer userId, String token, Integer startIndex, Integer pageSize) {
		startIndex = startIndex * pageSize;
		List<Map<String, Object>> abnormalThings = evaluateService.getAbnormalRecords(userId, startIndex, pageSize);
		return Utils.toSuccessJson(abnormalThings);
	}

	/**
	 * 生成异常事项处理记录
	 * @param userId
	 * @param token
	 * @param abnormalRecords
	 * @return
	 */
	@RequestMapping(value = "/insert-AbnormalRecords")
	@ResponseBody
	public String addAbnormalRecords(Integer userId, String token, AbnormalRecords abnormalRecords) {
		Assert.state(abnormalRecords != null, Constant.PARAMS_ERROR);
		abnormalRecords.setCreatetime(new Date());
		abnormalRecords.setCreateUserId(userId);
		abnormalRecords.setUpdatetime(new Date());
		abnormalRecords.setUpdateUserId(userId);
		abnormalRecords.setFaultState(9);
		abnormalRecords.setOverTime(0);
		int i = evaluateService.insertAbnormalRecords(abnormalRecords, userId);
		return Utils.toSuccessJson(i);
	}

	/**
	 * 首页工作面板
	 * 
	 * @time:2017年7月26日 下午2:26:30
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/home-stat-info")
	@ResponseBody
	public String homeStatInfo(Integer userId, String token) {
		Map<String, Object> res = faultSummaryService.selectHomeStatInfo(userId);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 人员汇总
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/select-statisticsByUser")
	@ResponseBody
	public String getStatisticsByUser(Integer userId, String startTime, String endTime, String type) {
		Assert.state((startTime != null && endTime != null) || (startTime == null && endTime == null),
				Constant.PARAMS_ERROR);
		Assert.state(type != null && (type.equals("0") || type.equals("1")), Constant.PARAMS_ERROR);
		Date startTimeD = null;
		Date endTimeD = null;
		if (startTime != null && endTime != null) {
			startTimeD = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
			endTimeD = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
		}
		List<FaultSummary> List = faultSummaryService.getStatisticsByUser(userId, startTimeD, endTimeD, type);
		return Utils.toSuccessJson(List);
	}

	/**
	 * 下载Excel(人员汇总、项目汇总)
	 * @param userId
	 * @param projectId
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @param downloadType
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/download-excel")
	@ResponseBody
	public String downLoadExcel(Integer userId, Integer projectId, String startTime, String endTime, String type,
			String downloadType, HttpServletResponse response) throws Exception {
		Assert.state(type != null && (type.equals("0") || type.equals("1")), Constant.PARAMS_ERROR);
		Assert.state(downloadType != null && (downloadType.equals("0") || downloadType.equals("1")),
				Constant.PARAMS_ERROR);
		Assert.state((startTime != null && endTime != null) || (startTime == null && endTime == null),
				Constant.PARAMS_ERROR);
		Assert.state(type != null && (type.equals("0") || type.equals("1")), Constant.PARAMS_ERROR);
		
		Date startTimeD = null;
		Date endTimeD = null;
		String month="";//查询月份。主要是为了生成文件名。
		if (startTime != null && endTime != null) {
			startTimeD = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
			endTimeD = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
			month=startTime.substring(5,6);
		}
		String title = "";// 标题
		List<String> row = new ArrayList<>();// 列名
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Object[]> dataList = new ArrayList<Object[]>();//数据
		String fileName="未命名";//文件名
		// String[]
		// row={"序号","项目名称","报修内容","报修时间","报修人","故障地点","维修工程师","维修费用","故障状态","完成时间","维保评价"};
		if (downloadType.equals("0")) {
			row.add("序号");row.add("项目名称");row.add("报修内容");row.add("报修时间");row.add("报修人");
			row.add("故障地点");row.add("维修工程师");row.add("维修费用");row.add("故障状态");row.add("完成时间");row.add("维保评价");
			list = faultSummaryService.excelByProjectId(userId, startTimeD, endTimeD, type, projectId);
			if (projectId!=null) {
				Project project = projectService.selectByPrimaryKey(projectId);
				Assert.state(project != null, "查无该项目");
				title = project.getName()+"项目维保汇总";
				fileName=project.getName()+month;
			}else {
				title="团队项目维保汇总";
				fileName=title+month;
			}
			Object[] objs = null;
			for (int i = 0; i < list.size(); i++) {
				objs = new Object[row.size()];
				objs[0] = i + 1;
				objs[1] = list.get(i).get("projectName");
				objs[2] = list.get(i).get("introduce");
				objs[3] = list.get(i).get("createtime");
				objs[4] = list.get(i).get("createName");
				objs[5] = list.get(i).get("position");
				objs[6] = list.get(i).get("indexUserName");
				objs[7] = list.get(i).get("total_price");
				objs[8] = getFaultState(list.get(i).get("state").toString());
				objs[9] = list.get(i).get("updatetime");
				objs[10] = list.get(i).get("avgScore");
				dataList.add(objs);
			}
		} else if(downloadType.equals("1")) {
			row.add("序号");row.add("维修工程师");row.add("项目名称");row.add("报修内容");row.add("报修时间");row.add("报修人");
			row.add("故障地点");row.add("维修费用");row.add("故障状态");row.add("完成时间");row.add("维保评价");
			list=faultSummaryService.excelByUserId(userId, startTimeD, endTimeD, type);
			User user =userService.selectByPrimaryKey(userId);
			Company company =companyService.selectByPrimaryKey(user.getCompanyId());
			title=company.getName()+"团队人员维保汇总";
			fileName=company.getName()+month;
			Object[] objs = null;
			for (int i = 0; i < list.size(); i++) {
				objs = new Object[row.size()];
				objs[0] = i + 1;
				objs[1] = list.get(i).get("indexUserName");
				objs[2] = list.get(i).get("projectName");
				objs[3] = list.get(i).get("introduce");
				objs[4] = list.get(i).get("createtime");
				objs[5] = list.get(i).get("createName");
				objs[6] = list.get(i).get("position");
				objs[7] = list.get(i).get("total_price");
				objs[8] = getFaultState(list.get(i).get("state").toString());
				objs[9] = list.get(i).get("updatetime");
				objs[10] = list.get(i).get("avgScore");
				dataList.add(objs);
			}
		}
		ExcelUtil excelUtil = new ExcelUtil(title, row, dataList,fileName, response);
		JSONObject object=new JSONObject();
		object.put("length", excelUtil.export());
		return Utils.toSuccessJson(object);
	}

	/**
	 * 获取对应中文报修状态
	 * 
	 * @param state
	 * @return
	 */
	private String getFaultState(String state) {
		String stateStr = "";
		switch (state) {
		case "1":
			stateStr = "待接收";
			break;
		case "2":
			stateStr = "已退回";
			break;
		case "3":
			stateStr = "待处理";
			break;
		case "4":
			stateStr = "已接收";
			break;
		case "5":
			stateStr = "待确认";
			break;
		case "6":
			stateStr = "维修中";
			break;
		case "7":
			stateStr = "待验收";
			break;
		case "8":
			stateStr = "待评价";
			break;
		case "9":
			stateStr = "已完成";
			break;
		default:
			stateStr = "状态有误";
			break;
		}
		return stateStr;
	}

}
