package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.ServiceType;
//import com.xmsmartcity.maintain.pojo.ServiceType;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 验收不合格  
 * @author:zhugw
 * @time:2017年6月9日 上午9:29:04
 */
@FaultProcessEntity(type=19)
public class FaultProcessDisqualification extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		
		//更新工单类型
		faultInfo.setState(Constant.FAULT_INFO_STATE.MAINTAINING);
		service.dao.updateByPrimaryKeySelective(faultInfo);
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "验收不合格,已退回维修。原因:"+param.getReason(),
				Constant.PROJECT_USER_TYPE_INDEX.A_INDEX,Constant.MAINTAIN_STATE_INDEX.LOOK_CHECK_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		if(StringUtils.isNotBlank(param.getPics())){
			maintainInfo.setPics(param.getPics());
		}
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		
		//通知乙方负责人
		ServiceType st = service.serviceTypeDao.selectByPrimaryKey(faultInfo.getServiceTypeId());
		if(st != null){
			service.messageService.sendMessage(st.getMaintainUserMobile(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"验收不合格已退回维修,快去看看吧~",
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
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"验收不合格已退回维修,快去看看吧~",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				
				//给乙方负责发短信 TODO finish fault info
				service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index, 
						reUser.getPhone(), param.getUserId(), faultInfo.getId(), false,null);
			}
		}
		
		User faultUser = service.userDao.selectByPhone(faultInfo.getFaultUserMobile());
		if(faultUser != null && !faultUser.getPhone().equals(user.getPhone())){
			//给甲方负责发短信
			service.messageService.sendMessage(faultUser.getPhone(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"验收不合格已退回维修,快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
		
		//给提交人发信息
		User submitUser = service.userDao.selectByPrimaryKey(faultInfo.getUserId());
		if(submitUser != null && !submitUser.getPhone().equals(user.getPhone())){			
			service.messageService.sendMessage(submitUser.getPhone(),userId,
					"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"验收不合格已退回维修,快去看看吧~",
					Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
					faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
		}
	}

}
