package com.xmsmartcity.maintain.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.service.EquipsService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/equips")
public class WebEquipsController {

	@Autowired
	private EquipsService service;
	
	/**
	 * 获取设备列表（web）
	 * @param userId
	 * @param projectId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String selectList(Integer userId,Integer projectId, Integer pageNo,Integer pageSize){
		Assert.state(pageNo > 0,Constant.PARAMS_ERROR+" pageNo参数错误");
		Assert.state(pageSize > 0,Constant.PARAMS_ERROR+" pageSize参数错误");
		Object equipsTypes = service.selectList(userId,projectId,pageNo, pageSize);
		return Utils.toSuccessJsonResults(equipsTypes);
	}

	/**
	 * 删除设备关联项目
	 * @param userId
	 * @param equipId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/join/cancel")
	@ResponseBody
	public String unJoinProject(Integer userId,Integer equipId,Integer projectId){
		Assert.state(equipId !=null && equipId != 0,Constant.PARAMS_ERROR+" equip id 参数错误");
		service.cancelJoinProject(userId,equipId,projectId);
		return Utils.toSuccessJson("取消成功！");
	}
	
	/**
	 * 设备绑定项目
	 * @param userId
	 * @param projectId
	 * @param equipIds
	 * @return
	 */
	@RequestMapping(value="/join/project")
	@ResponseBody
	public String joinEquipForProject(Integer userId,Integer projectId,String equipIds){
		service.updateByPrimaryKeyBatch(equipIds,projectId);
		return Utils.toSuccessJson("添加成功！");
	}

	/**
	 * 查询设备类别列表（web）
	 * @param userId
	 * @param equipsTypeName
	 * @param equipsTypeCode
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/web/select-equipsType")
	@ResponseBody
	public String selectEquipsTypeList(Integer userId,String equipsTypeName,String equipsTypeCode, Integer pageNo,Integer pageSize) {
		Assert.state(pageNo > 0,Constant.PARAMS_ERROR+" pageNo参数错误");
		Assert.state(pageSize > 0,Constant.PARAMS_ERROR+" pageSize参数错误");
		Object equipsTypes=service.selectEquipsTypeListByPage(userId,equipsTypeName,equipsTypeCode,pageNo,pageSize);
		return Utils.toSuccessJsonResults(equipsTypes);
	}
	
	/**
	 * 获取设备列表
	 * （projectId不为空则查询项目关联的设备，projectId为空时查询未关联的公司设备）
	 * @param userId
	 * @param projectId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/relevance/list")
	@ResponseBody
	public String selectRelevanceList (Integer userId,Integer projectId,String equipName, Integer pageNum,Integer pageSize){
		Assert.state(pageNum != null && pageNum > 0,Constant.PARAMS_ERROR+" pageNo参数错误");
		Assert.state(pageSize != null && pageSize > 0,Constant.PARAMS_ERROR+" pageSize参数错误");
		Object equipsTypes = service.selectListByRelevance(equipName,userId,projectId,pageNum, pageSize);
		return Utils.toSuccessJsonResults(equipsTypes);
	}
}
