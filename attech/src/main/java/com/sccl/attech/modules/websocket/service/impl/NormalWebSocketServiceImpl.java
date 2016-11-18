/**
 * @(#)NormalWebSocketServiceImpl.java     1.0 13:22:39
 * Copyright 2015 bjth, Inc. All rights reserved.
 * attech PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sccl.attech.modules.websocket.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import com.sccl.attech.modules.websocket.service.WebSocketService;

/**
 * The Class NormalWebSocketServiceImpl.
 * 普通消息
 * @author zzz
 * @version 1.0,13:22:39
 * @see com.sccl.attech.modules.websocket.service.impl
 * @since JDK1.7
 */
@Component
public class NormalWebSocketServiceImpl implements WebSocketService {

	/* (non-Javadoc)
	 * @see com.sccl.attech.modules.websocket.service.WebSocketService#getMessage(java.lang.String)
	 */
	@Override
	public WebSocketMessage<?> getMessage(String userId) {
		
//		return new TextMessage("普通消息");
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sccl.attech.modules.websocket.service.WebSocketService#replyMessage(java.lang.String, org.springframework.web.socket.WebSocketMessage)
	 */
	@Override
	public WebSocketMessage<?> replyMessage(String userId, WebSocketMessage<?> message) {
		Object messCont=message.getPayload();
		if(!(messCont instanceof String))
			return null;
		return new TextMessage("普通回复<"+message.getPayload()+">的消息");
	}

}
