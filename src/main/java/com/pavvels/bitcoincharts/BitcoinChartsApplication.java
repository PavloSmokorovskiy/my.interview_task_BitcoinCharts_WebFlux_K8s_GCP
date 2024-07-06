package com.pavvels.bitcoincharts;

import com.pavvels.bitcoincharts.clients.polygon.PolygonWebSocketClient;
import com.pavvels.bitcoincharts.services.polygon.PolygonApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BitcoinChartsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BitcoinChartsApplication.class, args);

        PolygonApiService polygonApiService = context.getBean(PolygonApiService.class);
        polygonApiService.performAction();
        PolygonWebSocketClient polygonWebSocketClient = context.getBean(PolygonWebSocketClient.class);
        polygonWebSocketClient.connectToPolygonCryptoWebSocket();

    }
}
