package com.hml.websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebSocketConfig {
	
	public static boolean ENABLE = false;

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
	
	@Value("${websocket.status}")
	public void setEnable(String status) {
		ENABLE = "Y".equals(status);
		log.info("WebSocket当前状态:{}",ENABLE);
	}
}
