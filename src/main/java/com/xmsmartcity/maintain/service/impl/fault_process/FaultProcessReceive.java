package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 乙方负责人接收报障单
 * @author:zhugw
 * @time:2017年6月8日 下午5:28:23
 */
@FaultProcessEntity(type=1)
public class FaultProcessReceive extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		//更新工单类型    1：乙方负责人接收报障单
		faultInfo.setState(Constant.FAULT_INFO_STATE.WAIT_CONDUCT);
		faultInfo.setReceivetime(new Date());
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(param.getFaultInfoId(),"", "受理报障单",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		User faultUser = service.userDao.selectByPhone(faultInfo.getFaultUserMobile());
		if(faultUser != null){
			//给甲方负责人发消息
			service.messageService.sendMessage(faultInfo.getFaultUserMobile(),param.getUserId(),
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"接收了报障单等待处理.",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		
		//给甲方负责发短信
		service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_receive_tpl_id_index, 
				faultInfo.getFaultUserMobile(), param.getUserId(), faultInfo.getId(), false,null);			
		
		//给填写人发送短信,需判断是否填写人是否是甲乙方人员，如果是则不再次发送
		User operateUser=service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (operateUser!=null && faultUser!=null && operateUser.getId().intValue()!=faultUser.getId().intValue() && !operateUser.getId().equals(param.getUserId())) {
			try {
				service.messageService.sendMessage(operateUser.getPhone(),param.getUserId(),
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"接收了报障单等待处理.",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				service.messageService.sendPhoneMsg(13, operateUser.getPhone(), null, faultInfo.getId());
			} catch (IOException e) {
				//暂不处理
			}
		}
	}

}
