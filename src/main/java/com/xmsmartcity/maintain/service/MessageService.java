package com.xmsmartcity.maintain.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.xmsmartcity.maintain.pojo.Message;

import net.sf.json.JSONObject;

public interface MessageService extends BaseService<Message>{

	/**
	 * 获取未处理的消息
	 * @param phone 
	 * @param userId refer_user_id
	 * @param classify
	 * @param type
	 * @return
	 */
	public Integer getPendingMessageForInteger(String content,String phone,Integer userId,Integer classify,Integer type,Integer referId);
	
	/**
	 * 判断是否有该
	 * @author:zhugw
	 * @time:2017年5月26日 上午11:15:06
	 * @param phone
	 * @param userId
	 * @param classify
	 * @param type
	 * @param referId
	 * @param state
	 * @return
	 */
	public Integer selectMessageExist(String phone, Integer userId, Integer classify, Integer type,Integer referId,Integer state);
	
	/**
	 * 发送消息新增消息
	 * @param phone
	 *            发送给谁的手机号
	 * @param userId
	 *            谁发送的 用户id (邀请人/申请者的id)
	 * @param content
	 *            发送内容
	 * @param type
	 *            消息类型，0系统消息 1邀请甲方负责人 2邀请乙方负责人 3邀请普通成员进团队 4甲方邀请普通成员进项目
	 *            5乙方邀请普通成员进项目 6邀请成为团队负责人 7邀请成为项目负责人 8申请加入企业 9退出项目 10申请加入项目 11项目相关 12其他消息 
	 * @param referId
	 *            相关id 1项目id， 2服务类型id 3团队id 4项目id 5服务类型id 6团队id 7团队id 8报障id 9项目id 10服务类型id
	 * @param classify
	 *            归类 0企业类 1项目类 2故障类
	 * @param projectId
	 *            项目ID
	 * @param state
	 *            状态 0未处理 1已同意 2已拒绝 3不用处理（通知类）4已失效
	 * @param userName
	 *            用户名称，用于推送内容的首部
	 * @param jumpType 
	 *            跳转类型 app专用，类型为项目类时0跳转报障详情 1不跳转
	 */
	public void sendMessage(String phone,Integer userId,String content,Integer type,Integer referId,Integer classify,Integer projectId,Integer state, String userName,Integer jumpType);
	
	/**
	 * 发送短信(不保存信息)
	 * 
	 * @param type
	 *            发送模板 0 客服邀请团队负责人 1 用户邀请团队成员 2 用户邀请乙方负责人 3 客服邀请乙方负责人 4
	 *            客服邀请项目负责人 5 用户邀请项目成员 6 用户邀请项目负责人
	 * @param phone
	 *            发送给谁
	 * @param userId
	 *            用户id
	 * @param referId
	 *            (type 为某一值放回的数据类型) 0,1 companyId 2,3 serviceTypeId 4,5,6项目id
	 */
	public void sendPhoneMsg(Integer type, String phone, Integer userId, Integer referId) throws IOException;
	
	/**
	 * 发送短信(保存信息)
	 * 
	 * @author:zhugw
	 * @time:2017年4月10日 上午9:42:45
	 * @param classify 消息类型
	 * @param type
	 *            消息类型， 1邀请甲方负责人 2邀请乙方负责人 3邀请普通成员进团队 4甲方邀请普通成员进项目 5乙方邀请普通成员进项目
	 *            6邀请成为团队负责人 7邀请成为项目负责人 8项目报障相关
	 * @param phone
	 *            发送给谁
	 * @param userId
	 *            用户id
	 * @param referId
	 *            相关id 1项目id， 2服务类型id 3团队id 4项目id 5服务类型id 6团队id 7项目id 8项目id
	 *            9报障id
	 * @param isSendSystemMessage
	 *            是否发送系统消息 true 发送
	 * @throws IOException
	 */
	public void sendPhoneMessage(Integer classify,Integer type,String phone,Integer userId,Integer referId,Boolean isSendSystemMessage,String webPath) throws IOException;

	/**
	 * 推送消息(保存信息)不发送短信
	 * @param classify
	 * @param type
	 * @param phone
	 * @param userId
	 * @param referId
	 * @param isSendSystemMessage
	 * @param webPath
	 * @throws IOException
	 */
	public void pushMessage(JSONObject jsonObject) throws IOException;
	
	/**
	 * 将邀请加入团队的消息置为无效
	 * @param companyId
	 */
	public void setReferMsgUseless(Integer referId,Integer type);
	
	/**
	 * 将邀请加入项目的消息置为无效
	 * @param companyId
	 */
	public void setReferProjectMsgUseless(String phone,Integer id, Integer type,Integer serviceTypeId);

	/**
	 * 获取消息首页（企业类、邀请类）
	 * @param phone
	 * @param zero
	 * @return
	 */
	public Object getNotProjectMessage(String phone, Integer zero);

	/**
	 * 消息首页(最新数据及未读数量)
	 * @param userId
	 * @return
	 */
	public String getIndexMsg(Integer userId);

	/**
	 * 
	 * 根据类型获取对应消息
	 * @author:zhugw
	 * @time:2017年4月9日 下午3:16:27
	 * @param userId
	 * @param classify
	 * @param startIndex
	 * @param pageSize	
	 */
	public List<Message> getMessageByClassify(Integer userId, Integer classify, Integer startIndex, Integer pageSize);

	/**
	 * 保存信息
	 * @author:zhugw
	 * @time:2017年4月11日 上午11:53:26
	 * @param phone
	 * @param userId
	 * @param content
	 * @param type
	 * @param referId
	 * @param classify
	 * @param projectId
	 * @param state
	 * @param userName
	 * @param jumpType
	 */
	public void saveMessage(String phone, Integer userId, String content, Integer type, Integer referId,
			Integer classify, Integer projectId, Integer state, String userName, Integer jumpType);

	/**
	 * 消息操作
	 * @author:zhugw
	 * @time:2017年4月14日 上午9:52:50
	 * @param userId
	 * @param type
	 * @param messageId
	 */
	public void messageOperate(Integer userId, Integer type, Integer messageId);

	/**
	 * 获取message
	 * @author:zhugw
	 * @time:2017年4月27日 下午4:20:55
	 * @param phone
	 * @param referUserId
	 * @param classify
	 * @param projectId
	 * @param referId
	 * @param type
	 * @param state
	 * @return
	 */
	public Message getMessage(String phone,Integer referUserId,Integer classify,Integer projectId,Integer referId,Integer type,Integer state);
	
	/**
	 * 根据消息状态获取首页待处理事项
	 * @param state
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> getMessageByState(Integer state,Integer userId,Integer startIndex,Integer pageSize);

	/**
	 * 设置消息已读
	 * @author:zhugw
	 * @time:2017年8月17日 下午2:34:08
	 * @param messageIds
	 * @param userId
	 */
	public void setMessageIsRead(String messageIds, Integer userId);

	/**
	 * 设置消息为已删除
	 * @author:zhugw
	 * @time:2017年8月17日 下午2:34:41
	 * @param messageId
	 * @param userId
	 */
	public void delMessageById(String messageIds, Integer userId);

	/**
	 * 首页获取团队邀请，项目邀请消息
	 * @author:zhugw
	 * @time:2017年9月27日 下午4:32:36
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getInviteMessage(Integer userId);

	/**
	 * web 页面分类消息
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:02:19
	 * @param userId
	 * @param classify
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Object selectMessageByClassify(Integer userId, Integer classify,Integer pageNo, Integer pageSize);
	
	/**
	 * 统计类别未读消息数
	 * @author:zhugw
	 * @time:2017年11月15日 上午10:07:45
	 * @param phone
	 * @return
	 */
	public List<Map<String,Integer>> selectUnreadMessageSum(String phone);

	/**
	 * 根据参数删除消息
	 * @author:zhugw
	 * @time:2017年12月19日 下午2:47:33
	 * @param phone
	 * @param referUserId
	 * @param type
	 * @param referId
	 * @param isRead
	 * @param classify
	 * @param state
	 * @param projectId
	 */
	public void delMessageByParams(String phone, Integer referUserId,Integer type,Integer referId,Integer isRead,
			Integer classify,Integer state,Integer projectId);

}
