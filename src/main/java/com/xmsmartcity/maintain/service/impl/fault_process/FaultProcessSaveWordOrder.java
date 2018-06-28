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
 * 填写工单
 * @author:zhugw
 * @time:2017年6月8日 下午7:34:12
 */
@FaultProcessEntity(type=11)
public class FaultProcessSaveWordOrder extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		
		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.WAIT_CONFIRM);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "填写了工单.",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.LOOK_WORK_ORDER,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		//通知甲方负责人
		User faultUser = service.userDao.selectByPhone(faultInfo.getFaultUserMobile());
		if(faultUser != null){
			service.messageService.sendMessage(faultInfo.getFaultUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"提交了费用工单,快去处理吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		
		//给甲方负责发短信
		service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_submit_work_order_tpl_id_index,
				faultInfo.getFaultUserMobile(), user.getId(), faultInfo.getId(), false,null);
		
		//给提交人发信息
		User submitUser = service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if(submitUser != null && !submitUser.getPhone().equals(user.getPhone())){
			service.messageService.sendMessage(submitUser.getPhone(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"提交了费用工单,快去处理吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);			
		}
		
		//通知乙方负责人
		ServiceType serviceType = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		if(serviceType != null && !serviceType.getMaintainUserMobile().equals(user.getPhone())){
			service.messageService.sendMessage(serviceType.getMaintainUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"提交了费用工单,快去处理吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		
	}

}
