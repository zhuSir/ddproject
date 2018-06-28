package com.xmsmartcity.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class Utils {

	private static Logger logger = Logger.getLogger(Utils.class);
	
	/**
	 * 设置servlet上下文
	 * @param key
	 * @param value
	 */
	public static void setServletAttribute(String key,Object value){
		if(!StringUtils.isNotEmpty(key) || value == null){
			logger.error("set servlet attribute key or value is null ");
			return ;
		}
		getServletContext().setAttribute(key, value);
	}
	
	/**
	 * 获取servlet上下文中数据
	 * @param key
	 * @return
	 */
	public static Object getServletAttribute(String key){
		if(!StringUtils.isNotEmpty(key)){
			logger.error("get servlet attribute key is null ");
			return null;
		}
		return getServletContext().getAttribute(key);
	}
	
	/**
	 * 删除servlet上下文中数据
	 * @param key
	 */
	public static void removeServletAttribute(String key){
		if(!StringUtils.isNotEmpty(key)){
			logger.error("remove servlet attribute key is null ");
			return ;
		}
		getServletContext().removeAttribute(key);
	}
	
	/**
	 * 获取sevlet上下文对象
	 * @return
	 */
	private static ServletContext getServletContext(){
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();    
        ServletContext servletContext = webApplicationContext.getServletContext();
        return servletContext;
	}
	
	/**
     * 发送json格式数据
     * 
     * @throws IOException
     */
    public static void sendJson(HttpServletResponse response, String data){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter writer;
		try {
			writer = response.getWriter();
			if(!StringUtils.isNotEmpty(data)){
				data = " results is null or empty";
			}
			writer.write(data);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 转换成json格式
	 * @param str
	 * @return
	 */
	public static String toJson(List<?> str){
		if(str != null){
			JSONArray arr = JSONArray.fromObject(str);
			String resStr = arr.toString();
			return resStr;
		}else{
			JSONObject obj = new JSONObject();
			obj.put("success", false);
			return obj.toString();
		}
	}
	
	/**
	 * 转换成功json
	 * @param str
	 * @return
	 */
	public static String toSuccessJson(Object str){
		JSONObject obj = new JSONObject();
		if(str != null){			
			obj.put("code", 0);
			JSONArray arr = JSONArray.fromObject(str);
			String resStr = arr.toString();
			obj.put("data", resStr);
			obj.put("info", "");
		}else{
			obj.put("code", 0);
			obj.put("info", "");
			obj.put("data", new Object());
		}
		return obj.toString();
	}
	
	/**
	 * 转换成功json(处理Date类型的数据)
	 * @param str
	 * @return
	 */
	public static String toSuccessJsonFormatDate(Object str){
		JSONObject obj = new JSONObject();
		if(str != null){			
			obj.put("code", 0);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			JSONArray arr = JSONArray.fromObject(str,jsonConfig);
			String resStr = arr.toString();
			obj.put("info", "");
			obj.put("data", resStr);
		}else{
			obj.put("code", 0);
			obj.put("info", "");
			obj.put("data", new Object());
		}
		return obj.toString();
	}
	
	/**
	 * 转换成功json
	 * @param str
	 * @return
	 */
//	public static String toSuccessJson(String str,Object result){
//		JSONObject obj = new JSONObject();
//		if(result != null){			
//			obj.put("success", true);
//			JSONArray arr = JSONArray.fromObject(result);
//			String resStr = arr.toString();
//			obj.put(str, resStr);
//		}else{
//			obj.put("success", false);
//			obj.put("info", "results is null");
//		}
//		return obj.toString();
//	}
	
	/**
	 * 转换成功json
	 * @param str
	 * @return
	 */
	public static String toSuccessJsonResults(Object str){
		JSONObject obj = new JSONObject();
		if(str != null){
			obj.put("code", 0);
			JSONObject arr = JSONObject.fromObject(str);
			String resStr = arr.toString();
			obj.put("data", resStr);
			obj.put("info", "");
		}else{
			obj.put("code",0);
			obj.put("info", "");
			obj.put("data", new Object());
		}
		return obj.toString();
	}
	
	/**
	 * 转换成功json
	 * @param str
	 * @return
	 */
//	public static String toSuccessJsonResults(String str,Object res){
//		JSONObject obj = new JSONObject();
//		if(res != null){
//			obj.put("success", true);
//			JSONObject arr = JSONObject.fromObject(res);
//			String resStr = arr.toString();
//			obj.put(str, resStr);
//		}else{
//			obj.put("success", false);
//			obj.put("info", "results is null");
//		}
//		return obj.toString();
//	}
	
	/**
	 * 转换成功json 
	 * @param str
	 * @return
	 */
	public static String toSuccessJson(String str){
		JSONObject obj = new JSONObject();
		if(str == null){
			obj.put("code",0);
			obj.put("info", "");
			obj.put("data", new Object());
		}else{
			obj.put("code",0);
			obj.put("info",str);
			obj.put("data", new Object());
		}
		return obj.toString();
	}
	
	/**
	 * 转换成功json 
	 * @param str
	 * @return
	 */
	public static String toSuccessJsonRes(String str){
		JSONObject obj = new JSONObject();
		if(str == null){
			obj.put("code",0);
			obj.put("info", "");
			obj.put("data", new Object());
		}else{
			obj.put("code",0);
			obj.put("info","");
			obj.put("data",str);
		}
		return obj.toString();
	}
	
	/**
	 * 转换成失败json
	 * @param msg
	 * @return
	 */
	public static String toFailJson(String msg,Exception e){
		if(!StringUtils.isNotEmpty(msg)){
			msg = Constant.SERVICE_ERROR;
		}
		JSONObject obj = new JSONObject();
		if(e != null){
        	logger.error(msg,e);
        	if(!(e instanceof IllegalStateException)&&
        			!(e instanceof IllegalArgumentException)){
        		msg = "serviceError_服务器异常，请稍后重试";
        	}
        }else{
        	logger.error(msg);
        }
		// msg = "isOtherDeviceLogin_您的帐号已在其他设备上登录。"
		// msg = "isOtherDeviceLogin_您的帐号已被禁用，请联系客服人员。"
		String[] tempArr = null != msg ? msg.split("_") : null;
		String newMsg = null;
		if(null != tempArr && tempArr.length > 1){
			// 这里的tempArr[0] 是 isOtherDeviceLogin 在 ApiHelper -> validateToken 方法中
			if("isOtherDeviceLogin".equals(tempArr[0])){
				//您的帐号已在其他设备上登录
				obj.put("info", tempArr[1]);
				obj.put("code", -1);
			}else if("paramsError".equals(tempArr[0])){
				//参数错误，请联系开发人员
				obj.put("info", tempArr[1]);
				obj.put("code", 101);
			}else if("serviceError".equals(tempArr[0])){
				//服务器异常，请稍后重试
				obj.put("info", tempArr[1]);
				obj.put("code", 102);
			}else if("accountDeprecated".equals(tempArr[0])){
				//您的账号已被禁用，请联系客服人员。
				obj.put("info", tempArr[1]);
				obj.put("code", 103);
			}
		}else{
			newMsg = msg;
			obj.put("code", 1);
			obj.put("info", newMsg);
		}
		obj.put("data", new Object());
		return obj.toString();
	}
	
	
	public static void main(String[] args) {
		String tt = toFailJson("ssss",new RuntimeException("321"));
		System.out.println(tt);
	}
	
	/**
	 * 转换成失败json
	 * token过期提示
	 * @param msg
	 * @return
	 * status：0 token失效 
	 */
	public static String toFailJson(String msg,int status,Exception e){
		if(!StringUtils.isNotEmpty(msg)){
			msg = "results is empty";
		}
		JSONObject obj = new JSONObject();
		obj.put("success", false);
		obj.put("status", status);
		obj.put("info", msg);
		return obj.toString();
	}
	
	/**
	 * 转换成json格式
	 * @param str
	 * @return
	 */
	public static String toJson(Map<?,?> str){
		if(str != null){
			JSONObject arr = JSONObject.fromObject(str);
			String resStr = arr.toString();
			return resStr;
		}else{
			JSONObject obj = new JSONObject();
			obj.put("success", false);
			obj.put("info", "results is null");
			return obj.toString();
		}
	}
	
	/**
	 * 转换成json格式
	 * @param str
	 * @return
	 */
	public static String toJson(Object str){
		if(str != null){
			JSONObject arr = JSONObject.fromObject(str);
			String resStr = arr.toString();
			return resStr;
		}else{
			JSONObject obj = new JSONObject();
			obj.put("success", false);
			obj.put("info", "results is null");
			return obj.toString();
		}
	}
	
	/**
	 * 默认编码
	 */
	private static String ENCODING = "UTF-8";
	
	/**
	 * 发生post请求
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static String post(String url, Map<String, String> paramsMap) {
	    CloseableHttpClient client = HttpClients.createDefault();
	    String responseText = "";
	    CloseableHttpResponse response = null;
	    try {
	        HttpPost method = new HttpPost(url);
	        if (paramsMap != null) {
	            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
	            for (Map.Entry<String, String> param : paramsMap.entrySet()) {
	                NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
	                paramList.add(pair);
	            }
	            method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
	        }
	        response = client.execute(method);
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            responseText = EntityUtils.toString(entity);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            response.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return responseText;
	}
	/**
	 * 获取客户的ip
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}

	/**
	 * 获取验证码
	 * @param num
	 * @return
	 */
	public static String getRandom(int num) {
		String str = "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
		String str2[] = str.split(",");//将字符串以,分割
		Random rand = new Random();//创建Random类的对象rand
		int index = 0;
		String randStr = "";//创建内容为空字符串对象randStr
		for (int i=0; i<num; ++i){  
			index = rand.nextInt(str2.length-1);//在0到str2.length-1生成一个伪随机数赋值给index  
		    randStr += str2[index];//将对应索引的数组与randStr的变量值相连接  
		}  
		System.out.println("验证码：" + randStr);  
		return randStr;
	}
	
	/**
	 * 云片网发送短信，自动匹配模板
	 * @param apikey
	 * @param text
	 * @param mobile
	 * @return
	 */
	public static String singleSend(String apikey, String text, String mobile) {
	    Map<String, String> params = new HashMap<String, String>();//请求参数集合
	    params.put("apikey", apikey);
	    params.put("text", text);
	    params.put("mobile", mobile);
	    return post("https://sms.yunpian.com/v2/sms/single_send.json", params);//请自行使用post方式请求,可使用Apache HttpClient
	}
	
	private static String URI_TPL_SEND_SMS = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";
	
	/**
	 * 云片网发送短信，指定匹配模板
	 * @param mobile
	 * @param tplId
	 * @param tplValue
	 * @return
	 */
	public String tplSingleSend(String mobile, String tplId, String tplValue) {
		Map<String, String> parms = new HashMap<String, String>();
		parms.put("mobile", mobile);
		parms.put("tpl_id", tplId);
		parms.put("tpl_value", tplValue);
		return post(URI_TPL_SEND_SMS, parms);
	}
	
}
