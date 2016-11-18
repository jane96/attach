/**
 * @(#)WebSocketService.java     1.0 11:33:34
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.websocket.service;

import org.springframework.web.socket.WebSocketMessage;

/**
 * The Interface WebSocketService.
 * websocket消息处理
 * @author zzz
 * @version 1.0,11:33:34
 * @see com.sccl.attech.modules.websocket.service
 * @since JDK1.7
 */
public interface WebSocketService {

	/**
	 * Gets the message.
	 * 建立连接时根据用户ID发送的消息
	 * @param userId the user id
	 * @return the message
	 */
	WebSocketMessage<?> getMessage(String userId);
	
	/**
	 * Reply message.
	 * 根据用户ID 回复消息
	 * @param userId the user id
	 * @param message the message
	 * @return the string
	 */
	WebSocketMessage<?> replyMessage(String userId,WebSocketMessage<?> message);

}
