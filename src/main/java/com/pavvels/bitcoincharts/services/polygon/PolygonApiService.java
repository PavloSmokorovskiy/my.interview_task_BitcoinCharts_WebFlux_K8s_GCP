package com.pavvels.bitcoincharts.services.polygon;

import com.pavvels.bitcoincharts.configs.polygon.PolygonApiKeysConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolygonApiService {

    private final PolygonApiKeysConfig apiKeys;

    public void performAction() {
        System.out.println("APIKey: " + apiKeys.apiKey());
    }

    public String getApiKey() {
        return apiKeys.apiKey();
    }
}
