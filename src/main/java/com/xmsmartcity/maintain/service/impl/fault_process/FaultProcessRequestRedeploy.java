package com.xmsmartcity.maintain.service.impl.fault_process;

import java.util.Date;

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
 * 乙方请求转派
 * @author:zhugw
 * @time:2017年6月8日 下午7:33:00
 */
@FaultProcessEntity(type=10)
public class FaultProcessRequestRedeploy extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		
		//更新工单类型
		//faultInfo.setState(Constant.FAULT_INFO_STATE.WAIT_CONDUCT);
		//dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "请求转派报账单.理由:"+param.getReason(),
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		IndexUserFault ufObj = service.indexUserFaultDao.selectByParams(userId,faultInfoId, null, 0);
		if(ufObj != null){
			//维修中
			if(Constant.FAULT_INFO_STATE.MAINTAINING == faultInfo.getState()){
				ufObj.setType(3);
			}
			//已接收
			else if(Constant.FAULT_INFO_STATE.RECEIVED == faultInfo.getState()){
				ufObj.setType(1);
			}
			service.indexUserFaultDao.updateByPrimaryKeySelective(ufObj);
		}
		//通知乙方负责人
		ServiceType st = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		if(st != null){
			service.messageService.sendMessage(st.getMaintainUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"申请转派报障单，申请理由:"+param.getReason(),
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
	}

}
