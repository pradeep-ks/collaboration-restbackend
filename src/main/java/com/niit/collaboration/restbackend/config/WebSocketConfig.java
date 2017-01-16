package com.niit.collaboration.restbackend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = "com.niit")
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/**
	 * This method configures the message broker.
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic", "/queue");
		registry.setApplicationDestinationPrefixes("/app");
	}

	/**
	 * This method configures the Endpont.
	 */
	public void registerStompEndpoints(StompEndpointRegistry arg0) {
		arg0.addEndpoint("/chat").withSockJS();			// for private chat
		arg0.addEndpoint("/chatForum").withSockJS();	// for group chat
	}

}
