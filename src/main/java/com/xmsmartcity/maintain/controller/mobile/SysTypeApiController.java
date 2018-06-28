package com.xmsmartcity.maintain.controller.mobile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.pojo.SysType;
import com.xmsmartcity.maintain.pojo.SysTypegroup;
import com.xmsmartcity.maintain.service.SysTypeService;
import com.xmsmartcity.maintain.service.SysTypegroupService;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/sysType")
public class SysTypeApiController {

	@Autowired
	private SysTypegroupService sysTypegroupService;
	@Autowired
	private SysTypeService sysTypeService;
	
	/**
	 * 查询数字字典组
	 * @param userId
	 * @param startIndex
	 * @param record
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/get/sysTypegroupList")
	@ResponseBody
	public String getSysTypegroupList(Integer userId,Integer startIndex,SysTypegroup record,
		Integer pageSize) {
		List<SysTypegroup> res = sysTypegroupService.getListByRecord(record,startIndex, pageSize);
		return Utils.toSuccessJson(res);
	}
	
	/**
	 * 查询数字典
	 * @param userId
	 * @param startIndex
	 * @param record
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/get/sysTypeList")
	@ResponseBody
	public String getSysTypeList(Integer userId,Integer startIndex,SysType record,
			Integer pageSize) {
		List<SysType> res = sysTypeService.getListByRecord(record,startIndex,pageSize);
		return Utils.toSuccessJson(res);
	}
	
}
