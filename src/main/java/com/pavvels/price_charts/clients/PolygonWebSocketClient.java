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
import reactor.core.publisher.Flux;
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
    private static final String SUBSCRIBE_MESSAGE = "{\"action\":\"subscribe\",\"params\":\"XT.BTC-USD\"}";
    private static final URI WEBSOCKET_URI = URI.create("wss://socket.polygon.io/crypto");

    private final PolygonApiKeysService polygonApiKeysService;
    private final ReactorNettyWebSocketClient client;
    private final Set<WebSocketSession> clientSessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private WebSocketSession polygonSession;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        manageActiveSessions(session);
        return sessionOperations(session);
    }

    private void manageActiveSessions(WebSocketSession session) {
        if (clientSessions.isEmpty()) {
            openPolygonConnection();
        }
        clientSessions.add(session);
    }

    private void openPolygonConnection() {
        client.execute(WEBSOCKET_URI, this::sessionHandler).subscribe();
    }

    private Mono<Void> sessionHandler(WebSocketSession session) {
        polygonSession = session;
        return sendAuthMessage(session).thenMany(receiveAuthMessage(session)).then();
    }

    private Mono<Void> sendAuthMessage(WebSocketSession webSocketSession) {
        String authMessage = String.format(AUTH_MESSAGE_TEMPLATE, polygonApiKeysService.getApiKey());
        return sendTextMessage(webSocketSession, authMessage);
    }

    private Flux<WebSocketMessage> receiveAuthMessage(WebSocketSession session) {
        return session.receive().doOnNext(message -> processReceivedMessage(session, message));
    }

    private void processReceivedMessage(WebSocketSession session, WebSocketMessage message) {
        var payload = message.getPayloadAsText();
        log.info("Received message: {}", payload);
        if (payload.contains("\"status\":\"auth_success\"")) {
            sendSubscribeMessage(session).subscribe();
        }
        broadcastMessageToClients(payload);
    }

    private void broadcastMessageToClients(String payload) {
        clientSessions.stream().filter(WebSocketSession::isOpen).forEach(session -> sendTextMessage(session, payload).subscribe());
    }

    private Mono<Void> sendSubscribeMessage(WebSocketSession webSocketSession) {
        return sendTextMessage(webSocketSession, SUBSCRIBE_MESSAGE);
    }

    private Mono<Void> sendTextMessage(WebSocketSession webSocketSession, String payload) {
        return webSocketSession.send(Mono.just(webSocketSession.textMessage(payload))).onErrorResume(e -> {
            log.error("Failed to send message: {}", e.getMessage());
            return Mono.empty();
        });
    }

    private Mono<Void> sessionOperations(WebSocketSession session) {
        return session.receive().map(WebSocketMessage::getPayloadAsText).doOnNext(this::broadcastMessageToClients).then().doFinally(sig -> closeClientSession(session)).onErrorResume(e -> {
            log.error("Connection failed: {}", e.getMessage());
            return Mono.empty();
        });
    }

    private void closeClientSession(WebSocketSession session) {
        clientSessions.remove(session);
        checkAndClosePolygonConnection();
        closeSession(session);
        log.info("Client WebSocket session closed.");
    }

    private void checkAndClosePolygonConnection() {
        if (clientSessions.isEmpty()) {
            closePolygonConnection();
        }
    }

    private void closePolygonConnection() {
        closeSession(polygonSession);
        log.info("Polygon WebSocket session closed.");
    }

    private void closeSession(WebSocketSession session) {
        if (session.isOpen()) {
            session.close().subscribe();
        }
    }
}
