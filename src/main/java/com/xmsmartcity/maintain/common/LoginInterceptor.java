package com.xmsmartcity.maintain.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xmsmartcity.maintain.controller.util.ApiHelper;
import com.xmsmartcity.maintain.controller.util.ControllerHelper;
import com.xmsmartcity.maintain.service.RedisCacheService;
import com.xmsmartcity.util.Utils;

import net.sf.json.JSONObject;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private ApiHelper apiHelper;
	
	private String[] whiteListPaths;
	
	private String[] whiteListHost;
	
	@Autowired
	private RedisCacheService redisCache;
	
	private Logger logger = Logger.getLogger(LoginInterceptor.class);
	
	
    public static byte[] getRequestPostBytes(HttpServletRequest request)  
            throws IOException {  
        int contentLength = request.getContentLength();  
        /*当无请求参数时，request.getContentLength()返回-1 */  
        if(contentLength<0){  
            return null;  
        }  
        byte buffer[] = new byte[contentLength];  
        for (int i = 0; i < contentLength;) {
            int readlen = request.getInputStream().read(buffer, i,  
                    contentLength - i);  
            if (readlen == -1) {  
                break;  
            }  
            i += readlen;  
        }  
        return buffer;  
    }  
  
    public static String getRequestPostStr(HttpServletRequest request)  
            throws IOException {  
        byte buffer[] = getRequestPostBytes(request);  
        String charEncoding = request.getCharacterEncoding();  
        if (charEncoding == null) {  
            charEncoding = "UTF-8";  
        }  
        return new String(buffer, charEncoding);  
    }
	
	
	/**
	 * 验证是否token是否失效
	 * @author:zhugw
	 * @time:2017年5月25日 上午9:16:31
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws Exception
	 * (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try{
			String host = CommonUtils.getRemoteHost(request);
			String uri = request.getRequestURI();
			logger.info("request host: "+host);
			logger.info("request path: "+uri);
//			String agentStr = request.getHeader("user-agent");
//			boolean istrue = agentStr.contains("Mozilla/5.0");
//			boolean isExist = agentStr.contains("iPhone");
//			boolean is_exist = uri.contains("/user/code");
//			if(!isExist && istrue && is_exist){
//				logger.info("request head: "+agentStr);
//				return false;
//			}
			//默认一个ip一个接口一秒内只能请求一次
			String cacheKey = host+uri;
			Object cacheObj = redisCache.getObject(cacheKey);
			if(cacheObj == null){
				redisCache.putObject(cacheKey, cacheKey,1);				
			}else{
				JSONObject result = new JSONObject();
				result.put("code", 1);
		        result.put("data", new Object());
		        result.put("info", "");
				ControllerHelper.sendJson(response,result.toString());
				logger.info(" 一秒内超过访问次数！");
				return false;
			}
			List<String> whiteHost = Arrays.asList(whiteListHost);
			List<String> whitePath = Arrays.asList(whiteListPaths);
			if(whiteHost.contains(host) || whitePath.contains(uri)){
				return true;
			}
			for(String path : whitePath){
				if(uri.indexOf(path) != -1){
					return true;
				}
			}
			
			String token = request.getParameter("token");
			//验证token
			if(!StringUtils.isNotBlank(token)){
				String contentType = request.getContentType();
				if(contentType != null && contentType.contains("application/json")){
					String requestContent = getRequestPostStr(request);
					JSONObject jsonObject = JSONObject.fromObject(requestContent);
					token = (String) jsonObject.get("token");
				}
			}
			logger.debug("request param token: "+token);
			apiHelper.validateToken(token);
			return true;
		}catch(Exception e){
			String data = Utils.toFailJson(e.getMessage(),e);
			ControllerHelper.sendJson(response, data);
			return false;
		}
	}
	
	

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	public String[] getWhiteListPaths() {
		return whiteListPaths;
	}

	public void setWhiteListPaths(String[] whiteListPaths) {
		this.whiteListPaths = whiteListPaths;
	}

	public String[] getWhiteListHost() {
		return whiteListHost;
	}

	public void setWhiteListHost(String[] whiteListHost) {
		this.whiteListHost = whiteListHost;
	}
}
//		   ＿ ＿＿
//		／　　　▲
//	／￣　 ヽ　   ■■
//	●　　　    　 ■■
//	ヽ＿＿＿　    ■■
//		       ）＝｜
//		    ／　｜｜
//		∩∩＿＿とﾉ
//		しし———┘