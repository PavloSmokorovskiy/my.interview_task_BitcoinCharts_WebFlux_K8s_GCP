package com.pavvels.bitcoincharts.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record ApiKeysConfig(@Value("${binance.api.key}") String apiKey,
                            @Value("${binance.api.secret}") String secretKey) {
}
