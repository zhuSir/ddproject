package com.xmsmartcity.maintain.controller.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.maintain.controller.util.UploadHelper;
import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.UserService;
import com.xmsmartcity.util.Constant;
import com.xmsmartcity.util.Utils;

@Controller
@RequestMapping(value = "/api/user")
public class WebUserController {

	@Autowired
	private UserService service;
	
	@Autowired
	private UploadHelper uploadHelper;
	
	/**
	 * web 页面个人中心 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/info")
	@ResponseBody
	public String selectUserInfo(Integer userId){
		Assert.state(userId != null && userId > 0,Constant.PARAMS_ERROR+"：userID错误,为null或为0");
		Object obj = service.selectUserInfo(userId);
		return Utils.toSuccessJsonResults(obj);
	}
	
	/**
	 * web 页面个人中心 
	 * @param userId
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public String updateUserInfo(User user,@RequestParam(required=false)MultipartFile file,
			HttpServletResponse response,HttpServletRequest request) throws IOException{
		//String phone_regex = "^1(3[0-9]|4[57]|5[0-35-9]|7[0-9]|8[0-9]|9[0-9])\\d{8}$";
		//String phone = user.getPhone();
		//Assert.state(StringUtils.isNotEmpty(phone),Constant.PARAMS_ERROR+" 电话号码错误." );
		//Assert.state(phone.matches(phone_regex),"请填写正确的电话号码！");
		int sizeLimit = 30 * 1024 * 1024;
		if(file != null){
			JSONObject result = uploadHelper.saveSingleFile(request,file,1,true, sizeLimit);
			if (result.getBooleanValue("success")) {
				user.setHeadPic(result.get("name").toString());
			}else{
				user.setHeadPic(null);
			}
		}else{
			user.setHeadPic(null);
		}
		int res = service.modifyUserInfo(user);
		Assert.state(res != 0,Constant.SERVICE_ERROR);
		return Utils.toSuccessJson("修改成功");
	}
}
