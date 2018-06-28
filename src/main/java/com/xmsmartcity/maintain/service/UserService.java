package com.xmsmartcity.maintain.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xmsmartcity.maintain.pojo.DealEvent;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.orm.Page;

public interface UserService  extends BaseService<User>{
	
	/**
	 * 获取用户信息
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:04:19
	 * @param id
	 * @return
	 */
	public User getUser(int id);
	
	/**
	 * 注册保存用户信息
	 * @param phone
	 * @param username
	 * @param password
	 * @return
	 */
	public User newUser(String phone,String username,String password);
	
	/**
	 * 验证手机号码是否注册过
	 * @param phone
	 * @return false未注册
	 */
	public boolean checkExist(String phone);
	
	/**
	 * 根据手机号查询用户
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:04:41
	 * @param phone
	 * @return
	 */
	public User getUserByPhone(String phone);

	/**
	 *  登录
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:04:53
	 * @param phone
	 * @param password
	 * @param pushToken
	 * @return
	 */
	public User login(String phone, String password, String pushToken,HttpServletRequest request);
	
	/**
	 * 根据验证码登录
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:05:04
	 * @param phone
	 * @param pushToken
	 * @return
	 */
	public User loginByCode(String phone,String pushToken,HttpServletRequest reqeust);
	
	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	public int modifyPassword(User user);

	/**
	 * 修改用户信息
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:05:16
	 * @param user
	 * @return
	 */
	public int modifyUserInfo(User user);

	/**
	 * 验证邮箱
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:05:24
	 * @param userId
	 * @param mail
	 */
	public void validateMail(Integer userId, String mail);
	
	/**
	 * 查询用户信息
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:05:51
	 * @param userId
	 * @return
	 * (non-Javadoc)
	 * @see com.xmsmartcity.maintain.service.BaseService#selectByPrimaryKey(java.lang.Integer)
	 */
	public User selectByPrimaryKey(Integer userId);

	/**
	 * 获取用户openid
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:06:02
	 * @param userIdList
	 * @return
	 */
	public List<String> getUserOpenIdsById(List<Integer> userIdList);
	
	/**
	 * 获取团队所属成员
	 * @param companyId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<User> selectUsersByCompanyId(Integer companyId,Integer startIndex,Integer pageSize);

	/**
	 * 获取用户状态
	 * @author:zhugw
	 * @time:2017年4月19日 上午11:41:29
	 * @param userId
	 * @return
	 */
	public DealEvent selectUserState(Integer userId);

	/**
	 * 根据openId获取用户
	 * @author:zhugw
	 * @time:2017年5月18日 上午9:46:20
	 * @param openId
	 * @return
	 */
	public User getUserByOpenId(String openId);
	
	/**
	 * 根据unionid获取用户信息
	 * @author:zhugw
	 * @time:2017年8月22日 下午3:46:33
	 * @param unionid
	 * @return
	 */
	public User getUserByUnionid(String unionid);

	/**
	 * 根据用户名和密码登录
	 * @author:zhugw
	 * @time:2017年5月18日 上午9:47:30
	 * @param trim
	 * @param parseMD5
	 * @return
	 */
	public User login(String trim, String parseMD5);

	/**
	 * 更新openId
	 * @author:zhugw
	 * @time:2017年5月18日 上午10:31:52
	 * @param user
	 */
	public void update(User user);

	/**
	 * 获取用户数量
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:08:00
	 * @return
	 */
	public Integer getUserCount();

	/**
	 * 获取其他用户信息
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:07:46
	 * @param otherUserId
	 * @return
	 */
	public Map<String,Object> getCountUserInfo(Integer otherUserId);
	
	/**
	 * 删除用户token
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:08:14
	 * @param userId
	 * @param sysToken
	 * @return
	 */
	public Integer removeUserTokenByParams(Integer userId,String sysToken);
	
	/**
	 * 查询公司用户（分页）
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:08:23
	 * @param user
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<User> queryForCompanyUserPage(User user,int pageNo,int pageSize);

	/**
	 * 手机号查询用户
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:08:50
	 * @param phone
	 * @return
	 */
	public User selectByPhone(String phone);
	
	/**
	 * 根据小程序openid获取用户
	 * @param xcxOpenId
	 * @return
	 */
	public User getUserByXcxOpenId(String xcxOpenId);
	
	/**
	 * 手机号和密码查询用户
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:09:01
	 * @param phone
	 * @param password
	 * @return
	 */
	public User getByPhoneAndPsw(String phone,String password);

	/**
	 * 获取用户动态
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:09:33
	 * @param userId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> getUserDynamic(Integer userId, Integer startIndex, Integer pageSize);

	/**
	 * 获取公司成员
	 * @time:2017年10月13日 下午2:25:33
	 * @param userId
	 * @param companyId
	 * @param searchPhone
	 * @return
	 */
	public List<User> selectCompanyMember(Integer userId, Integer companyId, String searchPhone);

	/**
	 * web 页面个人中心
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:09:44
	 * @param userId
	 * @return
	 */
	public Object selectUserInfo(Integer userId);

	/**
	 * 根据系统token查询user
	 * @author:zhugw
	 * @time:2017年11月15日 上午9:41:45
	 * @param token
	 * @return
	 */
	public User selectUserBySystemToken(String token);
}
