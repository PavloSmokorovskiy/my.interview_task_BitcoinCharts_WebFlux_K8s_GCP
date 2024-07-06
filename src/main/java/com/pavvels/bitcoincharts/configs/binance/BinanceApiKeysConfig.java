package com.pavvels.bitcoincharts.configs.binance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public record BinanceApiKeysConfig(@Value("${binance.api.key}") String apiKey,
                                   @Value("${binance.api.secret}") String secretKey) {
}
