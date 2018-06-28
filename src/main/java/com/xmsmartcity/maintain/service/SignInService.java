package com.xmsmartcity.maintain.service;

import com.xmsmartcity.maintain.pojo.SignIn;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenby on 2017/4/27.
 */
public interface SignInService extends BaseService<SignIn>{

    /**
     * 根据日期获取当日签到记录
     * @param date
     * @param userId
     * @return
     */
    public List<SignIn> getSignInByDate(Date date,Integer userId);
    
    /**
     * 根据月份获取当月签到日期
     * @param date
     * @return
     */
    public List<Date> getSignDayByMonth(Date date,Integer userId);

    /**
     * 签到汇总
     * @param checkTime
     * @param userId
     * @return
     */
	public  List<Map<String, Object>> getSignInForCollect(Date checkTime, Integer userId);

	/**
	 * 签到汇总天数
	 * @param checkTime
	 * @param userId
	 * @return
	 */
	public List<Date> getCollectDayByMonth(Date checkTime, Integer userId);

	/**
	 * web 签到页面
	 * @author:zhugw
	 * @time:2017年11月29日 下午2:04:12
	 * @param type
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Object selectListPageByDate(Integer type, Integer userId,Integer pageNo,Integer pageSize);
}
