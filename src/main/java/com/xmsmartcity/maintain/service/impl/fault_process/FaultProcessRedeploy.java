package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
 * 乙方负责人转派报障单
 * @author:zhugw
 * @time:2017年6月8日 下午5:35:49
 */
@FaultProcessEntity(type = 3)
public class FaultProcessRedeploy extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Assert.state(!param.getUserId().toString().equals(param.getReUserId()),"报障单不能转派给自己,请重新转派!");
		User referUser = service.userDao.selectByPrimaryKey(Integer.valueOf(param.getReUserId()));
		Assert.state(referUser != null,"转派人员错误，请返回重试！");
		IndexUserFault userFault = service.indexUserFaultDao.selectByParams(referUser.getId(), param.getFaultInfoId(), null, 0);
		Assert.state(userFault == null,"该成员已加入维修，请重新转派!");
		//报障单状态是待处理，改为已接收
		if(faultInfo.getState().equals(Constant.FAULT_INFO_STATE.WAIT_CONDUCT)){
			faultInfo.setState(Constant.FAULT_INFO_STATE.RECEIVED);
			service.dao.updateByPrimaryKeySelective(faultInfo);
		}
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(param.getFaultInfoId(),"", "转派工单 给："+referUser.getName(),
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		//删除维修人员并通知请求转派人员 
		List<IndexUserFault> iufs = service.indexUserFaultDao.selectListByParams(param.getFaultInfoId(), null, 0);
		for(IndexUserFault iuf : iufs){
			if(iuf != null){
				iuf.setIsDel(1);
				iuf.setType(0);
				service.indexUserFaultDao.updateByPrimaryKeySelective(iuf);
				User maintainUser = service.userDao.selectByPrimaryKey(iuf.getUserId());
				if(maintainUser != null){
					service.messageService.sendMessage(maintainUser.getPhone(),param.getUserId(),
							"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"负责人移交报障单，你已被移除此报障单快去看看吧~",
							Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
							faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				}
			}
		}
		//增加维修工人 0：普通人
		IndexUserFault oldUf = service.indexUserFaultDao.selectByParams(referUser.getId(),param.getFaultInfoId(),null,null);
		if(oldUf != null){
			oldUf.setIsDel(0);
			oldUf.setType(0);
			service.indexUserFaultDao.updateByPrimaryKeySelective(oldUf);
		}else{
			IndexUserFault indexUserFault = new IndexUserFault(referUser.getId(), param.getFaultInfoId(),Constant.ZERO,new Date(),param.getUserId());
			service.indexUserFaultDao.insertSelective(indexUserFault);
		}
	    //通知转派人员
		service.messageService.sendMessage(referUser.getPhone(),param.getUserId(),
				"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"将报障单转派给您，快去看看吧~",
				Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
				faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		
		//给维修工程师发短信 TODO 增加维修工程师短信模板
		service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
				referUser.getPhone(), user.getId(), faultInfo.getId(), false,null);
		
		//给填写人发信息
		User submitUser=service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if(submitUser != null){
			service.messageService.sendMessage(submitUser.getPhone(),param.getUserId(),
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"转派了报障单，快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
	}

}
