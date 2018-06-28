package com.xmsmartcity.maintain.controller.mobile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.controller.util.ApiHelper;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.ProjectService;
import com.xmsmartcity.maintain.service.ServerTypeService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/api/project")
public class PorjectApiController {

	@Autowired
	private ApiHelper helper;

	@Autowired
	private ProjectService service;

	@Autowired
	private ServerTypeService serverTypeService;
	
	/**
	 * 显示用户相关联的项目
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/project-list")
	@ResponseBody
	public String selectProjectList(Integer userId, String token, Integer startIndex, Integer pageSize) {
		List<Project> res = service.selectProjectList(userId, startIndex, pageSize);
		return Utils.toSuccessJson(res);
	}
	
	/**
	 * check 手机号是否为当前用户团队所属成员
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/check-phoneNum")
	@ResponseBody
	public String checkPhoneNumber(Integer userId, String token, String maintainPhone,String faultPhone) {
		Assert.state(StringUtils.isNotBlank(maintainPhone) && StringUtils.isNotBlank(faultPhone)
				, Constant.PARAMS_ERROR);
		String res = service.checkPhoneNum(maintainPhone,faultPhone,userId);
		return Utils.toSuccessJsonRes(res);
	}
	
	/**
	 * 获取用户关联的项目及所有服务内容
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/project-list-fault")
	@ResponseBody
	public String selectFaultProjectList(Integer userId, String token, Integer startIndex, Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		List<Project> res = service.selectFaultProjectList(userId, startIndex, pageSize);
		return Utils.toSuccessJson(res);
	}

	/**
	 * 根据项目名称搜索项目列表
	 * 
	 * @param projectName
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/project-list-search", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String selectProjectListForName(String projectName, Integer userId,Integer startIndex,
			Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		List<Project> results = service.selectProjectList(userId,projectName, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 创建项目(IOS)
	 * 
	 * @param project
	 * @param userId
	 * @param token
	 * @param maintain_contract
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/project-create")
	@ResponseBody
	public String createProject(Project project,String serviceTypes) throws IOException{
		Assert.state(project != null,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(serviceTypes),"服务内容不能少一个，请重新添加服务内容！");
		Assert.state(StringUtils.isNotBlank(project.getMaintain_contract()),"合同时间不能为空");
		JSONArray serverArr = JSONArray.fromObject(serviceTypes);
		List<ServiceType> serverList = JSONArray.toList(serverArr, new ServiceType(), new JsonConfig());
		Assert.state(serviceTypes != null && serviceTypes.length() > 0,"服务内容不能少一个，请重新添加服务内容！");
		project.setServices(serverList);
		long date = Long.parseLong(project.getMaintain_contract());
		Date maintainContract = new Date(date);
		project.setMaintainContract(maintainContract);//设置合同时间
		project.setCreateUserId(project.getUserId());
		Project resProject = service.createProject(project);
		return Utils.toSuccessJsonResults(resProject);
	}
	
	/**
	 * 创建项目(android)
	 * 
	 * @param project
	 * @param userId
	 * @param token
	 * @param maintain_contract
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/create-project")
	@ResponseBody
	public String createProjectObj(Project project) throws IOException {
		Assert.state(project != null,Constant.PARAMS_ERROR);
		Assert.state(project.getServices().size() > 0 ,"服务内容不能少一个，请重新添加服务内容！");
		Assert.state(StringUtils.isNotBlank(project.getMaintain_contract()),"合同时间不能为空");
		long date = Long.parseLong(project.getMaintain_contract());
		Date maintainContract = new Date(date);
		project.setMaintainContract(maintainContract);//设置合同时间
		project.setCreateUserId(project.getUserId());
		Project resProject = service.createProject(project);
		return Utils.toSuccessJsonResults(resProject);
	}
	
	/**
	 * 附近项目
	 * @author:zhugw
	 * @time:2017年4月24日 上午10:50:49
	 * @param userId
	 * @param token
	 * @param lfDownPosition 左下角经纬度（经度,维度）值用逗号隔开
	 * @param rgUpPosition	右上角经纬度（经度,维度）值用逗号隔开
	 * @return
	 */
	@RequestMapping(value = "/project-nearby")
	@ResponseBody
	public String nearbyProject(Integer userId, String token,String lfDownPosition,String rgUpPosition) {
		Assert.state(StringUtils.isNotBlank(lfDownPosition) 
				&& StringUtils.isNotBlank(rgUpPosition),"地址参数错误，请稍后重试！");
		String[] lfDown = lfDownPosition.split(",");
		String[] rgUp = rgUpPosition.split(",");
		Assert.state(lfDown.length > 1 && lfDown.length < 3,"地址参数错误，请稍后重试！");
		for(String left_Down : lfDown){
			Assert.state(StringUtils.isNotBlank(left_Down),"地址参数错误，请稍后重试！");
		}
		Assert.state(rgUp.length > 1 && rgUp.length < 3,"地址参数错误，请稍后重试！");
		for(String right_down : rgUp){
			Assert.state(StringUtils.isNotBlank(right_down),"地址参数错误，请稍后重试！");
		}
		List<Project> results =  service.getNearByProject(userId,lfDown,rgUp);
		return Utils.toSuccessJson(results);
	}
	
	/**
	 * 获取首页附近信息
	 * @author:zhugw
	 * @time:2017年5月16日 下午4:48:12
	 * @param userId
	 * @param token
	 * @param lfDownPosition
	 * @param rgUpPosition
	 * @return
	 */
	@RequestMapping(value = "/nearby-fault-project")
	@ResponseBody
	public String nearbyProjectAndFaultInfo(Integer userId, String token,String lfDownPosition,String rgUpPosition) {
		Assert.state(StringUtils.isNotBlank(lfDownPosition) 
				&& StringUtils.isNotBlank(rgUpPosition),"地址参数错误，请稍后重试！");
		String[] lfDown = lfDownPosition.split(",");
		String[] rgUp = rgUpPosition.split(",");
		Assert.state(lfDown.length > 1 && lfDown.length < 3,"地址参数错误，请稍后重试！");
		for(String left_Down : lfDown){
			Assert.state(StringUtils.isNotBlank(left_Down),"地址参数错误，请稍后重试！");
		}
		Assert.state(rgUp.length > 1 && rgUp.length < 3,"地址参数错误，请稍后重试！");
		for(String right_down : rgUp){
			Assert.state(StringUtils.isNotBlank(right_down),"地址参数错误，请稍后重试！");
		}
		Map<String,Object> results =  service.getHomeNearByInfo(userId,lfDown,rgUp);
		return Utils.toSuccessJsonResults(results);
	}
	
	/**
	 * 获取附近是否有项目
	 * @author:zhugw
	 * @time:2017年5月16日 下午4:48:12
	 * @param userId
	 * @param token
	 * @param lfDownPosition
	 * @param rgUpPosition
	 * @return
	 */
	@RequestMapping(value = "/exist-nearby-project")
	@ResponseBody
	public String existNearbyProject(Integer userId, String token,String lfDownPosition,String rgUpPosition) {
		Assert.state(StringUtils.isNotBlank(lfDownPosition) 
				&& StringUtils.isNotBlank(rgUpPosition),"地址参数错误，请稍后重试！");
		String[] lfDown = lfDownPosition.split(",");
		String[] rgUp = rgUpPosition.split(",");
		Assert.state(lfDown.length > 1 && lfDown.length < 3,"地址参数错误，请稍后重试！");
		for(String left_Down : lfDown){
			Assert.state(StringUtils.isNotBlank(left_Down),"地址参数错误，请稍后重试！");
		}
		Assert.state(rgUp.length > 1 && rgUp.length < 3,"地址参数错误，请稍后重试！");
		for(String right_down : rgUp){
			Assert.state(StringUtils.isNotBlank(right_down),"地址参数错误，请稍后重试！");
		}
		int results =  service.existNearByProject(userId,lfDown,rgUp);
		return Utils.toSuccessJsonRes(String.valueOf(results));
	}
	
	/**
	 * 保存项目(android)
	 * 
	 * @param project
	 * @param userId
	 * @param token
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/project-save", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveProject(@RequestBody Project project) throws IOException {
		helper.validateToken(project.getUserId(), project.getToken());
		Assert.state(project != null,Constant.PARAMS_ERROR);
		Assert.state(project.getServices().size() > 0 ,"服务内容不能少一个，请重新添加服务内容！");
		Assert.state(StringUtils.isNotBlank(project.getMaintain_contract()),"合同时间不能为空");
		long date = Long.parseLong(project.getMaintain_contract());
		Date maintainContract = new Date(date);
		project.setMaintainContract(maintainContract);//设置合同时间
		service.saveProject(project,project.getUserId());
		return Utils.toSuccessJson("保存成功");
	}
	
	/**
	 * 保存项目(IOS)
	 * 
	 * @param project
	 * @param userId
	 * @param token
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/save-project", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String projectSave(Project project,String serviceTypes) throws IOException {
		helper.validateToken(project.getUserId(), project.getToken());
		Assert.state(project != null,Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(serviceTypes),"服务内容不能少一个，请重新添加服务内容！");
		JSONArray serverArr = JSONArray.fromObject(serviceTypes);
		List<ServiceType> serverList = JSONArray.toList(serverArr, new ServiceType(), new JsonConfig());
		Assert.state(serviceTypes != null && serviceTypes.length() > 0,"服务内容不能少一个，请重新添加服务内容！");
		project.setServices(serverList);
		Assert.state(StringUtils.isNotBlank(project.getMaintain_contract()),"合同时间不能为空");
		long date = Long.parseLong(project.getMaintain_contract());
		Date maintainContract = new Date(date);
		project.setMaintainContract(maintainContract);//设置合同时间
		service.saveProject(project,project.getUserId());
		return Utils.toSuccessJson("保存成功");
	}

	/**
	 * 根据项目ID获取服务器内容列表
	 * 
	 * @param projectId
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/service-type-projectId")
	@ResponseBody
	public String selectServerTypeByProjectId(Integer projectId, Integer type, Integer userId, String token) {
		List<ServiceType> results = serverTypeService.selectServerTypeByProjectId(projectId, type);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 保存服务内容
	 * 
	 * @param serverType
	 * @param userId
	 * @param token
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/service-type-save", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveServerType(ServiceType serverType, Integer userId, String token) {
		ServiceType res = serverTypeService.saveServerType(serverType);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 获取项目详情
	 * @author:zhugw
	 * @time:2017年4月12日 上午9:51:10
	 * @param userId
	 * @param token
	 * @param projectId
	 * @param type
	 *            甲方：0/乙方：1
	 */
	@RequestMapping(value = "/project-detail")
	@ResponseBody
	public String getProjectDetail(Integer userId, String token, Integer projectId, Integer type) {
		Assert.state(userId != null && userId > 0 && projectId != null && projectId > 0, Constant.PARAMS_ERROR);
		Project project = service.getProjectDetail(userId, projectId, type);
		return Utils.toSuccessJsonResults(project);
	}
	
	/**
	 * 项目详情中预览
	 * @author:zhugw
	 * @time:2017年5月10日 上午9:47:02
	 * @param userId
	 * @param token
	 * @param projectId
	 * @param type
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/project-detail-collet")
	@ResponseBody
	public String getProjectDetailCollet(Integer userId, String token, Integer projectId, Integer type,Integer startIndex,Integer pageSize) {
		Assert.state(userId != null && userId > 0 && projectId != null && projectId > 0, Constant.PARAMS_ERROR);
		Map<?,?> res = service.getProjectDetailCollet(userId, projectId, type,startIndex,pageSize);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 邀请成员(短信邀请)
	 * 
	 * @author:zhugw
	 * @time:2017年4月11日 下午4:35:11
	 * @param phone 多个用逗号分隔
	 * @param type 0/甲方  1/乙方
	 * @param projectId
	 * @param userId
	 * @param token
	 * @throws IOException 
	 */
	@RequestMapping(value = "/invite-member")
	@ResponseBody
	public String visitMember(String phone, Integer type, Integer projectId, Integer userId, String token,
			Integer serviceTypeId) throws IOException {
		Assert.state(
				StringUtils.isNotBlank(phone) && projectId != null && projectId > 0 && userId != null && userId > 0,
				Constant.PARAMS_ERROR);
		Assert.state(type != null, Constant.PARAMS_ERROR);
		boolean isTrue = type.equals(Constant.APP_TYPE.B) ? (serviceTypeId != null) : true;
		Assert.state(isTrue, Constant.PARAMS_ERROR);
		service.inviteUserJoin(phone, type, projectId, userId,serviceTypeId);
		return Utils.toSuccessJson("邀请成功");
	}
	
	/**
	 * 获取项目成员（移除项目负责人）
	 * @author:zhugw
	 * @time:2017年4月21日 下午3:10:01
	 * @param type
	 * @param projectId
	 * @param userId
	 * @param token
	 * @param serviceTypeId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/project-member")
	@ResponseBody
	public String getProjectMember(Integer type, Integer projectId, Integer userId, String token,Integer serviceTypeId,Integer startIndex,Integer pageSize) {
		Assert.state(type != null, Constant.PARAMS_ERROR);
		boolean isTrue = type.equals(Constant.APP_TYPE.B) ? (serviceTypeId != null) : true;
		Assert.state(isTrue, Constant.PARAMS_ERROR);
		serviceTypeId = serviceTypeId != null && serviceTypeId == 0 ? null: serviceTypeId;
		List<User> results = service.getProjectMember(type, projectId, userId,serviceTypeId,startIndex,pageSize);
		return Utils.toSuccessJson(results);
	}
	
	

	/**
	 * 邀请成员(id邀请)
	 * @author:zhugw
	 * @time:2017年4月11日 下午4:35:11
	 * @param phone
	 * @param type  0/甲方  1/乙方
	 * @param projectId
	 * @param userId
	 * @param token
	 */
	@RequestMapping(value = "/invite-join")
	@ResponseBody
	public String inviteJoin(Integer inUserId, Integer type, Integer projectId, Integer userId, String token,Integer serviceTypeId) {
		Assert.state(projectId != null && projectId > 0 && userId != null && userId > 0, Constant.PARAMS_ERROR);
		Assert.state(inUserId != null, Constant.PARAMS_ERROR);
		Assert.state(type != null, Constant.PARAMS_ERROR);
		service.inviteUserJoinById(inUserId, type, projectId, userId,serviceTypeId);
		return Utils.toSuccessJson("邀请成功");
	}

	/**
	 * 删除成员
	 * @author:zhugw
	 * @time:2017年4月11日 下午7:59:47
	 * @param userId
	 *            用户id
	 * @param token
	 * @param projectId
	 *            项目id
	 * @param type
	 *            类型0甲方 1乙方
	 * @param serviceTypeId
	 *            服务类型id
	 * @param delUserId
	 *            被删除用户id
	 * @return
	 */
	@RequestMapping(value = "/del-member")
	@ResponseBody
	public String delMember(Integer userId, String token, Integer projectId, Integer type, Integer serviceTypeId,
			String delUserId) {
		Assert.state(null != projectId && projectId > 0, Constant.PARAMS_ERROR);
		Assert.state(null != type, Constant.PARAMS_ERROR);
		Assert.state(null != delUserId, Constant.PARAMS_ERROR);
		if (type == 1) {
			Assert.state(null != serviceTypeId && serviceTypeId > 0, Constant.PARAMS_ERROR);
		}
		serviceTypeId = (serviceTypeId != null && serviceTypeId == 0) ? null : serviceTypeId;
		int res = service.delUserByUserId(userId, projectId, type, serviceTypeId, delUserId);
		if (res == 0) {
			return Utils.toFailJson("serviceError_服务器异常，请稍后重试",null);
		}
		return Utils.toSuccessJson("删除成功");
	}
	
	/**
	 * 退出项目
	 * @param userId
	 * @param projectId
	 * @param type
	 *            0甲方 1乙方
	 * @param serviceTypeId
	 * @param response
	 * @throws IOException
	 * 
	 */
	@RequestMapping(value = "/out-of-project")
	@ResponseBody
	public String outOfProject(Integer userId, String token, Integer projectId,
			Integer type, Integer serviceTypeId){
		Assert.state(null != projectId && projectId > 0,
				Constant.PARAMS_ERROR);
		Assert.state(null != type, Constant.PARAMS_ERROR);
		if (type == Constant.APP_TYPE.B) {
			Assert.state(null != serviceTypeId && serviceTypeId > 0,
					Constant.PARAMS_ERROR);
		}
		service.outOfProject(userId,projectId,type,serviceTypeId);
		return Utils.toSuccessJson("已发送退出请求。");
	}
	
	/**
	 * 移交项目
	 * 
	 * @param userId
	 * @param token
	 * @param type
	 *            0甲方 1乙方
	 * @param id
	 *            项目id
	 * @param request
	 * @param response
	 * @throws IOException
	 * @date 2016-10-27 上午11:06:25
	 */
	@RequestMapping("/transfer-project")
	@ResponseBody
	public String transferProject(Integer reUserId, Integer projectId,
			Integer userId, String token, Integer type,Integer serviceTypeId){
		Assert.state(projectId != null, Constant.PARAMS_ERROR);
		Assert.state(null != type, Constant.PARAMS_ERROR);
		if (type == Constant.APP_TYPE.B) {
			Assert.state(null != serviceTypeId && serviceTypeId > 0,
					Constant.PARAMS_ERROR);
		}
		service.transferProject(userId,type,projectId,reUserId,serviceTypeId);
		return Utils.toSuccessJson("已发送信息，等待处理.");
	}
	
	/**
	 * 移交项目
	 * 
	 * @param userId
	 * @param token
	 * @param type
	 *            0甲方 1乙方
	 * @param id
	 *            项目id
	 * @param request
	 * @param response
	 * @throws IOException
	 * @date 2016-10-27 上午11:06:25
	 */
	@RequestMapping("/transfer-project-phone")
	@ResponseBody
	public String transferProjectByPhone(String phone, Integer projectId,
			Integer userId, String token, Integer type,Integer serviceTypeId) throws IOException{
		Assert.state(projectId != null, Constant.PARAMS_ERROR);
		Assert.state(null != type, Constant.PARAMS_ERROR);
		if (type == Constant.APP_TYPE.B) {
			Assert.state(null != serviceTypeId && serviceTypeId > 0,
					Constant.PARAMS_ERROR);
		}
		service.transferProjectByPhone(userId,type,projectId,phone,serviceTypeId);
		return Utils.toSuccessJson("已发送信息，等待处理.");
	}
	
	/**
	 * 申请加入项目
	 * 
	 * @author:zhugw
	 * @time:2017年4月11日 下午4:35:11
	 * @param phone
	 * @param type  0/甲方  1/乙方
	 * @param projectId
	 * @param userId
	 * @param token
	 */
	@RequestMapping(value = "/join-project")
	@ResponseBody
	public String joinPorject(Integer type, Integer projectId, Integer userId, String token,Integer serviceTypeId) {
		Assert.state(projectId != null && projectId > 0 && userId != null && userId > 0, Constant.PARAMS_ERROR);
		Assert.state(type != null, Constant.PARAMS_ERROR);
		serviceTypeId = (serviceTypeId != null && serviceTypeId == 0) ? null : serviceTypeId;
		service.joinProject(type, projectId, userId,serviceTypeId);
		return Utils.toSuccessJson("申请成功");
	}

	/**
	 * 微信小程序根据ID获取项目详情
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/xcx/get-project")
	@ResponseBody
	public String getXcxPorject(Integer serviceId) {
		Assert.state(serviceId > 0, Constant.PARAMS_ERROR);
		Map<String, Object> project=serverTypeService.getSericeById(serviceId);
		return Utils.toSuccessJsonResults(project);
	}
	
	/**
	 * 根据项目名称搜索项目列表
	 * @author:zhugw
	 * @time:2017年11月30日 下午2:58:43
	 * @param projectName
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/project-list/search", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String searchProjectListForName(String projectName, Integer userId,Integer pageNum,
			Integer pageSize) {
		Assert.state(pageNum != null && pageNum > 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		Object result = service.selectProjectListByPage(userId,projectName,pageNum,pageSize);
		return Utils.toSuccessJsonResults(result);
	}
	
	/**
	 * 查询项目甲乙方成员
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "/getProjectUserType")
	@ResponseBody
	public String getProjectUserType(Integer projectId,Integer userId) {
		Assert.state(projectId != null && projectId > 0 && userId != null && userId > 0, Constant.PARAMS_ERROR);
		Map<String, Object> map=service.getFaultType(projectId, userId);
		return Utils.toSuccessJsonResults(map);
	}

	/**
	 * 根据项目名称搜索项目是否已经存在
	 * @param name
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/byName")
	@ResponseBody
	public String selectProjectByName(String name, Integer userId,Integer type) throws IOException {
		Assert.state(StringUtils.isNotBlank(name), Constant.PARAMS_ERROR+" name字段错误，为null或空");
		Assert.state(userId != null && userId != 0, Constant.PARAMS_ERROR+" userId字段错误，为0");
		Object result = service.isExist(userId,name,type);
		return Utils.toSuccessJsonResults(result);
	}
	
}
