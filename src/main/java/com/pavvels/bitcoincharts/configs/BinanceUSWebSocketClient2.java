package com.pavvels.bitcoincharts.configs;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

import java.net.URI;

public class BinanceUSWebSocketClient2 {

    private static final String WS_URI = "wss://stream.binance.us:9443/ws/btcusdt@trade";

    public void connectToBinanceStream() {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(URI.create(WS_URI), session ->
                        session.receive()
                                .map(WebSocketMessage::getPayloadAsText)
                                .map(this::processMessage)
                                .then())
                .block();
    }

    private String processMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            if (json.getString("e").equals("trade")) {
                String price = json.getString("p");
                System.out.println("Current BTC Price: " + price);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static void main(String[] args) {
        new BinanceUSWebSocketClient2().connectToBinanceStream();
    }
}


