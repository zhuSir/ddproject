package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 乙方负责人接收分派报障单
 * @author:zhugw
 * @time:2017年6月8日 下午5:30:32
 */
@Service
@FaultProcessEntity(type=0)
public class FaultProcessReceiveAssign extends FaultProcessBase{
	
	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Assert.state(!param.getUserId().toString().equals(param.getReUserId()),"报障单不能转派给自己,请重新转派!");
		User reUser = service.userDao.selectByPrimaryKey(Integer.valueOf(param.getReUserId()));
		Assert.state(reUser != null,"转派人员错误，请返回重试！");
		IndexUserFault userFault = service.indexUserFaultDao.selectByParams(reUser.getId(), param.getFaultInfoId(), null, 0);
		Assert.state(userFault == null,"该成员已加入维修，请重新转派!");
		Assert.state(reUser != null,Constant.PARAMS_ERROR);
		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.RECEIVED);
		faultInfo.setReceivetime(new Date());
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(param.getFaultInfoId(),"", "受理报障单并转派给了："+reUser.getName(),
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		//给甲方负责人发消息
		User faultUser = service.userDao.selectByPhone(faultInfo.getFaultUserMobile());
		if(faultUser != null){
			service.messageService.sendMessage(faultInfo.getFaultUserMobile(),param.getUserId(),
    			"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"接收了报障单,并转派给了"+reUser.getName()+"工程师进行处理.",
    			Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
    			faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		
		//给甲方负责发短信
		service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_receive_tpl_id_index,
				faultInfo.getFaultUserMobile(), user.getId(), faultInfo.getId(), false,null);
		
		//给维修工程师发消息
		service.messageService.sendMessage(reUser.getPhone(),param.getUserId(),
    			"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"接收了报障单并转派给了您,快去处理吧~",
    			Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
    			faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		
		//给维修工程师发短信 TODO 增加维修工程师短信模板
		service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
				reUser.getPhone(), user.getId(), faultInfo.getId(), false,null);
		
		//给填写人发送短信,需判断是否填写人是否是甲乙方人员，如果是则不再次发送
		User operateUser=service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if (operateUser!=null && faultUser!=null && operateUser.getId().intValue()!=faultUser.getId().intValue() && !operateUser.getId().equals(param.getUserId())) {
			try {
				service.messageService.sendMessage(operateUser.getPhone(),param.getUserId(),
		    			"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"接收了报障单,并转派给:"+reUser.getName()+"工程师进行处理.",
		    			Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
		    			faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				service.messageService.sendPhoneMsg(13, operateUser.getPhone(), null, faultInfo.getId());
			} catch (IOException e) {
				//暂不处理
			}
		}
		//增加维修工人 0：普通人
		IndexUserFault indexUserFault = new IndexUserFault(reUser.getId(), param.getFaultInfoId(),Constant.ZERO,new Date(),param.getUserId());
		service.indexUserFaultDao.insertSelective(indexUserFault);
	}

}
