package com.xmsmartcity.maintain.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.MaterialLabour;
@Repository
public interface MaterialLabourMapper extends BaseDao<MaterialLabour>{

    /**
     * 批量保存
     * @author:zhugw
     * @time:2017年4月25日 下午3:44:00
     * @param materias
     */
	void insertBatch(List<MaterialLabour> materias);

	/**
	 * 根据报障单查询
	 * @author:zhugw
	 * @time:2017年5月18日 下午7:02:44
	 * @param faultInfoId
	 * @return
	 */
	List<MaterialLabour> selectListByFaultInfoId(Integer faultInfoId);

	/**
	 * 删除工单费用
	 * @author:zhugw
	 * @time:2017年6月13日 上午8:57:22
	 * @param faultInfoId
	 */
	void deleteByFaultInfo(Integer faultInfoId);
}