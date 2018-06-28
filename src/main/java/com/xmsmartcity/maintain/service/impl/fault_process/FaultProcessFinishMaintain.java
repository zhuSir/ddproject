package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 完成维修
 * @author:zhugw
 * @time:2017年6月8日 下午7:45:18
 */
@FaultProcessEntity(type=17)
public class FaultProcessFinishMaintain extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		
		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.WAIT_ACCEPTANCE);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "完成维修.",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.LOOK_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		
		//通知甲方负责人
		User faultUser = service.userDao.selectByPhone(faultInfo.getFaultUserMobile());
		if(faultUser != null){
			service.messageService.sendMessage(faultInfo.getFaultUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"完成维修了,快去处理吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);			
		}
		//给甲方负责人发短信
		service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_repaired_tpl_id_index,
				faultInfo.getFaultUserMobile(), user.getId(), faultInfo.getId(), false,null);
		
		//给乙方负责人发短信  TODO 新建模板
		ServiceType serviceType = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		if(serviceType != null && !user.getPhone().equals(serviceType.getMaintainUserMobile())){
			service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
					serviceType.getMaintainUserMobile(), user.getId(), faultInfo.getId(), false,null);
		}
		
		//给填写人发送短信,需判断是否填写人是否是甲乙方人员，如果是则不再次发送
		User operateUser=service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (operateUser!=null && !operateUser.getPhone().equals(faultInfo.getFaultUserMobile()) && !operateUser.getId().equals(userId)) {
			try {
				service.messageService.sendMessage(operateUser.getPhone(),userId,
		    			"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"完成维修了,快去处理吧~",
		    			Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
		    			faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				service.messageService.sendPhoneMsg(15, operateUser.getPhone(), null, faultInfo.getId());
			} catch (IOException e) {
				//暂不处理
			}
		}
	}

}
