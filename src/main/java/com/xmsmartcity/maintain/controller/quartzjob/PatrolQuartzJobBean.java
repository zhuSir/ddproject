package com.xmsmartcity.maintain.controller.quartzjob;

import java.util.Date;
import java.util.List;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.pojo.PatrolScheme;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecord;
import com.xmsmartcity.maintain.service.PatrolSchemeRecordService;
import com.xmsmartcity.maintain.service.PatrolSchemeService;

public class PatrolQuartzJobBean extends QuartzJobBean {

	private static final Logger logger = LoggerFactory.getLogger(PatrolQuartzJobBean.class);
	
	@Autowired
	PatrolSchemeService patrolSchemeService;
	@Autowired
	PatrolSchemeRecordService patrolSchemeRecordService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDetail detail = context.getJobDetail();
			JobDataMap jobDataMap = detail.getJobDataMap();
			JSONObject jsonObject = JSONObject.parseObject(jobDataMap.getString("data"));
			int id=jsonObject.getInteger("patrolSchemeId");
			PatrolScheme patrolScheme=patrolSchemeService.selectByPrimaryKey(id);
			PatrolSchemeRecord record=new PatrolSchemeRecord();
			record.setPatrolSchemeId(patrolScheme.getId());
			//未考虑巡检人员是公司负责人的情况
			List<PatrolSchemeRecord> patrolSchemeRecords=patrolSchemeRecordService.getListByRecord(record, new Date(), new Date(), patrolScheme.getPatrolUserId(), null, null);
			if (patrolSchemeRecords.size()>0) {
				return;
			}
			record.setCreateUserId(patrolScheme.getCreateUserId());
			record.setCreatetime(new Date());
			record.setUpdatetime(new Date());
			record.setPatrolTime(new Date());
			record.setIsDel(0);
			record.setIsNormal(0);
			record.setState(0);
			record.setUpdateUserId(patrolScheme.getCreateUserId());
			patrolSchemeRecordService.insert(record);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
}
