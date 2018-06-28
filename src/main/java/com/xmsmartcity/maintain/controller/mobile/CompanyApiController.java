package com.xmsmartcity.maintain.controller.mobile;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.CompanyService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/company")
public class CompanyApiController {

	@Autowired
	private CompanyService service;
	
	/**
	 * 团队列表搜索
	 * 
	 * @author:zhugw
	 * @time:2017年4月10日 下午12:00:44
	 * @param startIndex
	 * @param pageSize
	 * @param name
	 * @param userId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/company-list")
	@ResponseBody
	public String searchCompanyList(Integer startIndex, Integer pageSize, String name, Integer userId, String token) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		List<Company> results = service.getCompanyList(startIndex, pageSize, name);
		return Utils.toSuccessJson(results);
	}

	@RequestMapping(value = "/company-detail")
	@ResponseBody
	public String searchCompanyInfo(Integer companyId) {
		Company results = service.selectByPrimaryKey(companyId);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 新建团队(废除接口)
	 * @param userId
	 * @param company
	 * @return
	 */
	@RequestMapping(value = "/create-company")
	@ResponseBody
	public String createCompany(Integer userId,Company company) {
		company.setResponserId(userId);
		Assert.state(company != null, Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(company.getAddress()) && StringUtils.isNotBlank(company.getArea())
				&& StringUtils.isNotBlank(company.getCity()) && StringUtils.isNotBlank(company.getName()), Constant.PARAMS_ERROR);
		Company res = service.createCompany(company);
		return Utils.toSuccessJsonResults(res);
	}

	/**
	 * 更新团队信息
	 * 
	 * @author:zhugw
	 * @time:2017年4月10日 下午1:55:47
	 * @param userId
	 * @param token
	 * @param company
	 * @return
	 */
	@RequestMapping("save-company")
	@ResponseBody
	public String saveCompany(Integer userId, String token, Company company) {
		Assert.state(company != null, Constant.PARAMS_ERROR);
		Assert.state(company.getId() != null && !company.getId().equals(0), Constant.PARAMS_ERROR);
		String res = service.saveCompany(company, userId);
		return Utils.toSuccessJson(res);
	}

	/**
	 * 获取团队信息
	 * @param companyId
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/company-info")
	@ResponseBody
	public String getCompany(Integer companyId, Integer userId, Integer startIndex, Integer pageSize) {
		Assert.state(startIndex != null && startIndex >= 0, Constant.PARAMS_ERROR);
		Assert.state(pageSize != null && pageSize > 0, Constant.PARAMS_ERROR);
		Company company = service.getCompanyInfo(companyId,userId,startIndex, pageSize);
		if (company != null) {
			return Utils.toSuccessJsonResults(company);
		}
		return Utils.toFailJson("团队信息不存在，请刷新查询!", null);
	}

	/**
	 * 申请加入某个团队
	 * @param companyId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/apply-join")
	@ResponseBody
	public String applyJoin(Integer companyId, Integer userId) {
		Assert.state(null != companyId && companyId > 0, Constant.PARAMS_ERROR);
		service.applyJoin(companyId, userId);
		return Utils.toSuccessJson("申请成功，请等待确认噢~");
	}

	/**
	 * 团队负责人邀请成员加入团队
	 * @param userId
	 * @param invitePhone
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/invite-join")
	@ResponseBody
	public String inviteJoin(Integer userId,String invitePhone) throws IOException {
		Assert.state(StringUtils.isNotBlank(invitePhone), Constant.PARAMS_ERROR);
		service.inviteJoin(userId, invitePhone);
		return Utils.toSuccessJson("邀请已发送");
	}

	/**
	 * 获取团队成员
	 * @param userId
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list-company-user")
	@ResponseBody
	public String getListCompanyUser(Integer userId,Integer companyId, Integer startIndex,
			Integer pageSize) {
		Assert.state(companyId != null,Constant.PARAMS_ERROR);
		// 团队成员
		List<User> results = service.selectUserByCompanyId(userId, companyId, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 获取团队成员（过滤项目）
	 * @param projectId
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/company-member-filter")
	@ResponseBody
	public String getUnProjectMember(Integer projectId,Integer userId,Integer startIndex,
			Integer pageSize) {
		Assert.state(projectId > 0, Constant.PARAMS_ERROR);
		List<User> results = service.getCompanyMemberFilterProject(projectId, userId, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 获取团队成员（过滤项目）
	 * @param projectId
	 * @param serviceTypeId
	 * @param userId
	 * @param token
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/company-member-filter-leader")
	@ResponseBody
	public String getCompanyMemberUnProjectLeader(Integer projectId,Integer serviceTypeId, Integer userId, String token, Integer startIndex,
			Integer pageSize) {
		Assert.state(projectId != null && projectId > 0, Constant.PARAMS_ERROR);
		List<User> results = service.getCompanyMemberFilterProjectLeader(projectId,serviceTypeId, userId, startIndex, pageSize);
		return Utils.toSuccessJson(results);
	}

	/**
	 * 删除成员
	 * @param userId
	 * @param companyId
	 * @param delUserId
	 * @return
	 */
	@RequestMapping(value = "/del-member")
	@ResponseBody
	public String delMember(Integer userId, Integer companyId,String delUserId) {
		Assert.state(null != companyId && companyId > 0, Constant.PARAMS_ERROR);
		Assert.state(StringUtils.isNotBlank(delUserId), Constant.PARAMS_ERROR);
		if (service.delUserByUserId(userId, companyId, delUserId) == 0) {
			return Utils.toFailJson("服务器异常，请稍后重试", null);
		}
		return Utils.toSuccessJson("删除成功");
	}

	/**
	 * 退出团队
	 * @param userId
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "/left-company")
	@ResponseBody
	public String leftCompany(Integer userId, Integer companyId) {
		Assert.state(null != companyId && companyId > 0, Constant.PARAMS_ERROR);
		if (service.leftCompany(userId, companyId) == 0) {
			return Utils.toFailJson("服务器异常，请稍后重试", null);
		}
		return Utils.toSuccessJson("退出成功");
	}

	/**
	 * 移交团队(用户id)
	 * @param reUserId
	 * @param companyId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/transfer-company-id")
	@ResponseBody
	public String transferCompany(Integer reUserId, Integer companyId, Integer userId) {
		Assert.state(!reUserId.equals(0), Constant.PARAMS_ERROR);
		Assert.state(!companyId.equals(0), Constant.PARAMS_ERROR);
		String res = service.transferCompany(userId, companyId, reUserId);
		return Utils.toSuccessJson(res);
	}

	/**
	 * 移交团队(手机号)
	 * @param phone
	 * @param companyId
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/transfer-company-phone")
	@ResponseBody
	public String transferCompanyByPhone(String phone, Integer companyId, Integer userId) throws IOException {
		Assert.state(StringUtils.isNotBlank(phone), Constant.PARAMS_ERROR);
		Assert.state(!companyId.equals(0), Constant.PARAMS_ERROR);
		String res = service.transferCompany(userId, companyId, phone);
		return Utils.toSuccessJson(res);
	}
	
	/**
	 * 根据公司名称搜索公司是否存在
	 * @param phone
	 * @param companyId
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/byName")
	@ResponseBody
	public String selectCompanyByName(String name, Integer userId) throws IOException {
		Assert.state(StringUtils.isNotBlank(name), Constant.PARAMS_ERROR+" name字段错误，为null或空");
		Assert.state(userId != 0, Constant.PARAMS_ERROR+" userId字段错误，为0");
		Object result = service.isExist(userId,name);
		return Utils.toSuccessJsonResults(result);
	}

}
