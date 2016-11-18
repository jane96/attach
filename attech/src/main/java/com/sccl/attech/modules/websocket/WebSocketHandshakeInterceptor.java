package com.sccl.attech.modules.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.sccl.attech.common.utils.StringUtils;
import com.sccl.attech.modules.sys.entity.User;
import com.sccl.attech.modules.sys.utils.UserUtils;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler hander, Exception exception) {

	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler hander, Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			String userType = servletRequest.getServletRequest().getParameter("type");
			userType = StringUtils.isNotBlank(userType) ? "_" + userType : "";
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			if (session == null)
				return true;
			// 使用userName区分WebSocketHandler，以便定向发送消息
			User user = UserUtils.getUser();
			attributes.put(SystemWebSocketHandler.WEBSOCKET_USERID, user.getId() + userType);
		}
		return true;
	}

}
