package com.xmsmartcity.maintain.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.xmsmartcity.maintain.pojo.Project;
import com.xmsmartcity.maintain.pojo.User;

public interface ProjectService  extends BaseService<Project>{

	/**
	 * 根据项目名称搜索项目列表
	 * 
	 * @param projectName
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Project> selectProjectList(Integer userId,String projectName, Integer startIndex, Integer pageSize);

	/**
	 * 创建项目
	 * 
	 * @author:zhugw
	 * @time:2017年4月10日 下午3:54:18
	 * @param project
	 */
	public Project createProject(Project project) throws IOException ;

	/**
	 * 查询项目详情
	 * 
	 * @param userId
	 * @param projectId
	 * @return
	 */
	Project getProjectDetail(Integer userId, Integer projectId,Integer type);

	/**
	 * 删除项目成员
	 * 
	 * @param userId
	 * @param projectId
	 * @param type
	 * @param serviceTypeId
	 * @param delUserId
	 * @return
	 */
	int delUserByUserId(Integer userId, Integer projectId, Integer type, Integer serviceTypeId, String delUserId);

	/**
	 * 邀请成员加入(短信邀请)
	 * @param phone
	 * @param projectId
	 * @param userId
	 */
	void inviteUserJoin(String phone, Integer type, Integer projectId, Integer userId,Integer serviceTypeId)throws IOException;

	/**
	 * 
	 * 邀请成员加入(id邀请)
	 * @author:zhugw
	 * @time:2017年4月11日 下午6:35:44
	 * @param inUserId
	 * @param type
	 * @param projectId
	 * @param userId
	 */
	void inviteUserJoinById(Integer inUserId, Integer type, Integer projectId, Integer userId,Integer serviceTypeId);
	
	/**
	 * 查询我的项目列表
	 * 
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Project> selectProjectList(Integer userId, Integer startIndex, Integer pageSize);

	/**
	 * 更新项目信息
	 * @author:zhugw
	 * @time:2017年4月17日 上午11:10:35
	 * @param project
	 */
	public void saveProject(Project project,Integer userId) throws IOException;

	/**
	 * 获取用户关联的项目及所有服务内容
	 * @author:zhugw
	 * @time:2017年4月19日 下午4:13:17
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Project> selectFaultProjectList(Integer userId, Integer startIndex, Integer pageSize);

	/**
	 * 获取用户
	 * @author:zhugw
	 * @time:2017年4月21日 下午2:31:54
	 * @param type
	 * @param projectId
	 * @param userId
	 * @param serviceTypeId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<User> getProjectMember(Integer type, Integer projectId, Integer userId, Integer serviceTypeId,
			Integer startIndex, Integer pageSize);

	/**
	 * 退出项目
	 * @author:zhugw
	 * @time:2017年4月21日 下午3:45:24
	 * @param userId
	 * @param projectId
	 * @param type
	 * @param serviceTypeId
	 */
	void outOfProject(Integer userId, Integer projectId, Integer type, Integer serviceTypeId);

	/**
	 * 移交项目
	 * @author:zhugw
	 * @time:2017年4月24日 下午3:20:28
	 * @param userId
	 * @param type
	 * @param projectId
	 * @param reUserId
	 * @param serviceTypeId
	 */
	void transferProject(Integer userId, Integer type, Integer projectId, Integer reUserId, Integer serviceTypeId);

	/**
	 * 获取附近项目
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:02:38
	 * @param userId
	 * @param lfDown
	 * @param rgUp
	 * @return
	 */
	List<Project> getNearByProject(Integer userId, String[] lfDown, String[] rgUp);

	/**
	 * 根据手机号移交项目
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:02:52
	 * @param userId
	 * @param type
	 * @param projectId
	 * @param phone
	 * @param serviceTypeId
	 * @throws IOException
	 */
	void transferProjectByPhone(Integer userId, Integer type, Integer projectId, String phone, Integer serviceTypeId) throws IOException;

	/**
	 * 申请加入项目
	 * @author:zhugw
	 * @time:2017年5月8日 上午11:28:54
	 * @param type
	 * @param projectId
	 * @param userId
	 * @param serviceTypeId
	 */
	public void joinProject(Integer type, Integer projectId, Integer userId, Integer serviceTypeId);

	/**
	 * 判断手机号是否同一个团队
	 * @author:zhugw
	 * @time:2017年5月9日 上午11:20:54
	 * @param maintainPhone
	 * @param phone
	 * @return
	 */
	String checkPhoneNum(String maintainPhone, String faultPhone,Integer userId);

	/**
	 * 获取项目概览
	 * @author:zhugw
	 * @time:2017年5月9日 下午7:11:10
	 * @param userId
	 * @param projectId
	 * @param type
	 * @return
	 */
	Map<?, ?> getProjectDetailCollet(Integer userId, Integer projectId, Integer type,Integer startIndex,Integer pageSize);

	/**
	 * 首页附近项目
	 * @author:zhugw
	 * @time:2017年5月16日 下午3:55:03
	 * @param userId
	 * @param lfDown
	 * @param rgUp
	 * @return
	 */
	Map<String,Object> getHomeNearByInfo(Integer userId, String[] lfDown, String[] rgUp);

	/**
	 * 附近是否有项目
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:03:10
	 * @param userId
	 * @param lfDown
	 * @param rgUp
	 * @return
	 */
	int existNearByProject(Integer userId, String[] lfDown, String[] rgUp);

	/**
	 * 获取项目数
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:03:28
	 * @return
	 */
	Integer getProjectCount();

	/**
	 * 申请加入项目成员
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:03:39
	 * @param type
	 * @param userId
	 * @param projectId
	 * @param serviceTypeId
	 * @param reUserId
	 */
	void joinProjectMember(Integer type, Integer userId, Integer projectId, Integer serviceTypeId, Integer reUserId);

	/**
	 * 获取是否第三方报障
	 * @return
	 */
	Map<String, Object> getFaultType(Integer projectId,Integer userId);
	
	/**
	 * web 项目首页
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Object selectProjectListByUserId(Integer userId,Integer pageNo, Integer pageSize);

	/**
	 * web 项目详情
	 * @param userId
	 * @param type
	 * @param projectId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Object selectProjectInfo(Integer userId, Integer type, Integer projectId,Integer pageNo,Integer pageSize);

	/**
	 * 搜索项目成员
	 * @param userId
	 * @param searchPhone
	 * @param projectId
	 * @return
	 */
    List<User> searchProjectMember(Integer userId, String searchPhone, Integer projectId);

    /**
     * 根据项目名称查询项目列表
     * @author:zhugw
     * @time:2017年11月29日 上午10:05:45
     * @param userId
     * @param projectName
     * @param pageNum
     * @param pageSize
     * @return
     */
	Object searchProjectListByPage(Integer userId, String projectName, Integer pageNum, Integer pageSize);

	/**
	 * 项目列表分页，名称搜索
	 * @author:zhugw
	 * @time:2017年11月30日 下午2:56:11
	 * @param userId
	 * @param projectName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Object selectProjectListByPage(Integer userId, String projectName, Integer pageNum, Integer pageSize);
	
	/**
	 * 根据项目名称搜索项目是否已经存在
	 * @param userId
	 * @param name
	 * @param type 
	 * @return
	 */
    Object isExist(Integer userId, String name, Integer type);
}
