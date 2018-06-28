package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.User;

@Repository
public interface UserMapper extends BaseDao<User>{

    int updateByPrimaryKeyWithBLOBs(User record);

    User selectByPhone(String phone);
    
    User selectByPhoneAndPsw(@Param("phone")String phone,@Param("psw")String psw);

    int selectByUserIdAndMail(@Param("userId")Integer userId,@Param("mail") String mail);
    
    List<User> selectUsersByCompanyId(@Param("companyId")Integer companyId,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
    
    List<User> selectUsersByParams(@Param("userId")Integer userId,@Param("companyId")Integer companyId,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);

	List<String> selectUserOpenIdsByIds(List<Integer> ids);
	/**
	 * 获取团队用户列表
	 * @author:zhugw
	 * @time:2017年4月19日 下午4:54:06
	 * @param userId
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<User> selectUsersByCidAndUid(@Param("userId")Integer userId,@Param("companyId")Integer companyId,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);

	/**
	 * 获取项目用户
	 * @author:zhugw
	 * @time:2017年4月21日 下午2:49:28
	 * @param projectId
	 * @param userId
	 * @param type
	 * @param serviceTypeId
	 * @return
	 */
	public List<User> selectListByParames(@Param("projectId") Integer projectId,@Param("userId") Integer userId,@Param("type") Integer type,@Param("serviceTypeId") Integer serviceTypeId,@Param("startIndex")Integer startIndex,@Param("pageSize") Integer pageSize);

	/**
	 * 获取维修工程师
	 * @author:zhugw
	 * @time:2017年5月3日 上午9:48:09
	 * @param id
	 * @param i
	 * @return
	 */
	public List<User> selectMaintainUsers(Integer faultInfoId);
	
	public List<User> selectCompamyMemberFileterProject(@Param("companyId")Integer companyId,@Param("projectId") Integer projectId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);
	
	public User selectByOpenId(String openId);
	
	public User selectByUnionId(String unionid);
	
	Integer selectCount();

	/**
	 * 获取团队成员，过滤该项目负责人
	 * @author:zhugw
	 * @time:2017年5月26日 上午10:31:17
	 * @param companyId
	 * @param projectId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<User> selectCompamyMemberFileterProjectLeader(@Param("companyId")Integer companyId,@Param("projectId") Integer projectId,@Param("serviceTypeId")Integer serviceTypeId,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);
	
	/**
	 * 获取所在团队成员
	 * @author:cby
	 * @param user
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<User> queryForCompanyUserPage(@Param("uesr")User user,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);
	
	User selectByXcxOpenId(String xcxOpenId);

	/**
	 * 查询人员动态
	 * @author:zhugw
	 * @time:2017年9月8日 下午3:43:06
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> selectUserDynamic(@Param("id")Integer id,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);

	/**
	 * 查询公司成员
	 * @time:2017年10月13日 下午2:27:07
	 * @param companyId
	 * @param searchPhone
	 * @return
	 */
	List<User> selectCompanyMember(@Param("companyId")Integer companyId,@Param("searchPhone") String searchPhone);

	List<User> selectPageCompanyMember(@Param("companyId") Integer companyId, RowBounds rowBounds);

	/*
	 * web 页面个人中心
	 */
	Map<String, Object> selectUserInfo(Integer userId);

	/**
	 * 根据系统token查询用户信息
	 * @author:zhugw
	 * @time:2017年11月15日 上午9:44:15
	 * @return
	 */
	User selectUserBySystemToken(@Param("token")String token);

	/**
	 * 搜索项目成员
	 * @param projectId
	 * @param searchPhone
	 * @return
	 */
    List<User> selectSearchProjectMember(@Param("projectId") Integer projectId,@Param("searchPhone") String searchPhone);
}