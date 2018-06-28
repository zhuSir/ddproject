package com.xmsmartcity.maintain.service;

import java.io.IOException;
import java.util.List;

import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.orm.Page;

public interface CompanyService extends BaseService<Company>{

	/**
	 * 获取团队信息
	 * @param id
	 * @return
	 */
	public Company getCompanyInfo(Integer id,Integer userId,Integer startIndex,Integer pageSize);
	
	/**
	 * 根据团队名称搜索团队列表
	 * @param startIndex
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public List<Company> getCompanyList(Integer startIndex,Integer pageSize,String name);
	
	/**
	 * 创建团队
	 * @param company
	 * @return
	 */
	public Company createCompany(Company company);

	/**
	 * 申请加入团队
	 * @param companyId
	 * @param userId
	 */
	public void applyJoin(Integer companyId, Integer userId);
	
	/**
	 * 获取团队成员
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @param userId 
	 * @return
	 */
	public List<User> selectUserByCompanyId(Integer userId,Integer companyId,Integer startIndex,Integer pageSize);

	/**
	 * 删除团队成员
	 * @param userId
	 * @param companyId
	 * @param delUserId
	 * @return
	 */
	public int delUserByUserId(Integer userId, Integer companyId, String delUserId);

	/**
	 * 邀请成员加入团队
	 * @param userId
	 * @param invitePhone
	 * @throws IOException 
	 */
	public void inviteJoin(Integer userId, String invitePhone) throws IOException;

	/**
	 * 退出团队
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public int leftCompany(Integer userId, Integer companyId);

	/**
	 * 
	 * 保存团队信息
	 * @author:zhugw
	 * @time:2017年4月10日 下午1:49:40
	 * @param company
	 * @param userId
	 * @return
	 */
	public String saveCompany(Company company, Integer userId);

	/**
	 * 移交团队
	 * @author:zhugw
	 * @time:2017年4月10日 下午2:31:59
	 * @param userId
	 * @param companyId
	 * @param reUserId
	 * @return
	 */
	public String transferCompany(Integer userId, Integer companyId, Integer reUserId);
	
	
	/**
	 * 移交团队
	 * @author:zhugw
	 * @time:2017年4月10日 下午2:31:59
	 * @param userId
	 * @param companyId
	 * @param phone
	 * @return
	 */
	public String transferCompany(Integer userId, Integer companyId, String phone) throws IOException ;

	/**
	 * 获取团队成员过滤掉项目成员
	 * @author:zhugw
	 * @time:2017年5月17日 下午3:12:25
	 * @param projectId
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<User> getCompanyMemberFilterProject(Integer projectId, Integer userId, Integer startIndex,
			Integer pageSize);
	
	public Object getCompanyCount();

	/**
	 * 过滤掉项目负责人
	 * @author:zhugw
	 * @time:2017年5月26日 上午10:25:40
	 * @param projectId
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<User> getCompanyMemberFilterProjectLeader(Integer projectId,Integer serviceTypeId, Integer userId, Integer startIndex,
			Integer pageSize);
	
	
	public Company getCompanyDetail(Integer id); 
	
	public Page<Company> queryForCompanyPage(Company company,int pageNo,int pageSize);

	public void joinCompanyMember(String phone,Integer userId,Integer companyId,Integer reUserId);

	/**
	 * 根据名称获取公司
	 * @author:zhugw
	 * @time:2017年11月21日 下午9:01:16
	 * @param userId
	 * @param name
	 * @return
	 */
	public Object isExist(Integer userId, String name);

	/*
	 * init web company model
	 */
	public Object selectCompanyInfo(Integer userId, Integer companyId, Integer pageNo, Integer pageSize);

	/**
	 * 根据公司名称搜索公司是否存在
	 * @author:zhugw
	 * @time:2017年11月21日 上午9:38:50
	 * @param userId
	 * @param searchName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Object selectListByCompanyName(String searchName, Integer pageNum, Integer pageSize);
	
}
