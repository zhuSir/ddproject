package com.xmsmartcity.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//import com.zhishan.eastmaintain.constant.Constant;


@Component
public class NetUtil {
	
	private Logger logger = LoggerFactory.getLogger(NetUtil.class);
	
	@Autowired
	private RestTemplate template;
	
	
	public String getCounty(HttpServletRequest request){
		try{
			String ip = getIpAddr(request);
			String result = template.getForObject(Constant.IP_CONVERT_URL+ip,String.class);
			String[] arr = result.split("	");
			return arr[arr.length - 1];
		}catch (Exception e) {
			logger.error("error ip",e);
			return "";
		}
	}
	
	public String getIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}  
	
}
