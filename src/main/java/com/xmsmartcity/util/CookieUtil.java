package com.xmsmartcity.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * cookie相关
* @author linlijun
* @version 创建时间：2015-3-30 下午12:55:31
 */
public class CookieUtil {
	
	
	//添加到cookie
	public static void addCookie(HttpServletResponse response,String saveStr,String cookieName){
		Cookie cookie = new Cookie(cookieName, saveStr);
		// 设置有效日期  7天
		cookie.setMaxAge(7 * 24 * 60 * 60);  
		// 设置路径（默认）  
		cookie.setPath("/");   
		response.addCookie(cookie);
	}
	
	
	/**
	 * 根据cookie的key获取对应的cookie
	 * @param request
	 * @param cookieKey
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String cookieKey){
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(cookieKey)) {
			Cookie cookie = (Cookie) cookieMap.get(cookieKey);
			return cookie;
		}
		return new Cookie("n",null);
	}
	/**
	 * 根据多个keys获取对应的cookie
	 * @param request
	 * @param cookieKey
	 * @return
	 */
	public static List<Cookie> getCookieByNames(HttpServletRequest request,String...cookieKeys){
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		List<Cookie> list = new ArrayList<Cookie>();
		for (String key : cookieKeys) {
			if (cookieMap.containsKey(key)) {
				Cookie cookie = (Cookie) cookieMap.get(key);
				list.add(cookie);
			}
		}
		return list;
	}
	/**
	 * 清空cookie中，key包含likeName值的项
	 * @author linweiqin
	 * @createDate 2015-1-28 下午8:30:39
	 * @param request
	 * @param likeName
	 * @return
	 */
	public static void clearCookieLikeByName(HttpServletRequest request,HttpServletResponse response,String likeName){
		Cookie[] cookies = request.getCookies();
		List<String> keys = new ArrayList<String>();
		for (Cookie cookie : cookies) {
			if(cookie.getName().indexOf(likeName) > -1){
				keys.add(cookie.getName());
			}
		}
		removeCookies(response,keys.toArray());
	}
	/**
	 * 根据多个keys模糊查询获取对应的cookie
	 * @param request
	 * @param cookieKey
	 * @return
	 */
	public static List<Cookie> getCookieLikeByName(HttpServletRequest request,String likeName){
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		List<Cookie> list = new ArrayList<Cookie>();
		for (String key : cookieMap.keySet()) {
			if(key.indexOf(likeName) > -1){
				list.add(cookieMap.get(key));
			}
		}
		return list;
	}
	/**
	 * 可以添加一个或多个cookie
	 * @author linweiqin
	 * @createDate 2015-1-19 下午7:04:59
	 * @param cookies
	 */
	public static void addCookies(HttpServletResponse response,Cookie...cookies){
		for (Cookie cookie : cookies) {
			response.addCookie(cookie);
		}
	}
	/**
	 * 移除一个或多个cookie
	 * @author linweiqin
	 * @createDate 2015-1-19 下午7:08:34
	 * @param response
	 * @param cookieNames
	 */
	public static void removeCookies(HttpServletResponse response,Object...cookieNames){
		Cookie cookie = null;
		for (Object ckName : cookieNames) {
			cookie = new Cookie(ckName.toString(), null);
			cookie.setMaxAge(0); // 清除cookie
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}
	/**
	 * 
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * 
	 * @return
	 */
	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		//读取cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;

	}

}
