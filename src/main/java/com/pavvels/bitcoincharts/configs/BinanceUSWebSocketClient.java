package com.pavvels.bitcoincharts.configs;

import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Mono;
import java.net.URI;

public class BinanceUSWebSocketClient {

    private static final String WS_URI = "wss://stream.binance.us:9443/ws/btcusdt@trade";

    public void connectToBinanceStream() {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(URI.create(WS_URI), session -> session.send(Mono.just(session.textMessage("YOUR_API_KEY_HERE")))
                        .thenMany(session.receive()
                                .map(WebSocketMessage::getPayloadAsText)
                                .doOnNext(System.out::println))
                        .then())
                .block();
    }
}

