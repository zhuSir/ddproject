package com.xmsmartcity.maintain.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.xmsmartcity.maintain.pojo.User;
import com.xmsmartcity.maintain.service.MessageService;
import com.xmsmartcity.maintain.service.UserService;

import net.sf.json.JSONArray;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;

/**
 * 定义websocket处理器
 * @author:zhugw
 * @time:2017年11月15日 下午2:20:06
 */
public class MsgSynchronizationHandler implements WebSocketHandler {

	Logger logger = Logger.getLogger(MsgSynchronizationHandler.class);

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	private final static Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>();
	private final static Map<String, String> tokens = new HashMap<String, String>();

	/**
	 * 链接成功后调用
	 * @author:zhugw
	 * @time:2017年11月15日 下午2:20:47
	 * @param session
	 * @throws Exception
	 * (non-Javadoc)
	 * @see org.springframework.web.socket.WebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.put(session.getId(), session);
		if(session != null && session.isOpen()){
			session.sendMessage(new TextMessage("connected!"));
		}
	}

	/**
	 * Send message to websocket
	 * 
	 * @author:zhugw
	 * @time:2017年11月15日 上午10:23:23
	 * @param message
	 * @param token
	 */
	public static void sendMessage(String message, String token) {
		String sessionId = tokens.get(token);
		if (sessionId != null) {
			WebSocketSession session = sessions.get(sessionId);
			if (session != null && session.isOpen()) {
				try {
					TextMessage returnMessage = new TextMessage(message);
					session.sendMessage(returnMessage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				tokens.remove(token);
			}
		}
	}

	/**
	 * 接收webSocket发送过来的消息并处理 接收第一次session发送过来的token，返回该用户未读消息数
	 * 
	 * @author:zhugw
	 * @time:2017年11月15日 上午10:19:06
	 * @param wss
	 * @param wsm
	 * @throws Exception
	 *             (non-Javadoc)
	 * @see org.springframework.web.socket.WebSocketHandler#handleMessage(org.springframework.web.socket.WebSocketSession,
	 *      org.springframework.web.socket.WebSocketMessage)
	 */
	@Override
	public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
		String message = (String) wsm.getPayload();
		String[] msgs = message.split(":");
		if (StringUtils.isNotEmpty(msgs[0]) && msgs[0].equalsIgnoreCase("token") && StringUtils.isNotEmpty(msgs[1])) {
			String sessionId = tokens.get(msgs[1]);
			if (sessionId != null) {
				tokens.remove(msgs[1]);
			}
			tokens.put(msgs[1], wss.getId());
			String returnMsg = unreadMsgSum(msgs[1]);// 获取消息未读数
			TextMessage returnMessage = new TextMessage(returnMsg);
			wss.sendMessage(returnMessage);
		}
	}

	/**
	 * 查询消息未读数,用户未登录则放回空字符串
	 * 
	 * @author:zhugw
	 * @time:2017年11月15日 上午10:22:01
	 * @param token
	 * @return
	 */
	public String unreadMsgSum(String token) {
		User user = userService.selectUserBySystemToken(token);
		if (user != null) {
			List<Map<String,Integer>> resultSum = messageService.selectUnreadMessageSum(user.getPhone());
			JSONArray result = JSONArray.fromObject(resultSum);
			return result.toString();
		}
		return "";

	}

	/**
	 * webSocket发生错误
	 * 
	 * @author:zhugw
	 * @time:2017年11月15日 上午10:22:43
	 * @param wss
	 * @param thrwbl
	 * @throws Exception
	 *             (non-Javadoc)
	 * @see org.springframework.web.socket.WebSocketHandler#handleTransportError(org.springframework.web.socket.WebSocketSession,
	 *      java.lang.Throwable)
	 */
	@Override
	public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
		WebSocketSession session = sessions.get(wss.getId());
		if (session != null && session.isOpen()) {
			sessions.remove(wss.getId());
		}
		if (wss.isOpen()) {
			wss.close();
		}
	}

	/**
	 * 链接关闭之前
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:16:05
	 * @param wss
	 * @param cs
	 * @throws Exception
	 * (non-Javadoc)
	 * @see org.springframework.web.socket.WebSocketHandler#afterConnectionClosed(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.CloseStatus)
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
		WebSocketSession session = sessions.get(wss.getId());
		if (session != null && session.isOpen()) {
			sessions.remove(wss.getId());
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
}