package com.mindup.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker (MessageBrokerRegistry registry){
        //Simple broker just for testing, it will change to an external broker soon.
        registry.enableSimpleBroker("/topic", "/queue");
        //prefixes setted for messages sended from service to controller.
        registry.setApplicationDestinationPrefixes("/chat");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Stomp endpoint registed.
        registry.addEndpoint("/ws").setAllowedOrigins("*")
                .withSockJS();//this can be deleted only if the app It's for mobile and only mobile
    }
    //defining the exit channel
    @Bean
    public MessageChannel clientOutboundChannel2(){
        return new ExecutorSubscribableChannel();
    }
    //using bean because autowired gives problems in Junit, and this help to inject the dependency in controller;
    @Bean
    public SimpMessagingTemplate simpMessagingTemplate(MessageChannel clientOutboundChannel){
        return new SimpMessagingTemplate(clientOutboundChannel);
    }
}
