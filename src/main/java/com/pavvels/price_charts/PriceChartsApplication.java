package com.pavvels.price_charts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PriceChartsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PriceChartsApplication.class, args);

//        PolygonApiService polygonApiService = context.getBean(PolygonApiService.class);
//        polygonApiService.performAction();
//        PolygonWebSocketClient polygonWebSocketClient = context.getBean(PolygonWebSocketClient.class);
//        polygonWebSocketClient.connectToPolygonCryptoWebSocket();

    }
}
