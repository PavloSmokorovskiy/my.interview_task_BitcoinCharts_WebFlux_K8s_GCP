package com.pavvels.price_charts.clients.polygon;

import com.pavvels.price_charts.services.polygon.PolygonApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class PolygonWebSocketClient {

    final PolygonApiService polygonApiService;

    private final ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

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

