package com.sccl.attech.modules.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketUtil {

	/** The Constant users <userId,websocketSession>. */
	public static final Map<String, WebSocketSession> users = new HashMap<String, WebSocketSession>();
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(WebSocketUtil.class);

	/**
	 * 给所有在线用户发送消息.
	 * 
	 * @param message
	 *            the message
	 */
	public static void sendMessageToUsers(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageToUsers(String=" + message + ") - 开始");
		}

		// 所有用户发送消息
		for (Entry<String, WebSocketSession> user : users.entrySet()) {
			try {
				if (null != user.getValue() && user.getValue().isOpen()) {
					user.getValue().sendMessage(new TextMessage(message));
				}
			} catch (IOException e) {
				logger.error("sendMessageToUsers(String=" + message + ") - 异常",
						e);

				e.printStackTrace();
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageToUsers(String=" + message + ") - 结束");
		}
	}

	/**
	 * 给某个用户发送消息.
	 * 
	 * @param userId
	 *            the user id
	 * @param message
	 *            the message
	 */
	public static void sendMessageToUser(String userId, String message) {
		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageToUser(String=" + userId + ", String="
					+ message + ") - 开始");
		}

		try {
			// 给指定用户发送消息
			WebSocketSession session = users.get(userId);
			if (null != session && session.isOpen()) {
				session.sendMessage(new TextMessage(message));
			}
		} catch (Exception e) {
			logger.error("sendMessageToUser(String=" + userId
					+ ", TextMessage=" + message + ") - 异常", e);

			e.printStackTrace();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("sendMessageToUser(String=" + userId + ", String="
					+ message + ") - 结束");
		}
	}
}
