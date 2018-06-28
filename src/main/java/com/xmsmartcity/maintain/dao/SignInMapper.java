package com.xmsmartcity.maintain.dao;

import com.xmsmartcity.maintain.pojo.SignIn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SignInMapper extends BaseDao<SignIn> {

    /**
     * 根据日期获取当日签到记录
     * @param checkinTime
     * @param userId
     * @return
     */
    List<SignIn> getSignInByDate(@Param("checkinTime") Date checkinTime,@Param("userId")Integer userId);
    
    /**
     * 根据月份获取当月签到日期
     * @param checkinTime
     * @return
     */
    List<Date> getSignDayByMonth(@Param("checkinTime") Date checkinTime,@Param("userId")Integer userId);

    /**
     * 签到汇总
     * @author:zhugw
     * @time:2017年9月7日 下午4:51:00
     * @param id
     * @param checkTime
     * @return
     */
    List<Map<String, Object>> getSignInForCollect(@Param("companyId")Integer companyId,@Param("checkinTime") Date checkTime);

    /**
     * 获取签到汇总日期
     * @time:2017年9月8日 上午9:45:47
     * @param date
     * @return
     */
	List<Date> getCollectDayByMonth(@Param("monthDate")Date date,@Param("companyId")Integer companyId);

	/*
	 *  web签到页面
	 */
	List<Map<String, Object>> selectListByDate(@Param("type")Integer type,@Param("userId") Integer userId, RowBounds rowBounds);
    
}