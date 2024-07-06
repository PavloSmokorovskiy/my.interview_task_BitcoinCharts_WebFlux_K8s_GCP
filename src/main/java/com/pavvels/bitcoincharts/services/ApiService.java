package com.pavvels.bitcoincharts.services;

import com.pavvels.bitcoincharts.configs.ApiKeysConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final ApiKeysConfig apiKeys;

    public void performAction() {
        System.out.println("APIKey: " + apiKeys.apiKey());
        System.out.println("Secret: " + apiKeys.secretKey());
    }

    public String getApiKey(){
        return apiKeys.apiKey();
    }

    public String getSecret(){
        return apiKeys.secretKey();
    }
}
