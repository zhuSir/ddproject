package com.xmsmartcity.maintain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.xmsmartcity.maintain.pojo.Message;

@Repository
public interface MessageMapper extends BaseDao<Message> {
	
	public int selectMessageExist(@Param("phone") String phone, @Param("classify") Integer classify,
			@Param("userId") Integer userId, @Param("type") Integer type, @Param("referId") Integer referId,@Param("state") Integer state);
	
	public int selectMessageForInteger(@Param("phone") String phone, @Param("classify") Integer classify,
			@Param("userId") Integer userId, @Param("type") Integer type, @Param("referId") Integer referId);

	public int selectMessageForParames(@Param("content")String content,@Param("phone") String phone, @Param("classify") Integer classify,
			@Param("userId") Integer userId, @Param("type") Integer type, @Param("referId") Integer referId);

	public Message selectMessageByParames(Message msg);
	
	public List<Message> selectListMsgByParames(Message msg);

	/**
	 * 查询最新的非项目类消息
	 * 
	 * @param phone
	 * @param classify
	 * @return
	 */
	public Message getNoProjectMessage(@Param("phone") String phone, @Param("classify") Integer classify);

	/**
	 * 未读消息数量
	 * 
	 * @param phone
	 * @param classify
	 * @param type
	 */
	public Integer getNotReadSum(Message msg);

	/**
	 * 获取最新消息
	 * 
	 * @param phone
	 * @param classify
	 * @return
	 */
	public Message getNewestMessage(@Param("phone") String phone, @Param("classify") Integer classify);

	/**
	 * 查询对应类型消息
	 * 
	 * @author:zhugw
	 * @time:2017年4月9日 下午3:30:12
	 * @param phone
	 * @param classify
	 * @param startIndex
	 * @param pageSize
	 */
	public List<Message> selectMessageForClassify(@Param("phone") String phone, @Param("classify") Integer classify,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 获取message
	 * 
	 * @author:zhugw
	 * @time:2017年4月27日 下午4:27:15
	 * @param msg
	 * @return
	 */
	Message selectByParames(Message msg);

	/**
	 * 根据消息状态获取首页待处理事项
	 * 
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> selectMessageByState(@Param("state") Integer state, @Param("userId") Integer userId,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	List<Message> selectListByParames(
			@Param("phone") String phone, @Param("classify") Integer classify,
			@Param("userId") Integer userId, @Param("type") Integer type, 
			@Param("referId") Integer referId,@Param("isRead")Integer isRead,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	Integer selectUnReadMessageByPhone(String phone);

	List<Message> selectByPhone(String phone);

	public void updateIsReadForBatch(List<String> ids);

	public void updateIsDelForBatch(List<String> ids);

	/**
	 * 首页获取团队邀请，项目邀请消息
	 * @author:zhugw
	 * @time:2017年9月27日 下午4:34:19
	 * @param userId
	 * @param phone
	 * @return
	 */
	public List<Map<String, Object>> getInviteMessage(@Param("userId")Integer userId,@Param("userPhone") String phone);

	/*
	 *  web 页面分类消息未读消息
	 */
	public List<Map<String, Object>> selectListByUnread(@Param("userId") Integer userId,@Param("classify") Integer classify);
	/*
	 * web 页面分类消息已读消息
	 */
	public List<Map<String, Object>> selectListByRead(@Param("userId") Integer userId,@Param("classify") Integer classify, RowBounds rowBounds);

	/**
	 * 根据消息类别统计未读数
	 * @author:zhugw
	 * @time:2017年11月15日 上午10:12:54
	 * @param phone
	 * @return
	 */
	public List<Map<String, Integer>> selectUnreadMessageSum(@Param("phone")String phone);

	/**
	 * 根据参数查询消息
	 * @author:zhugw
	 * @time:2017年12月19日 下午2:34:02
	 * @param phone
	 * @param userId
	 * @param type
	 * @param referId
	 * @param referUserId
	 * @param isRead
	 * @param classify
	 * @param state
	 * @param projectId
	 */
	public Message selectMessageByParams(@Param("phone") String phone,@Param("type") Integer type,@Param("referId") Integer referId,@Param("referUserId") Integer referUserId,
			@Param("isRead")Integer isRead,@Param("classify") Integer classify,@Param("state") Integer state,@Param("projectId") Integer projectId);
}