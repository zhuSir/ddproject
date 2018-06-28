package com.xmsmartcity.maintain.service.impl.fault_process;

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
 * 甲方同意乙方退回
 * @author:zhugw
 * @time:2017年6月8日 下午7:30:39
 */
@FaultProcessEntity(type=8)
public class FaultProcessConsentReturn extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		
		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.FINISH);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "同意退回报账单.",
				Constant.PROJECT_USER_TYPE_INDEX.A_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(),new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		//通知乙方负责人
		ServiceType st = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		if(st != null){
			service.messageService.sendMessage(st.getMaintainUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"同意您申请退回报障单的请求。",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		//通知所有乙方人员
		List<IndexUserFault> result = service.indexUserFaultDao.selectListByParams(faultInfoId,null,0);
		for(IndexUserFault re : result){
			User reUser = service.userDao.selectByPrimaryKey(re.getUserId());
			if(reUser != null){
				service.messageService.sendMessage(reUser.getPhone(),userId,
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"同意了退回报障单的请求。",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
			}
		}
	}

}
