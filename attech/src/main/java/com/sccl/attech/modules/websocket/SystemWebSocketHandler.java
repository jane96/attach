/**
 * @(#)SystemWebSocketHandler.java     1.0 10:40:11
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.websocket;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.sccl.attech.modules.websocket.service.WebSocketService;

/**
 * The Class SystemWebSocketHandler.
 * 
 * @author zzz
 * @version 1.0,10:40:11
 * @see com.sccl.attech.modules.websocket
 * @since JDK1.7
 */
public class SystemWebSocketHandler implements WebSocketHandler {

	/** The Constant logger. */
	private static final Logger							logger=LoggerFactory.getLogger(SystemWebSocketHandler.class);


	static final String									WEBSOCKET_USERID	= "userId";
	/** The web socket service. */
	private WebSocketService							webSocketService;

	public WebSocketService getWebSocketService() {
		return webSocketService;
	}

	public void setWebSocketService(WebSocketService webSocketService) {
		this.webSocketService = webSocketService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.WebSocketHandler#afterConnectionClosed(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.CloseStatus)
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
		logger.debug("socket 连接断开......");
		WebSocketUtil.users.remove(session.getHandshakeAttributes().get(WEBSOCKET_USERID));

		if (logger.isDebugEnabled()) {
			logger.debug("afterConnectionClosed(WebSocketSession=" + session.getId() + ", CloseStatus=" + arg1 + ") - 结束");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.WebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("socket 连接成功......");
		Map<String, Object> attrs = session.getHandshakeAttributes();
		WebSocketUtil.users.put((String) attrs.get(WEBSOCKET_USERID), session);
		String userId = (String) session.getHandshakeAttributes().get(WEBSOCKET_USERID);
		WebSocketMessage<?> returnMessage=webSocketService.getMessage((String) attrs.get(WEBSOCKET_USERID));
		if (null!=userId&&null!=returnMessage) {
			// 查询未读消息
			session.sendMessage(returnMessage);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("afterConnectionEstablished(WebSocketSession=" + session.getId() + ") - 结束");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.WebSocketHandler#handleMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.WebSocketMessage)
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("handleMessage(WebSocketSession=" + session.getId() + ", WebSocketMessage<?>=" + message + ") - 开始");
		}
		// 接收消息，处理并回复
		String userId = (String) session.getHandshakeAttributes().get(WEBSOCKET_USERID);
		WebSocketMessage<?> returnMessage=webSocketService.replyMessage(userId, message);
		if (null!=userId&&null!=returnMessage) {
			session.sendMessage(returnMessage);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("handleMessage(WebSocketSession=" + session.getId() + ", WebSocketMessage<?>=" + message + ") - 结束");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.WebSocketHandler#handleTransportError(org.springframework.web.socket.WebSocketSession, java.lang.Throwable)
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		if (logger.isDebugEnabled()) {
			logger.debug("handleTransportError(WebSocketSession=" + session.getId() + ", Throwable=" + exception.getMessage() + ") - 开始");
		}

		if (session.isOpen()) {
			try {
				session.close();
			} catch (IOException e) {
				logger.error("socket 出现错误",e);
			}
		}
		logger.debug("socket 出现错误,连接断开......");
		WebSocketUtil.users.remove(session.getHandshakeAttributes().get(WEBSOCKET_USERID));

		if (logger.isDebugEnabled()) {
			logger.debug("handleTransportError(WebSocketSession=" + session.getId() + ", Throwable=" + exception.getMessage() + ") - 结束");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.WebSocketHandler#supportsPartialMessages()
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
