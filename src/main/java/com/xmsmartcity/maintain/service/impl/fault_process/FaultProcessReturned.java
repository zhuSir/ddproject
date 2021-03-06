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
 * 乙方负责人退回报障单 
 * @author:zhugw
 * @time:2017年6月8日 下午5:31:23
 */
@FaultProcessEntity(type=2)
public class FaultProcessReturned extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		
		//拒绝报障单,更改状态
		faultInfo.setState(Constant.FAULT_INFO_STATE.RETURNED);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(param.getFaultInfoId(),"拒绝理由："+param.getReason(), "拒绝报障单",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		//给甲方报障人发消息
		User faultUser = service.userDao.selectByPhone(faultInfo.getFaultUserMobile());
		if(faultUser != null){
			service.messageService.sendMessage(faultInfo.getFaultUserMobile(),param.getUserId(),
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"拒绝了报障单,拒绝理由："+param.getReason(),
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		//给甲方负责人发短信
		service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index, 
				faultInfo.getFaultUserMobile(), param.getUserId(), faultInfo.getId(), false,null);
		
		//给填写人发信息
		User operateUser=service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (operateUser!=null && faultUser!=null && operateUser.getId().intValue()!=faultUser.getId().intValue() && !operateUser.getId().equals(param.getUserId())) {
			service.messageService.sendMessage(operateUser.getPhone(),param.getUserId(),
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"拒绝了报障单,拒绝理由："+param.getReason(),
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		//只有小程序报修单才给提交人发送发短信
		if(faultInfo.getCode().contains("XCX")){
			service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_return_fault_tpl_id_index, 
					operateUser.getPhone(), param.getUserId(), faultInfo.getId(), false,null);
		}
	}

}
