package com.pavvels.price_charts;

import com.pavvels.price_charts.services.polygon.PolygonApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PriceChartsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PriceChartsApplication.class, args);

        PolygonApiService polygonApiService = context.getBean(PolygonApiService.class);
        polygonApiService.performAction();
//        PolygonWebSocketClient polygonWebSocketClient = context.getBean(PolygonWebSocketClient.class);
//        WebSocketSession webSocketSession = context.getBean(WebSocketSession.class);
//        polygonWebSocketClient.handle(webSocketSession);

    }
}
