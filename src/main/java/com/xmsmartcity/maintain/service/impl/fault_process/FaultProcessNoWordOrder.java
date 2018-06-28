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
 * 无需工单
 * @author:zhugw
 * @time:2017年6月8日 下午7:35:23
 */
@FaultProcessEntity(type=12)
public class FaultProcessNoWordOrder extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();

		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.MAINTAINING);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "无需工单，进入维修中.",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		
		//通知乙方负责人
		ServiceType st = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		if(st != null && !st.getMaintainUserMobile().equals(user.getPhone())){
			service.messageService.sendMessage(st.getMaintainUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"无需工单，进入维修中.快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(),Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		//通知甲方负责人
		service.messageService.sendMessage(faultInfo.getFaultUserMobile(),userId,
				"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"无需工单，进入维修中.快去看看吧~",
				Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
				faultInfo.getProjectId(),Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		
		//给填写人发送短信,需判断是否填写人是否是甲乙方人员，如果是则不再次发送
		User operateUser=service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (operateUser!=null && !operateUser.getPhone().equals(faultInfo.getFaultUserMobile()) && !operateUser.getPhone().equals(user.getPhone()) && !operateUser.getPhone().equals(st.getMaintainUserMobile())) {
			try {
				if(operateUser.getId() != user.getId()){
					service.messageService.sendMessage(operateUser.getPhone(),userId,
							"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"无需工单，进入维修中.快去看看吧~",
							Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
							faultInfo.getProjectId(),Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				}
				service.messageService.sendPhoneMsg(14, operateUser.getPhone(), null, faultInfo.getId());
			} catch (IOException e) {
				//暂不处理
			}
		}
	}

}
