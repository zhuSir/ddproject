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
 * 乙方负责人拒绝转派
 * @author:zhugw
 * @time:2017年6月8日 下午5:38:58
 */
@FaultProcessEntity(type=5)
public class FaultProcessRefuseRedeploy extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) {
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		
		//加入现场日志
		MaintainOperateLog maintainInfo = new MaintainOperateLog(param.getFaultInfoId(),"拒绝理由："+param.getReason(), "拒绝转派",
				Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
		service.maintainOperateLogDao.insertSelective(maintainInfo);
		//通知请求转派人员 
		IndexUserFault uf = service.indexUserFaultDao.selectByParams(null,param.getFaultInfoId(),1,0);
		uf = uf != null ? uf :service.indexUserFaultDao.selectByParams(null,param.getFaultInfoId(),3,0);
		if(uf != null){
			uf.setType(0);
			service.indexUserFaultDao.updateByPrimaryKeySelective(uf);
			User maintainUser = service.userDao.selectByPrimaryKey(uf.getUserId());
			if(maintainUser != null){
				service.messageService.sendMessage(maintainUser.getPhone(),param.getUserId(),
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"拒绝了您申请的转派报障单，拒绝理由："+param.getReason()+"，快去重新编辑吧~",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,param.getFaultInfoId(),Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(),Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
			}
		}
	}
	
}
