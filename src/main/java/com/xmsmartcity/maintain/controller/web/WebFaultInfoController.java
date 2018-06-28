package com.xmsmartcity.maintain.controller.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xmsmartcity.orm.Pagination;
import com.xmsmartcity.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.mysql.fabric.xmlrpc.base.Array;
import com.xmsmartcity.maintain.controller.util.UploadHelper;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.MaintainInfo;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.service.FaultInfoService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/faultInfo")
public class WebFaultInfoController {

	@Autowired
	private FaultInfoService service;

	@Autowired
	private UploadHelper uploadHelper;

	Logger log = Logger.getLogger(WebFaultInfoController.class);

	/**
	 * 添加报障单
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:24:14
	 * @param faultInfo
	 * @param file
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public String addFaultInfo(FaultInfo faultInfo, @RequestParam(required = false) MultipartFile[] file,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		Assert.notNull(faultInfo, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getLevel() != null && faultInfo.getLevel() >= 0 && faultInfo.getLevel() < 3, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getProjectId() != null && faultInfo.getProjectId() > 0, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getServiceTypeId() != null && faultInfo.getServiceTypeId() > 0, Constant.PARAMS_ERROR);
		Assert.state(faultInfo.getIntroduce() != null && StringUtils.isNotBlank(faultInfo.getIntroduce()), Constant.EMPTY_INTRODUCE);
		// 设置响应给前台内容的数据格式
		response.setContentType("text/plain; charset=UTF-8");
		String filePath = uploadImg(file, request);
		faultInfo.setPics(filePath);
		FaultInfo result = service.saveFaultInfo(faultInfo);
		return Utils.toSuccessJsonResults(result);
	}

	/**
	 * 根据时间查询报障单
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:24:25
	 * @param startTime
	 * @param endTime
	 * @param equipId
	 * @return
	 */
	@RequestMapping(value = "/list/byTime")
	@ResponseBody
	public String selectFaultInfoByTime(String startTime, String endTime, Integer equipId) {
		Assert.state(StringUtils.isNotEmpty(startTime), Constant.PARAMS_ERROR + " 开始时间错误！");
		Assert.state(StringUtils.isNotEmpty(endTime), Constant.PARAMS_ERROR + " 结束时间错误！");
		Date startDate = new Date(Long.valueOf(startTime));
		Date endDate = new Date(Long.valueOf(endTime));
		List<FaultInfo> results = service.selectListByEquipIdAndTime(startDate, endDate, equipId);
		return Utils.toSuccessJson(results);
	}

	/**
	 * web报障单页 获取报障单详情
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:25:03
	 * @param faultInfoId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/detail")
	@ResponseBody
	public String faultInfoDetail(Integer faultInfoId, Integer userId) {
		JSONObject result = new JSONObject();
		FaultInfo faultInfo = service.getFaultInfoDetailById(faultInfoId, userId);
		result.put("faultInfo", faultInfo);
		List<MaintainOperateLog> results = service.getMaintainOperateLogList(faultInfoId, null, null);
		result.put("maintainOperateLog", results);
		return Utils.toSuccessJsonResults(result);
	}
	
	/**
	 * 报障维修说明
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:25:11
	 * @param faultInfoId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/maintainInfo/list")
	@ResponseBody
	public String getMaintainInfoList(Integer faultInfoId, Integer pageNum,Integer pageSize) {
		Assert.state(faultInfoId != null && faultInfoId > 0, Constant.PARAMS_ERROR);
		Object results = service.getMaintainInfoByPage(faultInfoId, pageNum, pageSize);
		return Utils.toSuccessJsonResults(results);
	}

	/**
	 * web 报障单列表(个人权限)
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:25:24
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @param projectId
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list/byState")
	@ResponseBody
	public String selectFaultInfoByState(String startTime, String endTime, String state, Integer projectId,
			Integer userId, Integer pageNo, Integer pageSize) {
		Assert.state(StringUtils.isNotEmpty(startTime), Constant.PARAMS_ERROR + " 开始时间错误！");
		Assert.state(StringUtils.isNotEmpty(endTime), Constant.PARAMS_ERROR + " 结束时间错误！");
		Date startDate=null;
		Date endDate=null;
		try {
			 startDate = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
			 endDate = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
		} catch (Exception e) {
			Assert.state(false,"时间格式有误");
		}
		List<Map<String, Object>> results = service.selectWebFaultList(projectId, userId,
				state.equals("0") ? "" : state, startDate, DateUtils.plusDay(endDate, 1), pageNo, pageSize);
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(results);
		return Utils.toSuccessJsonResults(pageInfo);
	}

	/**
	 * web 报障单列表(公司权限)
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @param projectId
	 * @param equipId
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list/byEquip")
	@ResponseBody
	public String selectFaultInfoByEquip(String startTime, String endTime, String state, Integer projectId,Integer equipId, Integer userId,
			Integer pageNo, Integer pageSize) {
		Assert.state(StringUtils.isNotEmpty(startTime), Constant.PARAMS_ERROR + " 开始时间错误！");
		Assert.state(StringUtils.isNotEmpty(endTime), Constant.PARAMS_ERROR + " 结束时间错误！");
		Date startDate = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
		Date endDate = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
		List<Map<String, Object>> results = service.selectWebFaultListByEquip(projectId, equipId, userId,
				state.equals("0") ? "" : state, startDate, endDate, pageNo, pageSize);
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(results);
		return Utils.toSuccessJsonResults(pageInfo);
	}
	
	/**
	 * 保存维修说明
	 * @param userId
	 * @param maintainInfo
	 * @param file
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/maintainInfo/save")
	@ResponseBody
	public String saveMaintainInfo(Integer userId, MaintainInfo maintainInfo,@RequestParam(required=false) MultipartFile[] file,
								   HttpServletResponse response,HttpServletRequest request) throws IOException {
		// 设置响应给前台内容的数据格式
		response.setContentType("text/plain; charset=UTF-8");
		String filePath = uploadImg(file,request);
		maintainInfo.setPics(filePath);
		service.saveMaintainInfo(userId, maintainInfo);
		return Utils.toSuccessJson("保存成功");
	}
	
	/**
	 * 解析上传文件
	 * @param file
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String uploadImg(MultipartFile[] file,HttpServletRequest request) throws IOException {
		String filePath = "";
		// 设置响应给前台内容的数据格式
		int sizeLimit = 30 * 1024 * 1024;
		if (null != file) {
			for (int i = 0; i < file.length; i ++) {
				JSONObject result = uploadHelper.saveSingleFile(request,file[i],4,true, sizeLimit);
				if (!result.getBooleanValue("success")) {
					continue;
				}
				filePath+=(","+result.get("name"));
			}
		}
		System.out.println("---------> "+filePath);
		return filePath.replaceFirst(",","");
	}
	
	
	/**
	 * web datatable报障单列表(公司权限)
	 * @param startTime
	 * @param endTime
	 * @param state
	 * @param projectId
	 * @param equipId
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list/byEquipTable")
	@ResponseBody
	public String selectFaultInfoByEquipTable(String startTime, String endTime, String state, Integer projectId,Integer equipId, Integer userId,
			Integer pageNo, Integer pageSize,Integer draw) {
		Assert.state(StringUtils.isNotEmpty(startTime), Constant.PARAMS_ERROR + " 开始时间错误！");
		Assert.state(StringUtils.isNotEmpty(endTime), Constant.PARAMS_ERROR + " 结束时间错误！");
		Date startDate = DateUtils.StringToDate(startTime, DateUtils.formatStr_yyyyMMdd);
		Date endDate = DateUtils.StringToDate(endTime, DateUtils.formatStr_yyyyMMdd);
		List<Map<String, Object>> results = service.selectWebFaultListByEquip(projectId, equipId, userId,
				state.equals("0") ? "" : state, startDate, endDate, pageNo, pageSize);
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(results);
		JSONObject returnJson=new JSONObject();
		returnJson.put("draw", draw);
		returnJson.put("recordsTotal", pageInfo.getTotal());
		returnJson.put("recordsFiltered", pageInfo.getTotal());
		Object[] data=new Object[pageInfo.getList().size()];
		for(int i=0;i<pageInfo.getList().size();i++){
			Map<String, Object> map=pageInfo.getList().get(i);
			String[] strings={map.get("code").toString(),map.get("fault_user_name").toString(),
					map.get("fault_user_mobile").toString(),map.get("projectName").toString(),
					map.get("createtime").toString(),getFaultState(map.get("state").toString())};
			data[i]=strings;
		}
		returnJson.put("data", data);
		return returnJson.toString();
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
