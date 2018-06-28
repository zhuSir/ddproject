package com.xmsmartcity.maintain.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.controller.quartzjob.PatrolQuartzJobBean;
import com.xmsmartcity.maintain.service.SchedulerService;

/**
 * quartz 自定义任务调度
 * 
 * @author chenbinyi
 *
 */
@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

	private static final String NULLSTRING = null;
	private static final Date NULLDATE = null;

	@Autowired
	private Scheduler scheduler;
//	@Autowired
//	private JobDetail jobDetail;

//	@Override
//	public void schedule(String cronExpression) {
//		schedule(NULLSTRING, cronExpression);
//	}
//
//	@Override
//	public void schedule(String name, String cronExpression) {
//		schedule(name, NULLSTRING, cronExpression);
//	}

//	@Override
//	public void schedule(String name, String group, String cronExpression) {
//		try {
//			schedule(name, group, new CronExpression(cronExpression));
//		} catch (ParseException e) {
//			throw new IllegalArgumentException(e);
//		}
//	}

	@Override
	public void schedule(String name, String group, String cronExpression, String data,Class jobBean) {
		try {
			schedule(name, group, new CronExpression(cronExpression), data,jobBean);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

//	@Override
//	public void schedule(CronExpression cronExpression) {
//		schedule(NULLSTRING, cronExpression);
//	}
//
//	@Override
//	public void schedule(String name, CronExpression cronExpression) {
//		schedule(name, NULLSTRING, cronExpression);
//	}
//
//	@Override
//	public void schedule(String name, String group, CronExpression cronExpression) {
//		if (isValidExpression(cronExpression)) {
//
//			if (name == null || name.trim().equals("")) {
//				name = UUID.randomUUID().toString();
//			}
//
//			CronTriggerImpl trigger = new CronTriggerImpl();
//			trigger.setCronExpression(cronExpression);
//
//			TriggerKey triggerKey = new TriggerKey(name, group);
//			trigger.setJobName(jobDetail.getKey().getName());
//			trigger.setKey(triggerKey);
//
//			try {
//				scheduler.addJob(jobDetail, true);
//				if (scheduler.checkExists(triggerKey)) {
//					scheduler.rescheduleJob(triggerKey, trigger);
//				} else {
//					scheduler.scheduleJob(trigger);
//				}
//			} catch (SchedulerException e) {
//				throw new IllegalArgumentException(e);
//			}
//		}
//	}

	@SuppressWarnings("rawtypes")
	@Override
	public void schedule(String name, String group, CronExpression cronExpression, String data,Class jobBean) {

		if (isValidExpression(cronExpression)) {

			if (name == null || name.trim().equals("")) {
				name = UUID.randomUUID().toString();
			}

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).startNow().build();
			JobDetail jobDetail1 = JobBuilder.newJob(jobBean).storeDurably(true)
					.withIdentity(name, group).build();
			jobDetail1.getJobDataMap().put("data", data);

//			AnnualCalendar cal = new AnnualCalendar();
//			Calendar gCal = GregorianCalendar.getInstance();
//			gCal.set(Calendar.MONTH, Calendar.JULY);
//			gCal.set(Calendar.DATE, 4);
//			cal.setDayExcluded(gCal, true);

			try {
//				scheduler.addCalendar("bankHolidays", cal, true, true);
				scheduler.scheduleJob(jobDetail1, trigger);
			} catch (SchedulerException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

//	@Override
//	public void schedule(Date startTime) {
//		schedule(startTime, NULLDATE);
//	}
//
//	@Override
//	public void schedule(Date startTime, String group) {
//		schedule(startTime, NULLDATE, group);
//	}
//
//	@Override
//	public void schedule(String name, Date startTime) {
//		schedule(name, startTime, NULLDATE);
//	}

//	@Override
//	public void schedule(String name, Date startTime, String group) {
//		schedule(name, startTime, NULLDATE, group);
//	}
//
//	@Override
//	public void schedule(Date startTime, Date endTime) {
//		schedule(startTime, endTime, 0);
//	}
//
//	@Override
//	public void schedule(Date startTime, Date endTime, String group) {
//		schedule(startTime, endTime, 0, group);
//	}
//
//	@Override
//	public void schedule(String name, Date startTime, Date endTime) {
//		schedule(name, startTime, endTime, 0);
//	}
//
//	@Override
//	public void schedule(String name, Date startTime, Date endTime, String group) {
//		schedule(name, startTime, endTime, 0, group);
//	}
//
//	@Override
//	public void schedule(Date startTime, int repeatCount) {
//		schedule(null, startTime, NULLDATE, 0);
//	}
//
//	@Override
//	public void schedule(Date startTime, Date endTime, int repeatCount) {
//		schedule(null, startTime, endTime, 0);
//	}
//
//	@Override
//	public void schedule(Date startTime, Date endTime, int repeatCount, String group) {
//		schedule(null, startTime, endTime, 0, group);
//	}
//
//	@Override
//	public void schedule(String name, Date startTime, Date endTime, int repeatCount) {
//		schedule(name, startTime, endTime, 0, 0L);
//	}
//
//	@Override
//	public void schedule(String name, Date startTime, Date endTime, int repeatCount, String group) {
//		schedule(name, startTime, endTime, 0, 0L, group);
//	}
//
//	@Override
//	public void schedule(Date startTime, int repeatCount, long repeatInterval) {
//		schedule(null, startTime, NULLDATE, repeatCount, repeatInterval);
//	}
//
//	@Override
//	public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval) {
//		schedule(null, startTime, endTime, repeatCount, repeatInterval);
//	}
//
//	@Override
//	public void schedule(Date startTime, Date endTime, int repeatCount, long repeatInterval, String group) {
//		schedule(null, startTime, endTime, repeatCount, repeatInterval, group);
//	}
//
//	@Override
//	public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval) {
//		schedule(name, startTime, endTime, repeatCount, repeatInterval, NULLSTRING);
//	}
//
//	@Override
//	public void schedule(String name, Date startTime, Date endTime, int repeatCount, long repeatInterval,
//			String group) {
//
//		if (this.isValidExpression(startTime)) {
//
//			if (name == null || name.trim().equals("")) {
//				name = UUID.randomUUID().toString();
//			}
//
//			TriggerKey triggerKey = new TriggerKey(name, group);
//
//			SimpleTriggerImpl trigger = new SimpleTriggerImpl();
//			trigger.setKey(triggerKey);
//			trigger.setJobName(jobDetail.getKey().getName());
//
//			trigger.setStartTime(startTime);
//			trigger.setEndTime(endTime);
//			trigger.setRepeatCount(repeatCount);
//			trigger.setRepeatInterval(repeatInterval);
//
//			try {
//				scheduler.addJob(jobDetail, true);
//				if (scheduler.checkExists(triggerKey)) {
//					scheduler.rescheduleJob(triggerKey, trigger);
//				} else {
//					scheduler.scheduleJob(trigger);
//				}
//			} catch (SchedulerException e) {
//				throw new IllegalArgumentException(e);
//			}
//		}
//	}

	@Override
	public void pauseTrigger(String triggerName) {
		pauseTrigger(triggerName, NULLSTRING);
	}

	@Override
	public void pauseTrigger(String triggerName, String group) {
		try {
			scheduler.pauseTrigger(new TriggerKey(triggerName, group));// 停止触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void resumeTrigger(String triggerName) {
		resumeTrigger(triggerName, NULLSTRING);
	}

	@Override
	public void resumeTrigger(String triggerName, String group) {
		try {
			scheduler.resumeTrigger(new TriggerKey(triggerName, group));// 重启触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean removeTrigdger(String triggerName) {
		return removeTrigdger(triggerName, NULLSTRING);
	}

	@Override
	public boolean removeTrigdger(String triggerName, String group) {
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		try {
			scheduler.pauseTrigger(triggerKey);// 停止触发器
			return scheduler.unscheduleJob(triggerKey);// 移除触发器
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isValidExpression(final CronExpression cronExpression) {

		CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression(cronExpression);

		Date date = trigger.computeFirstFireTime(null);

		return date != null && date.after(new Date());
	}

	private boolean isValidExpression(final Date startTime) {

		SimpleTriggerImpl trigger = new SimpleTriggerImpl();
		trigger.setStartTime(startTime);

		Date date = trigger.computeFirstFireTime(null);

		return date != null && date.after(new Date());
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray getSchedulingList() {
		JSONArray jsonArray = new JSONArray();
		try {
			for (String groupName : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					JSONObject jsonObject = new JSONObject();
					JobDetail jobDetail = scheduler.getJobDetail(jobKey);
					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();
					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
					Date nextFireTime = triggers.get(0).getNextFireTime();
					Date previousFireTime = triggers.get(0).getPreviousFireTime();
					jsonObject.put("jobName", jobName);
					jsonObject.put("jobGroup", jobGroup);
					jsonObject.put("nextFireTime", nextFireTime);
					jsonObject.put("previousFireTime", previousFireTime);
					jsonObject.put("data", jobDetail.getJobDataMap().get("data"));
					jsonArray.add(jsonObject);
					// System.out.println("[jobName] : " + jobName + "
					// [groupName] : " + jobGroup + " - " + nextFireTime);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return jsonArray;
	}

	@Override
	public void pauseJob(String jobName, String group) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, group);
			scheduler.pauseJob(jobKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void resumeJob(String jobName, String group) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, group);
			scheduler.resumeJob(jobKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void removeJob(String jobName, String group) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, group);
			// 删除任务后，所对应的trigger也将被删除
			scheduler.deleteJob(jobKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isExistJob(String triggerName, String triggerGroup) throws Exception {
		JobKey jobKey = JobKey.jobKey(triggerName, triggerGroup);
		return scheduler.checkExists(jobKey);
	}

	
}
