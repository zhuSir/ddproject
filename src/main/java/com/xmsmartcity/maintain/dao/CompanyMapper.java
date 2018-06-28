package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.Company;

@Repository
public interface CompanyMapper extends BaseDao<Company>{

    int updateByPrimaryKeyWithBLOBs(Company record);

    List<Company> selectListByName(@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize,@Param("name")String name);
    
    List<Company> selectList(@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
    
    Company selectByUserId(Integer userId);

	Company selectCollect(Integer companyId);

	int selectCount();

	List<Company> selectByResponserPhone(String phone);
	
	Company findNoDel(Integer userId);
	
	List<Company> queryForCompanyPage(@Param("record")Company record,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);

	/**
	 * 根据负责人Id查询团队
	 * @author:zhugw
	 * @param userId
	 * @return
	 */
	Company selectByResponserId(Integer userId);

	/**
	 * 根据公司名称搜索
	 * @author:zhugw
	 * @time:2017年11月21日 下午9:03:09
	 * @param name
	 * @return
	 */
	Company selectCompanyByName(@Param("name")String name);

	/**
	 * 根据公司名称搜索列表（返回分页对象）
	 * @author:zhugw
	 * @time:2017年11月21日 上午9:42:35
	 * @param searchName
	 * @param rowBounds
	 * @return
	 */
	List<Company> selectListByCompanyName(@Param("name")String searchName, RowBounds rowBounds);
}