package com.xmsmartcity.extra.push;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.xmsmartcity.util.PropertiesHelper;

public class PushUtil {
	
	public static PropertiesHelper helper = new PropertiesHelper("system.properties");
	public static boolean ISUSER = helper.getBoolean("debug");

	/**
	 * 咚咚V2.0版本个推
	 */
	private final static String APP_ID = "v9Dji1hI889hnruPlt3x37";
	private final static String APP_KEY = "D4AorjdsFL7bqnoj1YF1w8";
	private final static String MASTER_SECRET = "WDUPOLmAqT8fgviVTRYE1";
	private final static String HOST = "http://sdk.open.api.igexin.com/apiex.htm";
	
	// 获取IGtPush
	public static IGtPush getIGtPush() {
		IGtPush PUBSH = null;
		if (PUBSH == null) {
			PUBSH = new IGtPush(HOST, APP_KEY,MASTER_SECRET);
			try {
				PUBSH.connect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return PUBSH;
	};

	/**
	 * 推送单条消息
	 * @author felix @date 2015-7-4 下午1:54:26
	 * @param id
	 * @param msg  O异地登陆  1邀请类消息
	 * @type  0甲方  1乙方
	 * @return
	 */
	public static boolean pushSingleMsg(String pushToken, String msg) {
		IGtPush push = getIGtPush();
		TransmissionTemplate template = transmissionTemplate(msg);
//		template.setAPNInfo(apnPayload);
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);
		// 判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。
		message.setPushNetWorkType(0);
		Target target = new Target();
		target.setAppId(APP_ID);
		target.setClientId(pushToken);
		// target.setAlias(id);
		IPushResult ret = push.pushMessageToSingle(message, target);
		System.out.println(ret.getResponse().toString());
		return "RESULT_OK".equals(ret.getResultCode().toString());
		
	}

	/**
	 * 批量推送
	 * 
	 * @author felix @date 2015-7-4 下午1:54:40
	 * @param ids
	 * @param msg
	 * @param type 0甲方1乙方
	 * @return
	 */
	public static boolean pushMsg(List<String> ids, String msg) {
		IGtPush push = getIGtPush();
		TransmissionTemplate template = transmissionTemplate(msg);
		ListMessage message = new ListMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);
		// 判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。
		message.setPushNetWorkType(0);
		List<Target> targets = new ArrayList<Target>();
		for (String id : ids) {
			Target target = new Target();
			target.setAppId(APP_ID);
			target.setClientId(id);
			// target.setAlias(userId);
			targets.add(target);
		}
		// 获取taskID
		String taskId = push.getContentId(message);
		IPushResult ret = push.pushMessageToList(taskId, targets);
		System.out.println("内容是："+ret.getResponse().toString());
		System.out.println("resultCode:"+ret.getResultCode());

		return "RESULT_OK".equals(ret.getResultCode().toString());
	}

	// 透传消息模板
	public static TransmissionTemplate transmissionTemplate(String msg) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(APP_ID);
		template.setAppkey(APP_KEY);
		
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(2);
		template.setTransmissionContent(msg);

		APNPayload payload = new APNPayload();
		payload.setBadge(1);
//		payload.setContentAvailable(1);
		payload.setSound("default");
		payload.setAlertMsg(new APNPayload.SimpleAlertMsg(JSONObject.parseObject(msg).getString("content")));
		payload.addCustomMsg("info", JSONObject.parseObject(msg));
		template.setAPNInfo(payload);

		return template;
	}

	// ==========================================================================================
	public static void main(String[] args) {
		JSONObject msg = new JSONObject();
//		Message m = new Message();
//		m.setContent("你没有看错,这个消息你看不到的");
//		m.setClassify(1);
//		m.setProjectId(1);
//		m.setType(1);
//		msg.put("message",m);
//		msg.put("type",0);
/*		List<String> ids = new ArrayList<String>();
		ids.add("c93fb6c1f273ab25681f486e48e3c107");
		PushUtil.pushMsg(ids,msg.toString(),0);
		ids = new ArrayList<String>();
		ids.add("81d66797c8735e2d4269c8aa62668eee");
		PushUtil.pushMsg(ids,msg.toString(),1);*/
/*		PushUtil.pushSingleMsg("15d4e29c881209efe1f63c33f70f6773", msg.toString(),0);*/
		msg.put("321", "你没有看错,这个消息你看不到的");//cf920eba7575b831be9a041de19f3945
		APNPayload apnPayload = new APNPayload();
		apnPayload.setBadge(1);
		PushUtil.pushSingleMsg("29556a18f968a0084d46f4aabddb8960", msg.toString());
	}

}
