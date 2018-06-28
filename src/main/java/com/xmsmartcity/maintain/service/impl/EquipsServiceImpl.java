package com.xmsmartcity.maintain.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.CompanyMapper;
import com.xmsmartcity.maintain.dao.EquipsMapper;
import com.xmsmartcity.maintain.dao.EquipsTypeMapper;
import com.xmsmartcity.maintain.dao.ServiceTypeMapper;
import com.xmsmartcity.maintain.dao.UserMapper;
import com.xmsmartcity.maintain.pojo.Company;
import com.xmsmartcity.maintain.pojo.Equips;
import com.xmsmartcity.maintain.pojo.EquipsType;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.EquipsService;
import com.xmsmartcity.orm.Pagination;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Util;

@Service
public class EquipsServiceImpl extends BaseServiceImpl<Equips> implements EquipsService {

	public EquipsServiceImpl(BaseDao<Equips> dao) {
		super(dao);
	}

	@Autowired
	private EquipsMapper equipsDao;
	
	@Autowired
	private EquipsTypeMapper equipsTypeDao;
	
	@Autowired
	private CompanyMapper companyDao;
	
	@Autowired
	private UserMapper userDao;
	
	@Autowired
	private ServiceTypeMapper serviceTypeDao;
	
	/**
	 * 创建或更新设备
	 * 设备编号生成规则：公司拼音缩写+公司ID+5位设备编号
	 */
	@Override
	public Equips saveOrUpdateEquips(Equips equips) {
		if (equips.getId()!=null) {
			Equips oldEquip=equipsDao.selectByPrimaryKey(equips.getId());
			equips.setEquipCode(oldEquip.getEquipCode());
			equips.setCreattime(oldEquip.getCreattime());
			equips.setCreatUserId(oldEquip.getCreatUserId());
			equipsDao.updateByPrimaryKeySelective(equips);
		}else {
			User user=userDao.selectByPrimaryKey(equips.getCreatUserId());
			Company company=companyDao.selectByPrimaryKey(user.getCompanyId());
			String PYname=Util.getUpperPinYinHeadChar(company==null?user.getName():company.getName());
			String equipCode=PYname+company.getId();
			Integer newCode = getNewCode(equipCode);
			DecimalFormat df = new DecimalFormat("00000");
			equipCode+=df.format(newCode);
			equips.setEquipCode(equipCode);
			equips.setIsDel(0);
			equipsDao.insertSelective(equips);
		}
		return equips;
	}

	/**
	 * 获取下个编码
	 * @param PYname
	 * @return
	 */
	@Override
	public Integer getNewCode(String PYname) {
		String maxCode=equipsDao.selectEquipCodeMax(PYname);
		String regEx="[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(maxCode);
		Integer newCode=Integer.parseInt(m.replaceAll("").trim());
		newCode=newCode+1;
		return newCode;
	}

	/**
	 * 批量插入设备
	 * @param equips
	 */
	@Override
	public void insertEquipBatch(List<Equips> equips) {
		equipsDao.insertBatch(equips);
	}

	@Override
	public List<Equips> getEquipsList(Integer startIndex, Integer pageSize, Equips equips, Integer userId) {
		return equipsDao.getEquipsList(startIndex, pageSize,equips,userId);
	}

	@Override
	public void deleteEquips(Equips equips) {
		equipsDao.deleteById(equips);
	}

	@Override
	public List<EquipsType> selectEquipsTypeList(Integer userId,Integer startIndex, Integer pageSize, String name) {
		List<EquipsType> equipsTypeList=equipsTypeDao.selectEquipsTypeList(userId,startIndex, pageSize,name);
		return equipsTypeList;
	}

	@Override
	public Equips selectByPrimaryKey(Integer id) {
		return equipsDao.selectByPrimaryKey(id);
	}

	@Override
	public Map<String, Object> selectProjectByEquip(Integer id,Integer userId) {
		return equipsDao.selectProjectByEquip(id,userId);
	}

	@Override
	public List<Map<String, Object>> getProcessingByEquip(Integer userId, Integer id,Integer startIndex, Integer pageSize) {
		return equipsDao.getProcessingByEquip(userId, id,startIndex,pageSize);
	}

	@Override
	public Map<String, Object> getSummaryByEquip(Integer userId, Integer id) {
		return equipsDao.getSummaryByEquip(userId, id);
	}

	@Override
	public Integer getEquipsAvg(Integer id) {
		return equipsDao.getEquipsAvg(id);
	}

	@Override
	public Integer getEquipsSumTime(Integer id) {
		return equipsDao.getEquipsSumTime(id);
	}

	@Override
	public Map<String, Object> getServiceByEquipCode(String equipCode) {
		return equipsDao.getServiceByEquipCode(equipCode);
	}

	/*
	 * web 设备列表页面
	 */
	@Override
	public Object selectList(Integer userId,Integer projectId, Integer pageNo, Integer pageSize) {
		List<Map<String, Object>> result = equipsDao.selectListByPage(userId,projectId,new Pagination<Map<String, Object>>(pageNo,pageSize).getRowBounds());
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(result);
		return pageInfo;
	}

	/**
	 * 批量更新设备
	 * @author:zhugw
	 * @time:2017年11月17日 下午2:49:54
	 * @param equipIds
	 * @param projectId
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.EquipsService#updateByPrimaryKeyBatch(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void updateByPrimaryKeyBatch(String equipIds, Integer projectId) {
		List<ServiceType> result = serviceTypeDao.selectByProjectId(projectId);
		Assert.state(result != null && result.size() > 0,Constant.PARAMS_ERROR+" project id error");
		Integer serviceTypeId = result.get(0).getId();
		String[] ids = equipIds.split(",");
		List<String> equipId = Arrays.asList(ids);
		equipsDao.updateByPrimaryKeyBatch(serviceTypeId,equipId);
	}
	
	@Override
	public void deleteEquips(EquipsType equipsType){
		equipsTypeDao.deleteByPrimaryKey(equipsType.getId());
	}

	/**
	 * 创建设备类别
	 * 设备编号生成规则：公司拼音缩写+公司ID+5位设备类别编号
	 */
	@Override
	public EquipsType saveOrUpdateEquipsType(EquipsType equipsType,Integer userId) {
		if (equipsType.getId()!=null) {
			EquipsType oldEquip=equipsTypeDao.selectByPrimaryKey(equipsType.getId());
			equipsType.setCreateUserId(oldEquip.getCreateUserId());
			equipsType.setCreatetime(oldEquip.getCreatetime());
			equipsType.setEquipsTypeCode(oldEquip.getEquipsTypeCode());
			equipsType.setUpdatetime(new Date());
			equipsType.setUpdateUserId(userId);
			equipsTypeDao.updateByPrimaryKeySelective(equipsType);
		}else {
			equipsType.setCreatetime(new Date());
			equipsType.setCreateUserId(userId);
			equipsType.setUpdatetime(new Date());
			equipsType.setUpdateUserId(userId);
			equipsTypeDao.insertSelective(equipsType);
		}
		return equipsType;
	}

	/**
	 * 取消设备关联项目
	 * @param userId
	 * @param equipId
	 * @param projectId
	 * @return
	 */
	@Override
	public void cancelJoinProject(Integer userId, Integer equipId, Integer projectId) {
		Equips obj = equipsDao.selectByParams(null,equipId,null);
		if(obj != null){			
			obj.setServiceTypeId(0);
			equipsDao.updateByPrimaryKeySelective(obj);
		}
	}

	/**
	 * 获取设备类别列表（web分页加载）
	 * @param userId
	 * @param name
	 * @param code
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
	public Object selectEquipsTypeListByPage(Integer userId, String name, String code, Integer pageNo, Integer pageSize) {
		List<Map<String, Object>> result = equipsTypeDao.selectEquipsTypeListByPage(userId,name,code,new Pagination<Map<String, Object>>(pageNo,pageSize).getRowBounds());
		Pagination<Map<String, Object>> pageInfo = new Pagination<Map<String, Object>>(result);
		return pageInfo;
	}

	@Override
	@Transactional
	public void deleteEquipsType(Integer id) {
		equipsDao.unJoinByEquipTypeId(id);
		equipsTypeDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insertEquipTypeBatch(List<EquipsType> equipsTypes) {
		equipsTypeDao.insertBatch(equipsTypes);
		
	}

	@Override
	public List<EquipsType> getEquipTypeListByName(String names) {
		return equipsTypeDao.selectEquipsTypeListByNames(names);
	}

	@Override
	public List<Equips> getByServiceTypeId(Integer serviceTypeId, Integer startIndex, Integer pageSize) {
		return equipsDao.selectByServiceTypeId(serviceTypeId, startIndex, pageSize);
	}
	
	/*
	 * 查询关联设备
	 */
	@Override
	public Object selectListByRelevance(String equipName,Integer userId, Integer projectId, Integer pageNo, Integer pageSize) {
		User user = userDao.selectByPrimaryKey(userId);
		Assert.state(user != null,Constant.PARAMS_ERROR);
		List<Map<String,Object>> results = equipsDao.selectListByRelevance(equipName,user.getCompanyId(),projectId,new Pagination<>(pageNo, pageSize).getRowBounds());
		Pagination<Map<String,Object>> pageInfo = new Pagination<Map<String,Object>>(results);
		return pageInfo;
	}

	@Override
	public List<Equips> listBySearch(String equipName,Integer serviceTypeId, Integer startIndex, Integer pageSize) {
		return equipsDao.listBySearch(equipName,serviceTypeId, startIndex, pageSize);
	}

}
