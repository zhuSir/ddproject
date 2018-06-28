package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 乙方处理报障单
 * @author:zhugw
 * @time:2017年6月8日 下午5:37:30
 */
@FaultProcessEntity(type=4)
public class FaultProcessOperate extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Assert.state(StringUtils.isNotBlank(param.getAppointmentTime()),"未选择预约处理时间,请选择预约处理时间。");
		Long appointment_time = Long.valueOf(param.getAppointmentTime());
		Date appointment_date = new Date();
		appointment_date.setTime(appointment_time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String appointmentTime = sdf.format(appointment_date);
		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.RECEIVED);
		faultInfo.setReceivetime(new Date());
		faultInfo.setAppointmentTime(appointment_date);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(param.getFaultInfoId(),"", "受理报障单,预约处理时间："+appointmentTime,
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		
		User faultUser = service.userDao.selectByPhone(faultInfo.getFaultUserMobile());
		if(faultUser != null){
			//给甲方负责人发消息
			service.messageService.sendMessage(faultInfo.getFaultUserMobile(),param.getUserId(),
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"受理报障单,预约处理时间："+appointmentTime+",快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);			
		}
		/*给维修工程师发消息
		service.messageService.sendMessage(user.getPhone(),param.getUserId(),"【报障单 "+faultInfo.getCode()+"】您受理了报障单,预约处理时间："+a_time+",快去处理吧~",
    			Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
    			faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		*/
		
		ServiceType serviceType = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		//给乙方项目负责人发信息
		if(serviceType != null && !user.getPhone().equals(serviceType.getMaintainUserMobile())){			
			service.messageService.sendMessage(serviceType.getMaintainUserMobile(),param.getUserId(),
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"受理报障单,预约处理时间："+appointmentTime+",快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		
		//给填写人发送短信,需判断是否填写人是否是甲乙方人员，如果是则不再次发送
		User operateUser=service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (operateUser != null && !operateUser.getPhone().equals(faultInfo.getFaultUserMobile()) && !operateUser.getPhone().equals(user.getPhone())) {
			try {
				if(faultInfo.getUserId() != user.getId()){
					service.messageService.sendMessage(operateUser.getPhone(),param.getUserId(),
							"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"受理报障单,预约处理时间："+appointmentTime+",快去看看吧~",
							Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
							faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				}
				service.messageService.sendPhoneMsg(16, operateUser.getPhone(), null, faultInfo.getId());
			} catch (IOException e) {
			}
		}
		
		//增加维修工人 0：普通人
		IndexUserFault indexUserFault = new IndexUserFault(param.getUserId(), param.getFaultInfoId(),Constant.ZERO,new Date(),param.getUserId());
		service.indexUserFaultDao.insertSelective(indexUserFault);
	}

}
