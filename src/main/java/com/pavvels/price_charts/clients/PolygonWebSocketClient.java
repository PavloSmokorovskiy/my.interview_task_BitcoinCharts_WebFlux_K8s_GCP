package com.pavvels.price_charts.clients;

import com.pavvels.price_charts.services.PolygonApiKeysService;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Set<WebSocketSession> clientSessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private WebSocketSession polygonSession;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        if (clientSessions.isEmpty()) {
            openPolygonConnection();
        }
        clientSessions.add(session);

        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(this::broadcastMessageToClients)
                .then()
                .doFinally(sig -> {
                    clientSessions.remove(session);
                    if (clientSessions.isEmpty()) {
                        closePolygonConnection();
                    }
                    if (session.isOpen()) {
                        session.close().subscribe();
                    }
                    log.info("Client WebSocket session closed.");
                })
                .onErrorResume(e -> {
                    log.error("Connection failed: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    private void openPolygonConnection() {
        client.execute(WEBSOCKET_URI, session -> {
            polygonSession = session;
            return sendAuthMessage(session)
                    .thenMany(session.receive()
                            .doOnNext(message -> {
                                log.info("Received message: {}", message.getPayloadAsText());
                                if (message.getPayloadAsText().contains("\"status\":\"auth_success\"")) {
                                    sendSubscribeMessage(session).subscribe();
                                }
                                broadcastMessageToClients(message.getPayloadAsText());
                            }))
                    .then();
        }).subscribe();
    }


    private void closePolygonConnection() {
        if (polygonSession != null && polygonSession.isOpen()) {
            polygonSession.close().subscribe();
            log.info("Polygon WebSocket session closed.");
        }
    }

    private Mono<Void> sendAuthMessage(WebSocketSession webSocketSession) {
        String authMessage = String.format(AUTH_MESSAGE_TEMPLATE, polygonApiKeysService.getApiKey());
        return webSocketSession.send(Mono.just(webSocketSession.textMessage(authMessage)));
    }

    private void broadcastMessageToClients(String payload) {
        clientSessions.forEach(session -> {
            if (session.isOpen()) {
                session.send(Mono.just(session.textMessage(payload)))
                        .onErrorResume(e -> {
                            log.error("Failed to send message to client: {}", e.getMessage());
                            return Mono.empty();
                        }).subscribe();
            }
        });
    }

    private Mono<Void> sendSubscribeMessage(WebSocketSession webSocketSession) {
        String subscribeMessage = String.format(SUBSCRIBE_MESSAGE_TEMPLATE);
        return webSocketSession.send(Mono.just(webSocketSession.textMessage(subscribeMessage)))
                .onErrorContinue((throwable, o) -> log.error("Failed to send subscribe message: {}", throwable.getMessage()));
    }
}
