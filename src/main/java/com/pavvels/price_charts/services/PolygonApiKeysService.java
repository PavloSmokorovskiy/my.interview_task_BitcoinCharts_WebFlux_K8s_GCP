package com.pavvels.price_charts.services;

import com.pavvels.price_charts.configs.PolygonApiKeysConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PolygonApiKeysService {

    private final PolygonApiKeysConfig apiKeys;

    public void performAction() {
        log.info("APIKey: {}", apiKeys.apiKey());
    }

    public String getApiKey() {
        return apiKeys.apiKey();
    }
}
