package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 工单评价
 * @author:zhugw
 * @time:2017年6月8日 下午7:48:28
 */
@FaultProcessEntity(type=20)
public class FaultProcessEvaluate extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		
		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.FINISH);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "评价了报障单",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.LOOK_EVALUATE,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		
		//通知乙方负责人
		ServiceType st = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		if(st != null && !st.getMaintainUserMobile().equals(user.getPhone())){
			service.messageService.sendMessage(st.getMaintainUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"评价了,快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
			
			//给乙方负责发短信 TODO finish fault info
			service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index, 
					st.getMaintainUserMobile(), param.getUserId(), faultInfo.getId(), false,null);	
			
		}
		//通知所有乙方人员
		List<IndexUserFault> result = service.indexUserFaultDao.selectListByParams(faultInfoId,null,0);
		for(IndexUserFault re : result){
			User reUser = service.userDao.selectByPrimaryKey(re.getUserId());
			if(reUser != null){
				service.messageService.sendMessage(reUser.getPhone(),userId,
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"评价了,快去看看吧~",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				// TODO 给维修人员发短信 finish fault info
				service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index, 
						reUser.getPhone(), param.getUserId(), faultInfo.getId(), false,null);	
			}
		}
		//给提交人发信息
		User submitUser = service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if(submitUser != null && !submitUser.getPhone().equals(user.getPhone())){			
			service.messageService.sendMessage(submitUser.getPhone(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"评价了,快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
	}

}
