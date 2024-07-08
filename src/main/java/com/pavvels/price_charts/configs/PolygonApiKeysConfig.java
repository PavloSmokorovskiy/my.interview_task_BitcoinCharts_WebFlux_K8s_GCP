package com.pavvels.price_charts.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record PolygonApiKeysConfig(@Value("${polygon.api.key}") String apiKey) {
}
