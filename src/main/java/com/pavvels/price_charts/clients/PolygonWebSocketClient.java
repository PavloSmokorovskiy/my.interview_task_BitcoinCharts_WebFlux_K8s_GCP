package com.pavvels.price_charts.clients;

import com.pavvels.price_charts.services.PolygonApiKeysService;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.socket.WebSocketMessage;

import java.net.URI;

@NonNullApi
@Component
@Slf4j
@RequiredArgsConstructor
public class PolygonWebSocketClient implements WebSocketHandler {
    private static final String AUTH_MESSAGE_TEMPLATE = "{\"action\":\"auth\",\"params\":\"%s\"}";
    private static final String SUBSCRIBE_MESSAGE_TEMPLATE = "{\"action\":\"subscribe\",\"params\":\"XT.BTC-USD\"}";
    private static final URI WEBSOCKET_URI = URI.create("wss://socket.polygon.io/crypto");

    private final PolygonApiKeysService polygonApiKeysService;
    private final ReactorNettyWebSocketClient client;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        System.out.println("Attempting to connect to WebSocket server at:");
        return client.execute(WEBSOCKET_URI, webSocketSession -> sendAuthMessage(webSocketSession)
                        .thenMany(receiveAndHandleMessages(webSocketSession, session))
                        .then())
                .onErrorResume(e -> {
                    log.error("Connection failed: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<Void> sendAuthMessage(WebSocketSession webSocketSession) {
        String authMessage = String.format(AUTH_MESSAGE_TEMPLATE, polygonApiKeysService.getApiKey());
        return webSocketSession.send(Mono.just(webSocketSession.textMessage(authMessage)));
    }

    private Flux<WebSocketMessage> receiveAndHandleMessages(WebSocketSession webSocketSession, WebSocketSession session) {
        return webSocketSession.receive()
                .doOnNext(message -> handleMessage(webSocketSession, session, message.getPayloadAsText()))
                .doOnError(error -> log.error("Error in WebSocket session: {}", error.getMessage()))
                .doOnTerminate(() -> log.info("WebSocket session terminated."));
    }

    private void handleMessage(WebSocketSession webSocketSession, WebSocketSession session, String payload) {
        log.info("Received message: {}", payload);
        if (payload.contains("\"status\":\"connected\"")) {
            sendSubscribeMessage(webSocketSession).subscribe();
        }
        session.send(Mono.just(session.textMessage(payload))).subscribe();
    }

    private Mono<Void> sendSubscribeMessage(WebSocketSession webSocketSession) {
        return webSocketSession.send(Mono.just(webSocketSession.textMessage(SUBSCRIBE_MESSAGE_TEMPLATE)))
                .onErrorContinue((throwable, o) -> log.error("Failed to send message: {}", throwable.getMessage()));
    }
}
