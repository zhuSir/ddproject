package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.ServiceType;

@Repository
public interface ServiceTypeMapper extends BaseDao<ServiceType> {
    
    List<ServiceType> selectByProjectId(Integer projectId);
    
    List<ServiceType> selectByProjectIdAndUserId(@Param("projectId")Integer projectId,@Param("userId") Integer userId);
    
    /**
     * 批量保存服务内容
     * @author:zhugw
     * @time:2017年4月11日 上午9:34:03
     * @param services
     * @return
     */
	int insertBatch(List<ServiceType> services);

	/**
	 * 根据projectId和甲乙方查询
	 * @author:zhugw
	 * @time:2017年5月8日 上午11:06:52
	 * @param projectId
	 * @param type
	 * @return
	 */
	List<ServiceType> selectListByParams(@Param("projectId") Integer projectId, @Param("type") Integer type,@Param("userId") Integer userId);
	
	ServiceType selectByParams(@Param("projectId") Integer projectId, @Param("type") Integer type,@Param("userId") Integer userId,@Param("serviceTypeId") Integer serviceTypeId);
	
	List<ServiceType> selectListByUnEqUser(@Param("projectId") Integer projectId, @Param("userId") Integer userId,@Param("companyId") Integer companyId);

	List<ServiceType> selecyByMaintainUserPhone(String phone);

	List<ServiceType> selectByCompanyPrincipal(@Param("projectId") Integer projectId, @Param("type") Integer type,@Param("companyId") Integer companyId);
	
	Map<String, Object> getSericeById(@Param("id") Integer id);
}