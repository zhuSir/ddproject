package com.xmsmartcity.maintain.controller.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmsmartcity.maintain.service.SignInService;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/signIn")
public class WebSignInController {
	
	@Autowired
    private SignInService signInService;

	/**
	 * web 签到页面
	 * @time:2017年11月7日 下午2:56:08
	 * @param userId
	 * @param type   1:本月    2:本周
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/list/byDate")
    @ResponseBody
    public String getSignInByDate(Integer userId, Integer type,Integer pageNo,Integer pageSize){
        Object signIns = signInService.selectListPageByDate(type,userId,pageNo,pageSize);
        return Utils.toSuccessJsonResults(signIns);
    }
}
