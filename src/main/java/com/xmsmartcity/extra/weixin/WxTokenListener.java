package com.xmsmartcity.extra.weixin;

//import static com.zhishan.extra.weixin.WxHttp.href;
import static com.xmsmartcity.extra.weixin.WxHttp.href;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmsmartcity.extra.weixin.WxConstant.SYS;
import com.xmsmartcity.extra.weixin.WxConstant.URL;

//import com.zhishan.extra.weixin.WxConstant.SYS;
//import com.zhishan.extra.weixin.WxConstant.URL;

/**
 * Application Lifecycle Listener implementation class WxTokenListener
 *
 */
@WebListener
public class WxTokenListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(WxTokenListener.class);

    /**
     * Default constructor. 
     */
    public WxTokenListener() {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
//    	/**
//    	 * 每隔60分钟刷新一次 系统的参数
//    	 */
    	if(WxConstant.Debug){
    		return ;
    	}
		refreshWxToken();
		Timer timer = new Timer("update weixin timer",true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				refreshWxToken();
			}
		}, toNextHour(), 60*60*1000l);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }
    

	

	/**
	 * 刷新微信的token和jsticket
	 */
	private void refreshWxToken(){
		try{
			refreshSysAccessToken();
			refreshWxJsApiTicket();
			logger.info(" reload weixin sys param success");
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("get ticket exception then reload");
			refreshWxJsApiTicket();
			logger.info(" reload weixin sys param again sucess");
		}
	}
	
	/**
	 * 获得距离下一次整点过10秒的时间，返回豪秒数
	 * @return
	 */
	private Long toNextHour(){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 1);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 5);
		return now.getTimeInMillis() - System.currentTimeMillis();
	}
	

	/**
	 * 刷新系统accesstoken
	 * 
	 * @author felix  @date 2015-4-14 下午8:26:47
	 */
	private  void refreshSysAccessToken(){
		JSONObject jsonToken = WxHttp.href(URL.GEN_SYS_ACCESS_TOKEN,"GET",SYS.APPID,SYS.APP_SECRET);
		logger.info("access token is :" + jsonToken);
		WxConstant.SYS.ACCESS_TOKEN.changeSysToken(jsonToken);
	}
	
	/**
	 * 刷新JS的ticket
	 * 
	 * @author felix  @date 2015-4-14 下午8:27:00
	 * @return
	 */
	private void refreshWxJsApiTicket() {
//		System.setProperty("https.protocols", "SSLv3,SSLv2Hello");
		JSONObject jsonTicket = href(URL.GET_JS_API_TICKET, "GET",SYS.ACCESS_TOKEN.getAccessToken());
		logger.info("jsonTicket is :" + jsonTicket);
		WxConstant.SYS.JS_API_TICKET.change(jsonTicket);
	}
}
