package com.pavvels.price_charts.clients.polygon;

import com.pavvels.price_charts.services.polygon.PolygonApiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.socket.WebSocketMessage;

import java.net.URI;

@Component
public class PolygonWebSocketClient implements WebSocketHandler {

    private final PolygonApiService polygonApiService;
    private final ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

    @Autowired
    public PolygonWebSocketClient(PolygonApiService polygonApiService) {
        this.polygonApiService = polygonApiService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        System.out.println("Attempting to connect to WebSocket server at:");
        URI uri = URI.create("wss://socket.polygon.io/crypto");
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        return client.execute(uri, webSocketSession ->
                        webSocketSession.send(
                                        Mono.just(webSocketSession
                                                .textMessage("{\"action\":\"auth\",\"params\":\""
                                                        + polygonApiService.getApiKey() + "\"}"))
                                ).thenMany(webSocketSession.receive()
                                        .doOnNext(message -> {
                                            String payload = message.getPayloadAsText();
                                            System.out.println("Received message: " + payload);
                                            if (payload.contains("\"status\":\"connected\"")) {
                                                webSocketSession.send(Mono.just(webSocketSession.textMessage(
                                                                "{\"action\":\"subscribe\",\"params\":\"XT.BTC-USD\"}")))
                                                        .onErrorContinue((throwable, o) -> System.err.println(
                                                                "Failed to send message: " + throwable.getMessage()))
                                                        .subscribe();
                                            }
                                            session.send(Mono.just(session.textMessage(payload))).subscribe();
                                        })
                                        .doOnError(error -> System.err.println("Error in WebSocket session: " + error.getMessage()))
                                        .doOnTerminate(() -> System.out.println("WebSocket session terminated."))
                                )
                                .then())
                .onErrorResume(e -> {
                    System.err.println("Connection failed: " + e.getMessage());
                    return Mono.empty();
                });
    }

//    @PostConstruct
//    public void init() {
//        this.connectToPolygonCryptoWebSocket();
//    }

    public void connectToPolygonCryptoWebSocket() {
        URI uri = URI.create("wss://socket.polygon.io/crypto");

        client.execute(uri, session -> session.send(
                                Mono.just(session.textMessage("{\"action\":\"auth\",\"params\":\"" + polygonApiService.getApiKey() + "\"}")))
                        .thenMany(session.send(Mono.just(session.textMessage("{\"action\":\"subscribe\",\"params\":\"XT.BTC-USD\"}"))))
                        .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText)
                                .doOnNext(message -> System.out.println("Received message: " + message))
                                .doOnError(error -> System.err.println("Error in WebSocket connection: " + error))
                                .doOnTerminate(() -> System.out.println("WebSocket connection closed.")))
                        .then())
                .subscribe();
    }

    @Deprecated
    @SuppressWarnings("unused")
    private void connectToPolygonStocksWebSocket() {
        client.execute(
                        URI.create("wss://socket.polygon.io/stocks"),
                        session -> session.send(
                                        Mono.just(session.textMessage("action=auth&params=" + polygonApiService.getApiKey())))
                                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText)
                                        .doOnNext(System.out::println))
                                .then())
                .block();
    }

    @Deprecated
    @SuppressWarnings("unused")
    private void connectAndPrintStocksWebSocketData() {
        client.execute(
                        URI.create("wss://socket.polygon.io/stocks"),
                        session -> session.send(Mono.just(session.textMessage("action=auth&params=" + polygonApiService.getApiKey())))
                                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText)
                                        .doOnNext(message -> System.out.println("Received message: " + message)))
                                .then())
                .subscribe();
    }
}
