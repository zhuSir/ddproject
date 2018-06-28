package com.xmsmartcity.maintain.controller.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xmsmartcity.maintain.controller.quartzjob.MissingPatrolJobBean;
import com.xmsmartcity.maintain.controller.quartzjob.RemindPatrolJobBean;
import com.xmsmartcity.maintain.service.SchedulerService;
import com.xmsmartcity.util.Constant;

import net.sf.json.JSONObject;

/**
 * 周期性任务（用于消息周期性推送）
 * @author chenby
 * Date:2017-5-4
 */
@Component
public class ScheduledUtil implements InitializingBean {
	
	@Autowired
	SchedulerService schedulerService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		try{
			if (!schedulerService.isExistJob(Constant.PATROLSCHEME_REMIND_JOB_KEY, Constant.PATROLSCHEME_REMIND_JOB_GROUP)) {
	    		JSONObject jsonObject = new JSONObject();
	    		schedulerService.schedule(Constant.PATROLSCHEME_REMIND_JOB_KEY, Constant.PATROLSCHEME_REMIND_JOB_GROUP,"0 0 18 * * ?",jsonObject.toString(),RemindPatrolJobBean.class);
			}
	    	if (!schedulerService.isExistJob(Constant.PATROLSCHEME_MISSING_JOB_KEY, Constant.PATROLSCHEME_MISSING_JOB_GROUP)) {
	    		JSONObject jsonObject = new JSONObject();
	    		schedulerService.schedule(Constant.PATROLSCHEME_MISSING_JOB_KEY, Constant.PATROLSCHEME_MISSING_JOB_GROUP,"0 0 8,12 * * ?",jsonObject.toString(),MissingPatrolJobBean.class);
			}
		}catch (Exception e) {
			System.out.println("巡检初始化异常:"+e.getStackTrace()); 
		}
	}
}
