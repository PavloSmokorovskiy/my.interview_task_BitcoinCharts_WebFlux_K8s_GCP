package com.pavvels.bitcoincharts.services.binance;

import com.pavvels.bitcoincharts.configs.binance.BinanceApiKeysConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Deprecated
@Service
@RequiredArgsConstructor
public class BinanceApiService {

    private final BinanceApiKeysConfig apiKeys;

    @SuppressWarnings("unused")
    private void performAction() {
        System.out.println("APIKey: " + apiKeys.apiKey());
        System.out.println("Secret: " + apiKeys.secretKey());
    }

    public String getApiKey(){
        return apiKeys.apiKey();
    }
    @SuppressWarnings("unused")
    private String getSecret(){
        return apiKeys.secretKey();
    }
}
