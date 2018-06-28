package com.xmsmartcity.maintain.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.controller.quartzjob.PatrolQuartzJobBean;
import com.xmsmartcity.maintain.dao.BaseDao;
import com.xmsmartcity.maintain.dao.EquipsMapper;
import com.xmsmartcity.maintain.dao.PatrolSchemeItemMapper;
import com.xmsmartcity.maintain.dao.PatrolSchemeMapper;
import com.xmsmartcity.maintain.dao.SchemeEquipMapper;
import com.xmsmartcity.maintain.dao.SchemeItemEquipMapper;
import com.xmsmartcity.maintain.pojo.Equips;
import com.xmsmartcity.maintain.pojo.PatrolScheme;
import com.xmsmartcity.maintain.pojo.PatrolSchemeItem;
import com.xmsmartcity.maintain.pojo.SchemeEquip;
import com.xmsmartcity.maintain.pojo.SchemeItemEquip;
import com.xmsmartcity.maintain.service.PatrolSchemeService;
import com.xmsmartcity.maintain.service.SchedulerService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;

@Service
public class PatrolSchemeImpl extends BaseServiceImpl<PatrolScheme> implements PatrolSchemeService {

	@Autowired
	private PatrolSchemeMapper patrolSchemeDao;
	@Autowired
	private PatrolSchemeItemMapper patrolSchemeItemDao;
	@Autowired
	private EquipsMapper equipsDao;
	@Autowired
	SchedulerService schedulerService;
	@Autowired
	private SchemeEquipMapper schemeEquipDao;
	@Autowired
	private SchemeItemEquipMapper schemeItemEquipDao;

	public PatrolSchemeImpl(BaseDao<PatrolScheme> dao) {
		super(dao);
	}

	@Override
	public PatrolScheme saveOrUpdate(PatrolScheme patrolScheme) {
		String equipIds = patrolScheme.getEquipId();
		List<Equips> equips = equipsDao.selectByIds(equipIds);
		List<SchemeEquip> schemeEquips = new ArrayList<>();
		for (Equips equip : equips) {
			Assert.state(equip.getServiceTypeId() != null && equip.getServiceTypeId() != 0, "存在设备未绑定项目");
		}
		// 有ID则为更新
		if (patrolScheme.getId() != null) {
			PatrolScheme patrolSchemeOld = patrolSchemeDao.selectByPrimaryKey(patrolScheme.getId());
			Assert.state(patrolSchemeOld != null, "未找到该巡检任务");
			// 如果巡检时间、周期有变则需要重新制定job任务调度
			boolean isReset = false;
			isReset = (DateUtils.dateDiff(patrolSchemeOld.getCheckTime(), patrolScheme.getCheckTime()) != 0);
			isReset = (isReset && patrolScheme.getPeriod() != patrolSchemeOld.getPeriod());
			if (isReset) {
				schedulerService.removeJob(patrolScheme.getId() + "", Constant.PATROLSCHEME_JOB_GROUP);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("patrolSchemeId", patrolScheme.getId());
				schedulerService.schedule(patrolScheme.getId() + "", Constant.PATROLSCHEME_JOB_GROUP,
						getCronByCheckTime(patrolScheme.getPeriod(), patrolScheme.getCheckTime()),
						jsonObject.toJSONString(),PatrolQuartzJobBean.class);
			}
			patrolSchemeItemDao.deleteByPatrolSchemeId(patrolScheme.getId());
			patrolSchemeItemDao.insertBatch(patrolScheme.getPatrolSchemeItems());
			patrolScheme.setUpdatetime(new Date());
			patrolSchemeDao.updateByPrimaryKeySelective(patrolScheme);

		} else {
			patrolScheme.setCreatetime(new Date());
			patrolScheme.setUpdatetime(new Date());
			patrolSchemeDao.insertSelective(patrolScheme);
			List<PatrolSchemeItem> patrolSchemeItems = patrolScheme.getPatrolSchemeItems();
			for (PatrolSchemeItem patrolSchemeItem : patrolSchemeItems) {
				patrolSchemeItem.setPatrolSchemeId(patrolScheme.getId());
			}
			patrolScheme.setPatrolSchemeItems(patrolSchemeItems);
			patrolSchemeItemDao.insertBatch(patrolScheme.getPatrolSchemeItems());
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("patrolSchemeId", patrolScheme.getId());
			schedulerService.schedule(patrolScheme.getId() + "", Constant.PATROLSCHEME_JOB_GROUP,
					getCronByCheckTime(patrolScheme.getPeriod(), patrolScheme.getCheckTime()),
					jsonObject.toJSONString(),PatrolQuartzJobBean.class);
		}
		// 插入巡检设备关联
		for (Equips equip : equips) {
			SchemeEquip schemeEquip = new SchemeEquip();
			schemeEquip.setCreatetime(new Date());
			schemeEquip.setPatrolSchemeId(patrolScheme.getId());
			schemeEquip.setCreateUserId(patrolScheme.getUpdateUserId());
			schemeEquip.setEquipId(equip.getId());
			schemeEquips.add(schemeEquip);
		}
		// 插入巡检内容设备关联
		List<PatrolSchemeItem> list = patrolScheme.getPatrolSchemeItems();
		for (PatrolSchemeItem patrolSchemeItem : list) {
			schemeItemEquipDao.deleteByPatrolSchemeItemId(patrolSchemeItem.getId());
			List<SchemeItemEquip> itemEquips = new ArrayList<>();
			if (patrolSchemeItem.getEquipId() != null && !patrolSchemeItem.getEquipId().trim().equals("")) {
				String[] equipStr = patrolSchemeItem.getEquipId().split(",");
				for (String string : equipStr) {
					SchemeItemEquip schemeItemEquip = new SchemeItemEquip();
					schemeItemEquip.setCreatetime(new Date());
					schemeItemEquip.setCreateUserId(patrolScheme.getUpdateUserId());
					schemeItemEquip.setEquipId(Integer.parseInt(string));
					schemeItemEquip.setPatrolSchemeItemId(patrolSchemeItem.getId());
					itemEquips.add(schemeItemEquip);
				}
				schemeItemEquipDao.insertBatch(itemEquips);
			}
		}
		schemeEquipDao.deleteByPatrolSchemeId(patrolScheme.getId());
		schemeEquipDao.insertBatch(schemeEquips);
		return patrolScheme;
	}

	@Override
	public List<PatrolScheme> getPatrolSchemeList(PatrolScheme patrolScheme,String ids, Integer startIndex, Integer pageSize) {
		List<PatrolScheme> patrolSchemes = patrolSchemeDao.selectByRecord(patrolScheme,ids, startIndex, pageSize);
		return patrolSchemes;
	}

	@Override
	public List<Map<String, Object>> getPatrolSchemeListByIds(String ids, Integer userId, String equipId) {
		List<Map<String, Object>> result = patrolSchemeDao.selectListByIdsNoToday(ids, userId, equipId);
		return result;
	}

	/**
	 * 1:每天 2：每周 3：每两周 4：每月 5：每年 6：每季度
	 * 
	 * @param typeId
	 * @return
	 */
	private String getCronByCheckTime(int period, Date date) {
		String cron = Integer.parseInt(DateUtils.getSeconds(date)) + " " + Integer.parseInt(DateUtils.getMinutes(date))
				+ " " + Integer.parseInt(DateUtils.getHours(date));
		switch (period) {
		case 1:
			cron += " * * ? *";
			break;
		case 2:
			cron += " ? * " + DateUtils.getWeek(date) + " *";
			break;
		case 3:
			cron += " ? * " + DateUtils.getWeek(date) + "/2 *";
			break;
		case 4:
			cron += " " + Integer.parseInt(DateUtils.getDay(date)) + " * ? *";
			break;
		case 5:
			cron += " " + Integer.parseInt(DateUtils.getDay(date)) + " " + Integer.parseInt(DateUtils.getMonth(date))
					+ " ? *";
			break;
		case 6:
			cron += " * " + Integer.parseInt(DateUtils.getMonth(date)) + "/3 ? *";
			break;
		default:
			break;
		}
		return cron;
	}
}
