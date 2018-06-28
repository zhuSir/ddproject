package com.xmsmartcity.maintain.service.impl.fault_process;

import java.io.IOException;

import com.xmsmartcity.maintain.common.FaultProcessEntity;

/**
 * 报障单流程映射抽象类
 * @author:zhugw
 * @time:2017年6月8日 下午5:26:51
 */
public abstract class FaultProcessBase {
	
	public abstract void executeProcess(FaultProcessPojo param) throws IOException ;
	
	/**
	 * 报障流程操作
	 * 		1待接收	0:乙方负责人接收分派报障单 1:乙方负责人接收报障单  2:乙方负责人退回报障单 
	 * 		3待处理	3:乙方负责人转派报障单  4:乙方处理报障单  5:乙方负责人拒绝转派  6:乙方负责人增援  7:乙方负责人拒绝增援
	 *      2已退回	8:甲方同意乙方退回  9:甲方重新发起报障单  
	 *      4已接收	10:乙方请求转派  11:填写工单 12:无需工单
	 *      5待确认	13:工单不通过 14:工单通过 
	 *      6维修中	15:请求增援 16:维修说明 17:完成维修
	 *      7待验收	18:验收合格 19:验收不合格  
	 *      8待评价	20:工单评价
	 */
	private static Class<?>[] clazzs = new Class<?>[]{
		FaultProcessReceiveAssign.class,
		FaultProcessReceive.class,
		FaultProcessReturned.class,
		FaultProcessRedeploy.class,
		FaultProcessOperate.class,
		FaultProcessRefuseRedeploy.class,
		FaultProcessReinforce.class,
		FaultProcessRefuseReinforce.class,
		FaultProcessConsentReturn.class,
		FaultProcessReissue.class,
		FaultProcessRequestRedeploy.class,
		FaultProcessSaveWordOrder.class,
		FaultProcessNoWordOrder.class,
		FaultProcessNotPasseWordOrder.class,
		FaultProcessPasseWordOrder.class,
		FaultProcessRequestReinforce.class,
		FaultProcessSaveMaintainInfo.class,
		FaultProcessFinishMaintain.class,
		FaultProcessQualified.class,
		FaultProcessDisqualification.class,
		FaultProcessEvaluate.class
	};
	
	public static FaultProcessBase getFaultProcess(int type){
		FaultProcessBase faultPrecessObj = null;
		for(Class<?> clazz : clazzs){
			FaultProcessEntity entity = clazz.getAnnotation(FaultProcessEntity.class);
			int value = entity.type();
			if(type == value){
				try {
					faultPrecessObj = (FaultProcessBase)clazz.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		return faultPrecessObj;
	}
	
}
