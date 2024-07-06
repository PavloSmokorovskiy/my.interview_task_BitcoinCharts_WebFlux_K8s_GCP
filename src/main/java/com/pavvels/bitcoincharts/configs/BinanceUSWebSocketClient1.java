package com.pavvels.bitcoincharts.configs;

import com.pavvels.bitcoincharts.services.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
public class BinanceUSWebSocketClient1 {

    private final ApiService apiService;

    private static final String WS_URI = "wss://stream.binance.us:9443/ws/btcusdt@trade";

    public void connectToBinanceStream() {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(URI.create(WS_URI), session ->
                        session.send(Mono.just(session.textMessage(createAuthMessage())))
                                .thenMany(session.receive()
                                        .map(WebSocketMessage::getPayloadAsText)
                                        .doOnNext(System.out::println))
                                .then())
                .block();
    }

    private String createAuthMessage() {
        return String.format("{\"method\": \"SUBSCRIBE\", \"params\": [\"btcusdt@trade\"], \"id\": 1, \"api_key\": \"%s\"}", apiService.getApiKey());
    }
}


