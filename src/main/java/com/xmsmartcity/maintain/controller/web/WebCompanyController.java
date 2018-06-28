package com.xmsmartcity.maintain.controller.web;

import java.io.IOException;
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
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/company")
public class WebCompanyController{
	
	@Autowired
	private CompanyService service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UploadHelper uploadHelper;
	
	/**
	 * web 公司搜索成员
	 * @author:zhugw
	 * @time:2017年10月14日 上午10:22:51
	 * @param userId
	 * @param companyId
	 * @param searchPhone
	 * @return
	 */
	@RequestMapping(value="/member/search")
	@ResponseBody
	public String searchMember(Integer userId,Integer companyId,String searchPhone){
		Assert.state(companyId != null && companyId > 0,Constant.PARAMS_ERROR+"：companyID错误,为null或为0");
		Assert.state(userId != null && userId > 0,Constant.PARAMS_ERROR+"：userID错误,为null或为0");
		List<User> user = userService.selectCompanyMember(userId,companyId,searchPhone);
		return Utils.toSuccessJson(user);
	}
	
	/**
	 * web初始化公司页面
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:23:15
	 * @param userId
	 * @param companyId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/init")
	@ResponseBody
	public String initData(Integer userId,Integer companyId,Integer pageNo,Integer pageSize){
		Assert.state(companyId != null && companyId > 0,Constant.PARAMS_ERROR+"：companyID错误,为null或为0");
		Assert.state(userId != null && userId > 0,Constant.PARAMS_ERROR+"：userID错误,为null或为0");
		Object res = service.selectCompanyInfo(userId,companyId,pageNo,pageSize);
		return Utils.toSuccessJsonResults(res);
	}
	
	/**
	 * web 创建团队
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:23:33
	 * @param company
	 * @param file
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/create")
	@ResponseBody
	public String createCompany(Company company,@RequestParam(required=false) MultipartFile[] file,
			HttpServletResponse response,HttpServletRequest request) throws IOException{
		Assert.state(company != null, Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(company.getAddress()) && StringUtils.isNotBlank(company.getArea())
				&& StringUtils.isNotBlank(company.getCity()) && StringUtils.isNotBlank(company.getName()), Constant.PARAMS_ERROR);
		// 设置响应给前台内容的数据格式
		response.setContentType("text/plain; charset=UTF-8");
		int sizeLimit = 30 * 1024 * 1024;
		if(file != null && file.length > 0){
			JSONObject result = uploadHelper.saveSingleFile(request,file[0],2,true, sizeLimit);
			if (result.getBooleanValue("success")) {
				company.setHeadPic(result.get("name").toString());
			}
			if(file.length > 1){
				result = uploadHelper.saveSingleFile(request,file[0],2,true, sizeLimit);
				if (result.getBooleanValue("success")) {
					company.setThreeCertificates(result.get("name").toString());
				}
			}
		}
		if(company.getId() != null){
			String res = service.saveCompany(company,company.getResponserId());
			return Utils.toSuccessJson(res);
		}else{			
			Company res = service.createCompany(company);
			return Utils.toSuccessJsonResults(res);
		}
	}
	
	/**
	 * web搜索公司名称
	 * @author:zhugw
	 * @time:2017年10月14日 上午10:22:51
	 * @param userId
	 * @param companyId
	 * @param searchPhone
	 * @return
	 */
	@RequestMapping(value="/list/search")
	@ResponseBody
	public String searchCompanyList(Integer userId,String searchName,Integer pageNum,Integer pageSize){
		Assert.state(userId != null && userId > 0,Constant.PARAMS_ERROR+"：userID错误,为null或为0");
		Assert.state(pageNum > 0,Constant.PARAMS_ERROR+" pageNum参数错误");
		Assert.state(pageSize > 0,Constant.PARAMS_ERROR+" pageSize参数错误");
		Object results = service.selectListByCompanyName(searchName,pageNum,pageSize);
		return Utils.toSuccessJsonResults(results);
	}
}