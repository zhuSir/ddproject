package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.xmsmartcity.maintain.pojo.Admin;

@Repository
public interface AdminMapper extends BaseDao<Admin>{
    
    /**
     * 验证登陆账号、根据用户名查找用户
     * @param record
     * @return
     */
    List<Admin> selectByRecord(Admin record);
    /**
     * 分页查询管理员
     * @param record
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<Admin> queryForAdminPage(@Param("record")Admin record,@Param("startIndex")Integer startIndex,@Param("pageSize")Integer pageSize);
    
    /**
     * 查找未删除用户
     * @param id
     * @return
     */
    Admin selectNoDelByKey(Integer id);
}