package com.xmsmartcity.maintain.controller.quartzjob;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.pojo.PatrolScheme;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.maintain.service.PatrolSchemeService;
import com.xmsmartcity.maintain.service.SchedulerService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;

/**
 * 巡检计划提前一天发送提醒
 * @author chenbinyi
 *
 */
public class RemindPatrolJobBean extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(RemindPatrolJobBean.class);
	
	@Autowired
	SchedulerService schedulerService;
	@Autowired
	private PatrolSchemeService patrolSchemeService;
	@Autowired
	private MessageService messageService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			JSONArray arry = schedulerService.getSchedulingList();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fmt1 = new SimpleDateFormat("HH");
			Date tomorrowDate=DateUtils.plusDay(new Date(), 1);
			String tomorrowDateString=fmt.format(tomorrowDate).toString();
			String ids = "";
			
			List<PatrolScheme> patrolSchemes = new ArrayList<>();
			for (Object object : arry) {
				JSONObject jsonObject = (JSONObject) object;
				if (Constant.PATROLSCHEME_JOB_GROUP.equals(jsonObject.getString("jobGroup"))) {
					Date nextTime = jsonObject.getDate("nextFireTime");
					String id = jsonObject.getString("jobName");
					// 下一次巡检任务时间为明天
					boolean isNext = false;
					isNext = tomorrowDateString.equals(fmt.format(nextTime).toString());
					if (isNext) {
						ids += (id+",");
					}
				}
			}
			if (!ids.equals("")) {
				patrolSchemes = patrolSchemeService.getPatrolSchemeList(new PatrolScheme(),ids.substring(0, ids.length()-1),null,null);
			}
			for (PatrolScheme patrolScheme : patrolSchemes) {
				Date checkTime= patrolScheme.getCheckTime();
				net.sf.json.JSONObject jsonObject=new net.sf.json.JSONObject();
				jsonObject.put("type", 100);
				jsonObject.put("userId", patrolScheme.getPatrolUserId());
				jsonObject.put("equipName", patrolScheme.getEquipName());
				jsonObject.put("checkTime", tomorrowDateString.replaceFirst("-", "年").replaceFirst("-", "月")+"日"+fmt1.format(checkTime)+"时");
				jsonObject.put("overTime", "");
				jsonObject.put("phone", patrolScheme.getPatrolUserMobile());
				jsonObject.put("id", patrolScheme.getId());
				messageService.pushMessage(jsonObject);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
}
