package com.xmsmartcity.maintain.controller.mobile;

import com.xmsmartcity.maintain.pojo.SignIn;
import com.xmsmartcity.maintain.service.SignInService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.DateUtils;
import com.xmsmartcity.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenby on 2017/4/27.
 */
@Controller
@RequestMapping(value = "/api/signIn")
public class SignInApiController {
	
    @Autowired
    private SignInService signInService;

    /**
     * 创建签到
     * @param userId
     * @param token
     * @param signIn
     * @return
     */
    @RequestMapping(value = "/creat-signIn")
    @ResponseBody
    public String creatSignIn(Integer userId, String token, SignIn signIn) {
        Assert.state(signIn !=null, Constant.PARAMS_ERROR);
        Assert.state(signIn.getLat()!=null && signIn.getLng() !=null && signIn.getContent() !=null && signIn.getLocation()!=null & signIn.getUserId()!=null,Constant.PARAMS_ERROR);
        Date date = new Date();
        signIn.setCreatetime(date);
        signIn.setCheckinTime(date);
        signIn.setFaultInfoId(userId);
        signInService.insert(signIn);
        return Utils.toSuccessJson("创建成功！");
    }
    
    /**
     * 获取某天签到列表
     * @param userId
     * @param token
     * @param checkinTime
     * @return
     */
    @RequestMapping(value = "/select-signInByDate")
    @ResponseBody
    public String getSignInByDate(Integer userId, String token, String checkinTime){
        Assert.state(checkinTime !=null, Constant.PARAMS_ERROR);
        Date checkTime=DateUtils.StringToDate(checkinTime,DateUtils.formatStr_yyyyMMdd);
        List<SignIn> signIns=signInService.getSignInByDate(checkTime,userId);
        return Utils.toSuccessJson(signIns);
    }
    
    /**
     * 获取某月签到列表
     * @param userId
     * @param token
     * @param month
     * @return
     */
    @RequestMapping(value = "/select-signDayByMonth")
    @ResponseBody
    public String getSignDayByMonth(Integer userId, String token, String month){
        Assert.state(month !=null, Constant.PARAMS_ERROR);
        Date checkTime=DateUtils.StringToDate(month,DateUtils.formatStr_yyyyMM);
        List<Date> dates=signInService.getSignDayByMonth(checkTime,userId);
        return Utils.toSuccessJsonFormatDate(dates);
    }
    
    /**
     * 签到汇总
     * @author:zhugw
     * @time:2017年9月7日 下午4:54:22
     * @param userId
     * @param token
     * @param checkinTime
     * @return
     */
    @RequestMapping(value = "/selectCollectByDate", method=RequestMethod.POST)
    @ResponseBody
    public String getSignInForCollect(Integer userId, String checkinTime){
    	Assert.state(checkinTime !=null, Constant.PARAMS_ERROR);
        Date checkTime=DateUtils.StringToDate(checkinTime,DateUtils.formatStr_yyyyMMdd);
        List<Map<String, Object>> results = signInService.getSignInForCollect(checkTime,userId);
    	return Utils.toSuccessJson(results);
    }
    
    /**
     * 获取签到汇总日期
     * @time:2017年9月8日 上午9:55:54
     * @param userId
     * @param month
     * @return
     */
    @RequestMapping(value = "/selectCollectDayByMonth")
    @ResponseBody
    public String getSignInDayByMonth(Integer userId,String month){
        Assert.state(month !=null, Constant.PARAMS_ERROR);
        Date checkTime=DateUtils.StringToDate(month,DateUtils.formatStr_yyyyMM);
        List<Date> dates=signInService.getCollectDayByMonth(checkTime,userId);
        return Utils.toSuccessJsonFormatDate(dates);
    }
}
