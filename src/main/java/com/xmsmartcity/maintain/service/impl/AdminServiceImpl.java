package com.xmsmartcity.maintain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmsmartcity.maintain.dao.AdminMapper;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.pojo.Admin;
import com.xmsmartcity.maintain.service.AdminService;
import com.xmsmartcity.orm.Page;

@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

	@Autowired
	private AdminMapper adminDao;
	
	public AdminServiceImpl(BaseDao<Admin> dao) {
		super(dao);
	}

	public Admin getAdminInfo(String account,String password){
		Admin admin = new Admin();
		admin.setAccount(account);
		admin.setPassword(password);
		List<Admin> list=adminDao.selectByRecord(admin);
		if (list.size()==0) {
			return null;
		}
		return list.get(0);
	}
	
	public Admin selectNoDelByKey(Integer id){
		return adminDao.selectNoDelByKey(id);
	}
	
	public Page<Admin> queryForAdminPage(Admin admin,int pageNo,int pageSize){
		Page<Admin> page = new Page<Admin>();
    	page.setPageNo(pageNo);
    	page.setPageSize(pageSize);
    	List<Admin> list=adminDao.queryForAdminPage(admin, (pageNo-1)*pageSize, pageSize);
    	page.setList(list);
    	List<Admin> allList=adminDao.selectByRecord(new Admin());
    	int total=allList.size();
    	page.setTotal(total);
    	int totalPage = total / pageSize;
		// 不足不页算一页
		if (total % pageSize > 0){
			totalPage++;
		} 
		page.setTotalPage(totalPage);
		return page;
	}
	
	public Admin find(String account){
		Admin admin = new Admin();
		admin.setAccount(account);
		List<Admin> list=adminDao.selectByRecord(admin);
		return list.size()==0?null:list.get(0);
	}
	
	public void save(Admin admin){
		adminDao.insert(admin);
	}
	
	public boolean checkExist(String account,Integer userId){
		Admin admin = new Admin();
		admin.setAccount(account);
		admin.setId(userId);
		List<Admin> list=adminDao.selectByRecord(admin);
		return list.size()==0?true:false;
	}
	
	public void update(Admin admin){
		adminDao.updateByPrimaryKey(admin);
	}

}
