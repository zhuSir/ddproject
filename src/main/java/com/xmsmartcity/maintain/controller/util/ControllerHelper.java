package com.xmsmartcity.maintain.controller.util;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;
import com.xmsmartcity.util.Constant;


public class ControllerHelper {
	
	 public static final JSONObject SUCCESS_RES =  new JSONObject();
	 static{
		 SUCCESS_RES.put(Constant.SUCCESS, Boolean.TRUE);
	 }
    public static final Logger logger = LoggerFactory.getLogger(ControllerHelper.class);

    /**
     * 发送json格式数据
     * @throws IOException
     */
    public static void sendJson(HttpServletResponse response, String data) throws IOException {
        response.setCharacterEncoding(Constant.UTF8);
        response.setContentType(Constant.TEXT_HTML);
        PrintWriter writer = response.getWriter();
        writer.write(data);
        writer.flush();
        writer.close();
    }

    /**
     * 只发送success标志位
     * @param response
     * @throws IOException
     */
    public static void sendSuccessJson(HttpServletResponse response) throws IOException {
        sendJson(response, SUCCESS_RES.toString()); 
    }
    
    
    /**
     * 在传入参数内，加入成功标志符
     * @param response
     * @throws IOException
     */
    public static void sendSuccessJson(HttpServletResponse response,JSONObject result) throws IOException {
        result.put(Constant.SUCCESS, Boolean.TRUE);
        sendJson(response, result.toString()); 
    }
    
    /**
     * 发送成功消息
     * @param response
     * @param msg
     * @throws IOException
     */
    public static void sendSuccessJson(HttpServletResponse response,String msg) throws IOException{
    	SUCCESS_RES.put(Constant.INFO, msg);
    	sendJson(response,SUCCESS_RES.toString());
    }

    /**
     * 发送json格式数据
     * 
     * @throws IOException
     */
    public static void sendErrorJson(HttpServletResponse response, String msg) throws IOException {
        sendErrorJson(response, msg, null);
    }

    /**
     * 发送json格式数据
     * 
     * @throws IOException
     */
    public static void sendErrorJson(HttpServletResponse response, String msg, Exception e)
            throws IOException {
        JSONObject result = new JSONObject();
        
        // 只有在 ApiHelper -> validateToken 方法中，验证其他设备登录的情况下，才会出现该情况
        // msg = "isOtherDeviceLogin_您的帐号已在其他设备上登录。"
        // msg = "isOtherDeviceLogin_您的帐号已被禁用，请联系客服人员。"
        String[] tempArr = null != msg ? msg.split("_") : null;
        String newMsg = null;
        if(null != tempArr && tempArr.length > 1){
        	newMsg = tempArr[1];
        	// 这里的tempArr[0] 是 isOtherDeviceLogin 在 ApiHelper -> validateToken 方法中
            result.put(tempArr[0], true);
        }else{
        	newMsg = msg;
        }
        if (e == null) {
            logger.error(newMsg);
        }
        else {
            logger.error(newMsg, e);
        }
        if(null != newMsg&&newMsg.length() > 40){
        	newMsg = "服务器异常，请稍后重试";
        }
        result.put("code", 1);
        result.put("data", "");
        result.put("success", false);
        result.put(Constant.INFO, newMsg);
        sendJson(response, result.toString());
    }


    /**
     * 跳转错误页面
     * 
     * @param model
     * @param msg
     * @return
     */
    public static String goErrorPage(Model model,String msg){
    	model.addAttribute("errorMsg", msg);
		return "/error/error_show";
    }
    
    /**
     * 获得url前缀全路径
     * 
     * @param request
     * @return
     */
    public static String getAllPath(HttpServletRequest request){
    	return request.getScheme()+"://"+request.getServerName() + request.getContextPath(); 
    }
    
    
    /**
     * 获得客户端ip
     * 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
    /**
     * 是否为ajax请求
     * 
     * @author felix  @date 2014-9-16 下午3:38:24
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request){
    	 String header = request.getHeader("X-Requested-With");  
    	 return "XMLHttpRequest".equals(header);
    }
    
   
}
