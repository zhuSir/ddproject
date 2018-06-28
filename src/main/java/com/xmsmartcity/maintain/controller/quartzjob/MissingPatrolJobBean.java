package com.xmsmartcity.maintain.controller.quartzjob;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.xmsmartcity.maintain.pojo.PatrolSchemeRecord;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.maintain.service.PatrolSchemeRecordService;
import com.xmsmartcity.util.DateUtils;

/**
 * 漏巡检当天8:00和12:00发送提醒
 * @author chenbinyi
 *
 */
public class MissingPatrolJobBean extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(MissingPatrolJobBean.class);
	
	@Autowired
	private PatrolSchemeRecordService patrolSchemeRecordService;
	@Autowired
	private MessageService messageService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fmt1 = new SimpleDateFormat("HH");
			Date nowDate=DateUtils.getDayOnlyYYYYMMDD(new Date());
			String nowDateString=fmt.format(nowDate).toString();
			PatrolSchemeRecord selectRecord =new PatrolSchemeRecord();
			selectRecord.setState(0);
			List<PatrolSchemeRecord>  patrolSchemeRecords = patrolSchemeRecordService.getListByRecord(selectRecord, nowDate, nowDate, null, null, null);
			for (PatrolSchemeRecord patrolSchemeRecord : patrolSchemeRecords) {
				Date checkTime= patrolSchemeRecord.getCheckTime();
				net.sf.json.JSONObject jsonObject=new net.sf.json.JSONObject();
				jsonObject.put("type", 101);
				jsonObject.put("userId", patrolSchemeRecord.getPatrolUserId());
				jsonObject.put("equipName", patrolSchemeRecord.getEquipName());
				jsonObject.put("checkTime", nowDateString.replaceFirst("-", "年").replaceFirst("-", "月")+"日"+fmt1.format(checkTime)+"时");
				String newTimeStr = fmt1.format(nowDate).replaceFirst("^0*", "");
				String checkTimeStr = fmt1.format(checkTime).replaceFirst("^0*", "");
				jsonObject.put("overTime", Integer.parseInt(newTimeStr)-Integer.parseInt(checkTimeStr));
				jsonObject.put("phone", patrolSchemeRecord.getPatrolUserMobile());
				jsonObject.put("id", patrolSchemeRecord.getId());
				messageService.pushMessage(jsonObject);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
}
