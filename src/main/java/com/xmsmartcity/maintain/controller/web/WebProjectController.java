package com.xmsmartcity.maintain.controller.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.controller.util.UploadHelper;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.ProjectService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/project")
public class WebProjectController {
	
	@Autowired
	private ProjectService service;
	
	@Autowired
	private UploadHelper uploadHelper;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 初始化项目页面
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:25:45
	 * @param userId
	 * @param projectId
	 * @param type
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/info")
	@ResponseBody
	public String projectInfo(Integer userId,Integer projectId,Integer type,Integer pageNo,Integer pageSize){
		Assert.state(userId != null && userId > 0 && projectId != null && projectId > 0, Constant.PARAMS_ERROR);
		Object obj = service.selectProjectInfo(userId,type,projectId,pageNo,pageSize);
		return Utils.toSuccessJsonResults(obj);
	}
	
	/**
	 * web 项目首页 
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public String projectList(Integer userId,Integer pageNo,Integer pageSize){
		Assert.state(userId != null && userId > 0,Constant.PARAMS_ERROR+"：userID错误,为null或为0");
		Object obj = service.selectProjectListByUserId(userId,pageNo,pageSize);
		return Utils.toSuccessJsonResults(obj);
	}
	
	/**
	 * 创建项目
	 * @time:2017年11月7日 上午9:29:14
	 * @param project
	 * @return
	 * @throws IOException 
	 * 
	 */
	@RequestMapping(value="/create")
	@ResponseBody
	public String createProject(Project project,Integer faultType,@RequestParam(required=false) MultipartFile file,
			HttpServletResponse response,HttpServletRequest request) throws IOException{
		Assert.state(project != null,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(project.getMaintain_contract()),"合同时间不能为空");
		List<ServiceType> services = project.getServices();
		services.get(0).setName(project.getName()+"服务");
		switch(faultType){
		case 0 : 
			//甲方创建项目
			User user = userService.selectByPrimaryKey(project.getCreateUserId());
			Assert.state(user != null,Constant.PARAMS_ERROR);
			project.setOwnerName(user.getName());
			project.setOwnerPhone(user.getPhone());
			project.setCompanyId(user.getCompanyId());
			project.setOwnerId(project.getCreateUserId());
			break;
		case 1 ://乙方创建项目，创建人是乙方 ，service里面是甲方
			//设置甲方负责
			project.setOwnerName(services.get(0).getMaintainUserName());
			project.setOwnerPhone(services.get(0).getMaintainUserMobile());
			//设置乙方负责人
			user = userService.selectByPrimaryKey(project.getCreateUserId());
			Assert.state(user != null,Constant.PARAMS_ERROR);
			services.get(0).setMaintainUserMobile(user.getPhone());
			services.get(0).setMaintainUserName(user.getName());
			break;
		case 2 ://乙方创建项目自己维修， createUserId是乙方，service里面为空，填写咚咚客服为乙方
			//设置甲方负责
			project.setOwnerName("咚咚维保云");
			project.setOwnerPhone("4006568189");
			//设置乙方负责人
			user = userService.selectByPrimaryKey(project.getCreateUserId());
			Assert.state(user != null,Constant.PARAMS_ERROR);
			services.get(0).setMaintainUserMobile(user.getPhone());
			services.get(0).setMaintainUserName(user.getName());
			break;
		}
		int sizeLimit = 30 * 1024 * 1024;
		if(file != null){
			JSONObject result = uploadHelper.saveSingleFile(request,file,1,true, sizeLimit);
			if (result.getBooleanValue("success")) {
				project.setPic(result.get("name").toString());
				System.out.println("result :  "+result.get("name").toString());
			}
		}
		long date = Long.parseLong(project.getMaintain_contract());
		Date maintainContract = new Date(date);
		project.setMaintainContract(maintainContract);//设置合同时间
		Project resProject = service.createProject(project);
		return Utils.toSuccessJsonResults(resProject);
	}
	
	/**
	 * 更新项目
	 * @time:2017年11月10日 下午2:44:26
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public String updateProject(Project project,@RequestParam(required=false) MultipartFile file) throws IOException{
		Assert.state(project != null,Constant.PARAMS_ERROR);
		long date = Long.parseLong(project.getMaintain_contract());
		Date maintainContract = new Date(date);
		project.setMaintainContract(maintainContract);//设置合同时间
		service.saveProject(project,project.getUserId());
		return Utils.toSuccessJson("保存成功");
	}

	/**
	 * 搜索项目成员
	 * @param userId
	 * @param searchPhone
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value="/member/search")
	@ResponseBody
	public String searchProjectMember(Integer userId,String searchPhone,Integer projectId){
		List<User> result = service.searchProjectMember(userId,searchPhone,projectId);
		return Utils.toSuccessJson(result);
	}
	
	/**
	 * 根据项目名称搜索项目列表
	 * @author:zhugw
	 * @time:2017年11月29日 上午10:18:22
	 * @param projectName
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/search", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectProjectListForName(String projectName, Integer userId, Integer pageNum,
			Integer pageSize) {
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		Object results = service.searchProjectListByPage(userId,projectName, pageNum, pageSize);
		return Utils.toSuccessJsonResults(results);
	}
	
}
