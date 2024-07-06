package com.pavvels.bitcoincharts;

import com.pavvels.bitcoincharts.configs.BinanceUSWebSocketClient1;
import com.pavvels.bitcoincharts.configs.BinanceUSWebSocketClient2;
import com.pavvels.bitcoincharts.services.ApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BitcoinChartsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BitcoinChartsApplication.class, args);

        ApiService apiService = context.getBean(ApiService.class);
        apiService.performAction();
//        new BinanceUSWebSocketClient1(apiService).connectToBinanceStream();
//        new BinanceUSWebSocketClient2().connectToBinanceStream();
    }
}
