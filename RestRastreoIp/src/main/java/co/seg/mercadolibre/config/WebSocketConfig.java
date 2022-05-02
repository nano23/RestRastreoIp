package co.seg.mercadolibre.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * registro y configuracion de endpoint
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket").setAllowedOriginPatterns("*").withSockJS();
	}

	/**
	 * registro y configuracion de canal de mensajes
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app").enableSimpleBroker("/message");
	}

	/**
	 * configuraciones adicionales de los mensajes
	 */
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setMessageSizeLimit(500 * 1024 * 1024);
		registration.setSendBufferSizeLimit(1024 * 1024 * 1024);
		registration.setSendTimeLimit(200000);
	}
}