package com.pavvels.price_charts.configs;

import com.pavvels.price_charts.clients.PolygonWebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Bean
    public ReactorNettyWebSocketClient reactorNettyWebSocketClient() {
        return new ReactorNettyWebSocketClient();
    }

    @Bean
    public HandlerMapping webSocketMapping(PolygonWebSocketClient polygonWebSocketClient) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/crypto-feed", polygonWebSocketClient);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(-1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
