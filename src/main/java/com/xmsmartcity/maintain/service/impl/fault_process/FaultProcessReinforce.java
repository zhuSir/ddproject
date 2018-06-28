package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.xmsmartcity.maintain.common.FaultProcessEntity;
import com.xmsmartcity.maintain.pojo.FaultInfo;
import com.xmsmartcity.maintain.pojo.IndexUserFault;
import com.xmsmartcity.maintain.pojo.MaintainOperateLog;
import com.xmsmartcity.maintain.pojo.ProjectUser;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.impl.FaultInfoServiceImpl;
import com.xmsmartcity.util.Constant;

/**
 * 乙方负责人增援
 * @author:zhugw
 * @time:2017年6月8日 下午7:28:51
 */
@FaultProcessEntity(type=6)
public class FaultProcessReinforce extends FaultProcessBase{

	@Override
	@Transactional
	public void executeProcess(FaultProcessPojo param) throws IOException {
		Assert.state(!param.getUserId().toString().equals(param.getReUserId()),"报障单不能增援负责人,请重新增援!");
		FaultInfoServiceImpl service = param.getFaultInfoImpl();
		FaultInfo faultInfo = service.dao.selectByPrimaryKey(param.getFaultInfoId());
		User user = service.userDao.selectByPrimaryKey(param.getUserId());
		Integer faultInfoId = param.getFaultInfoId();
		Integer userId = param.getUserId();
		String reUserId = param.getReUserId();
		if(param.getReUserId() == null || param.getReUserId()== ""){
			Assert.state(StringUtils.isNotBlank(param.getReUserPhone()),"paramsError_增援人员错误，请返回重试！");
			String[] phones = param.getReUserPhone().split(",");
			for(String phone : phones){					
				Assert.state(!user.getPhone().equals(phone),"报障单不能增援负责人,请重新增援!");
				User reUser = service.userDao.selectByPhone(phone);
				if(reUser != null){
					IndexUserFault userFault = service.indexUserFaultDao.selectByParams(reUser.getId(), param.getFaultInfoId(), null, 0);
					Assert.state(userFault == null,"该成员已加入维修，请重新转派!");
					ProjectUser pu = service.projectUserDao.selectByParames(faultInfo.getProjectId(), reUser.getId(), Constant.APP_TYPE.A, null, null);
					Assert.state(pu == null,"不能增援甲方人员,请从新选择。");
					//加入现场日志
					MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "增援"+reUser.getName(),
							Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
					service.maintainOperateLogDao.insertSelective(maintainInfo);
					//增加维修工人 0：普通人
					IndexUserFault indexUserFault = new IndexUserFault(reUser.getId(), faultInfoId,Constant.ZERO,new Date(),userId);
					service.indexUserFaultDao.insertSelective(indexUserFault);
					//通知增援人员
					service.messageService.sendMessage(reUser.getPhone(),userId,
							"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"增援您加入维修，快去看看吧~",
							Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
							faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
				}else{
					//加入现场日志
					MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "增援:"+phone,
							Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
					service.maintainOperateLogDao.insertSelective(maintainInfo);
					//发送消息提醒
					service.messageService.sendPhoneMessage(Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,Constant.PHONE_MESSAGE_INDEX.user_reported_fault_tpl_id_index,
							phone, user.getId(), faultInfo.getId(), false,null);
				}
			}
		}else{
			String[] reUserIds = reUserId.split(",");
			for(String refer_user_id : reUserIds){					
				User referUser = service.userDao.selectByPrimaryKey(Integer.valueOf(refer_user_id));
				Assert.state(referUser != null,"paramsError_增援人员错误，请返回重试！");
				//加入现场日志
				MaintainOperateLog maintainInfo = new MaintainOperateLog(faultInfoId,"", "增援"+referUser.getName(),
						Constant.PROJECT_USER_TYPE_INDEX.B_INDEX,Constant.MAINTAIN_STATE_INDEX.NO_DETAIL,faultInfo.getState(), new Date(),user.getName(),param.getUserId());
				service.maintainOperateLogDao.insertSelective(maintainInfo);
				//增加维修工人 0：普通人
				IndexUserFault indexUserFault = new IndexUserFault(referUser.getId(), faultInfoId,Constant.ZERO,new Date(),userId);
				service.indexUserFaultDao.insertSelective(indexUserFault);
				//通知增援人员
				service.messageService.sendMessage(referUser.getPhone(),userId,
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"增援您加入维修，快去看看吧~",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
			}
		}
		//通知请求增援人员 
		IndexUserFault uf = service.indexUserFaultDao.selectByParams(null,faultInfoId,2,0);
		if(uf != null){
			uf.setType(0);
			service.indexUserFaultDao.updateByPrimaryKeySelective(uf);
			User maintainUser = service.userDao.selectByPrimaryKey(uf.getUserId());
			if(maintainUser != null){
				service.messageService.sendMessage(maintainUser.getPhone(),userId,
						"【报障单 "+faultInfo.getCode()+"】"+user.getName()+"同意了您的增援申请，快去看看吧~",
						Constant.MESSAGE_TYPE_INDEX.PROJECT_INDEX,faultInfoId,Constant.MESSAGE_CLASSIFY_INDEX.MAINTAIN,
						faultInfo.getProjectId(), Constant.MESSAGE_STATE_INDEX.NO_HANDLE, null,0);
			}
		}
	}
	
}
