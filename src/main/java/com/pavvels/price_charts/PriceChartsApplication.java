package com.pavvels.price_charts;

import com.pavvels.price_charts.services.PolygonApiKeysService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PriceChartsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PriceChartsApplication.class, args);

        PolygonApiKeysService polygonApiKeysService = context.getBean(PolygonApiKeysService.class);
        polygonApiKeysService.performAction();

    }
}
