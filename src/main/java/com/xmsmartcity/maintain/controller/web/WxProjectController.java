package com.xmsmartcity.maintain.controller.web;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.xmsmartcity.extra.weixin.WxConstant;
import com.xmsmartcity.extra.weixin.WxUtil;
import com.xmsmartcity.extra.weixin.WxConstant.URL;
import com.xmsmartcity.extra.weixin.pojo.WxToken;
import com.xmsmartcity.extra.weixin.pojo.WxUser;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.ProjectService;
import com.xmsmartcity.maintain.service.RedisCacheService;
import com.xmsmartcity.maintain.service.ServerTypeService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.AES;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
//@RequestMapping("/wx/project")
public class WxProjectController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService service;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ServerTypeService serverTypeService;
	
	@Autowired
	private RedisCacheService redisCache;
	
	/**
	 * 微信初始化项目页面
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:28:54
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/wx/project/go/project-init" , method = RequestMethod.GET)
	public String  goProjectInit(HttpServletRequest request,Model model){
		System.out.println(WxConstant.URL.SERVE_NAME);
		String loc = ServletRequestUtils.getStringParameter(request, "loc", "");
		model.addAttribute("loc", loc);
		if(WxUtil.isWeixinBrowser(request)){
			String url =  WxUtil.goUserAuthorizeURL(URL.SERVE_NAME+request.getContextPath()+"/mp/project/project-init",  loc);
			System.out.println("wxurl=="+url);
			return url;
		}
		model.addAttribute("errorMsg", "请用微信平台打开！");
		return "/wxweb/error-page";
	}
	
	/**
	 * 微信 init create
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:29:10
	 * @param code
	 * @param state
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/mp/project/project-init",method=RequestMethod.GET)
	public String projectInit(String code,String state,Model model,HttpServletRequest request){
		try{
			WxToken wxToken = WxUtil.getWxToken(code);
			WxUser wxUser = WxUtil.getWxUser(wxToken);
			User userVo = userService.getUserByUnionid(wxUser.getUnionid());
			if(userVo==null){
				model.addAttribute("openId", wxUser.getOpenId());
				model.addAttribute("unionid", wxUser.getUnionid());
				return "/wxweb/join-company";
			}
			if (userVo.getCompanyId()!=null && userVo.getCompanyId()!=0) {
				//获取团队名称
				Company company =companyService.selectByPrimaryKey(userVo.getCompanyId());
				userVo.setCompanyName(company.getName());
			}else {
				//未加入团队则跳转到加入团队界面
				model.addAttribute("openId", wxUser.getOpenId());
				model.addAttribute("unionid", wxUser.getUnionid());
				model.addAttribute("userId", userVo.getId());
				return "/wxweb/join-company";
			}
			model.addAttribute("userInfo", userVo);
			WebUtils.setSessionAttribute(request, "userInfo", userVo);
			return "/wxweb/init-project";
		}catch(Exception e){
			return "/wxweb/error-page";
		}
	}
	
	/**
	 * 微信 create project by a or b
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:29:19
	 * @param type
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wx/project/project-create",method=RequestMethod.GET)
	public String goProject(Integer type,Model model,HttpServletRequest request){
		try {
			User userinfo=(User)WebUtils.getSessionAttribute(request, "userInfo");
			model.addAttribute("userInfo", userinfo);
			if (type==0) {
				return "/wxweb/create-project-fault";
			}
			else {
				return "/wxweb/create-project-maintain";
			}
		} 
		catch (Exception e) {
			return "/wxweb/error-page";
		}
	}
	
	/**
	 * 创建项目
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:29:29
	 * @param project
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wx/project/do/project-create",method=RequestMethod.POST)
	@ResponseBody
	public String doCreateProject(Project project,HttpServletRequest request){
		try{
			Assert.state(project != null,Constant.PARAMS_ERROR);
			Assert.state(project.getServices().size() > 0 ,"服务内容不能少一个，请重新添加服务内容！");
			Assert.state(StringUtils.isNotBlank(project.getMaintain_contract()),"合同时间不能为空");
			String phone = project.getOwnerPhone();
			String phone_regex = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
			boolean isTrue = phone.matches(phone_regex);
			Assert.state(isTrue,"手机号格式错误，请重新填写。");
			long date = Long.parseLong(project.getMaintain_contract());
			Date maintainContract = new Date(date);
			project.setMaintainContract(maintainContract);//设置合同时间
			project.setCreateUserId(project.getUserId());
			Project resProject = service.createProject(project);
			//查询创建用户详细信息
			User user=userService.selectByPrimaryKey(resProject.getCreateUserId());
			resProject.setCompanyId(user.getCompanyId());
			List<ServiceType> serviceTypes=serverTypeService.selectServerTypeByProjectId(resProject.getId(), 0);
			resProject.setServices(serviceTypes);
			resProject.setServiceTypeId(serviceTypes.get(0).getId());
			WebUtils.setSessionAttribute(request, "project", resProject);
			return Utils.toSuccessJsonResults(resProject);
		}catch(Exception e){
			return Utils.toFailJson(e.getMessage(), e);
		}
	}
	
	/**
	 * init invite project member
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:29:40
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/wx/project/invite-member" , method = RequestMethod.GET)
	public String toInviteProject(HttpServletRequest request,Model model){
		String loc = ServletRequestUtils.getStringParameter(request, "loc", "");
		model.addAttribute("loc", loc);
		if(WxUtil.isWeixinBrowser(request)){
			String url =  WxUtil.goUserAuthorizeURL(URL.SERVE_NAME+request.getContextPath()+"/mp/wx/project/list",  loc);
			System.out.println("wxurl=="+url);
			return url;
		}
		model.addAttribute("errorMsg", "请用微信平台打开！");
		return "/wxweb/error-page";
	}
	
	/**
	 * to init project list
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:29:47
	 * @param code
	 * @param state
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mp/wx/project/list" , method = RequestMethod.GET)
	public String initProjectList(String code,String state,Model model,HttpServletRequest request){
		try{
			WxToken wxToken = WxUtil.getWxToken(code);
			WxUser wxUser = WxUtil.getWxUser(wxToken);
			User userVo = userService.getUserByUnionid(wxUser.getUnionid());
			if(userVo == null){
				model.addAttribute("openId", wxUser.getOpenId());
				model.addAttribute("unionid", wxUser.getUnionid());
				return "/wxweb/join-company";
			}
			if (userVo.getCompanyId()==null || userVo.getCompanyId()==0) {
				//未加入团队则跳转到加入团队界面
				model.addAttribute("openId", wxUser.getOpenId());
				model.addAttribute("unionid", wxUser.getUnionid());
				model.addAttribute("userId", userVo.getId());
				return "/wxweb/join-company";
			}
			//获取团队名称
			Company company =companyService.selectByPrimaryKey(userVo.getCompanyId());
			userVo.setCompanyName(company.getName());
			List<Project> res = service.selectProjectList(userVo.getId(), 0, 10);
			if(res == null || res.size() == 0){
				model.addAttribute("userInfo", userVo);
				WebUtils.setSessionAttribute(request, "userInfo", userVo);
				return "/wxweb/init-project";
			}
			model.addAttribute("res",Utils.toSuccessJson(res));
			model.addAttribute("code",code);
			redisCache.putObject(code, userVo.getId());
			return "/wxweb/project-list";
		}catch(Exception e){
			return "/wxweb/error-page";
		}
	}
	
	/**
	 * 项目列表
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:29:52
	 * @param code
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/wx/project/list" , method = RequestMethod.POST)
	@ResponseBody
	public String getProjectList(String code,Integer startIndex,Integer pageSize){
		Object str = redisCache.getObject(code);
		if(str != null){
			Integer userId = (Integer)str;
			List<Project> res = service.selectProjectList(userId, startIndex, pageSize);
			return Utils.toSuccessJson(res);
		}
		return Utils.toSuccessJson(null);
	}

	/**
	 * to project invite member page
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:30:06
	 * @param type
	 * @param projectId
	 * @param serviceTypeId
	 * @param isMenu
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wx/project/go/project-invite",method=RequestMethod.GET)
	public String goProjectInvite(String type,Integer projectId,Integer serviceTypeId,String isMenu,Model model,HttpServletRequest request){
		Project project = service.selectByPrimaryKey(projectId);
		Integer referId = type.equals(Constant.APP_TYPE.B) ? serviceTypeId : 0;
		String inviteToken =  projectId+"_0_"+project.getCreateUserId()+"_"+type+"_"+referId;
		inviteToken = AES.encrypt(inviteToken);//AES加密后的参数
		String webPath = "http://"+Constant.hostSite+"/dweibao/join-pm/"+inviteToken;
		model.addAttribute("webPath", webPath);
		model.addAttribute("projectName", project.getName());
		if(isMenu==null || isMenu.equals(""))
			return "/wxweb/project-invite";
		return "/wxweb/project-invite-menu";
	}
}
