package com.xmsmartcity.maintain.service.impl.fault_process;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 乙方负责人拒绝增援
 * @author:zhugw
 * @time:2017年6月8日 下午7:29:40
 */
@FaultProcessEntity(type=7)
public class FaultProcessRefuseReinforce extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"拒绝理由："+param.getReason(), "拒绝增援的申请",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		//通知请求增援人员 
		IndexUserFault uf = service.indexUserFaultDao.selectByParams(null,faultInfoId,2,0);
		if(uf != null){
			uf.setType(0);
			service.indexUserFaultDao.updateByPrimaryKeySelective(uf);
			User maintainUser = service.userDao.selectByPrimaryKey(uf.getUserId());
			if(maintainUser != null){
				service.messageService.sendMessage(maintainUser.getPhone(),userId,
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"拒绝了您的增援申请，拒绝理由："+param.getReason(),
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
			}
		}
	}

}
