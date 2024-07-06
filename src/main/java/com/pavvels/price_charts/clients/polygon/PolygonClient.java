package com.pavvels.price_charts.clients.polygon;

import com.pavvels.price_charts.services.polygon.PolygonApiService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Deprecated
@Component
public class PolygonClient {

    final PolygonApiService polygonApiService;
    private final WebClient webClient;

    public PolygonClient(PolygonApiService polygonApiService) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.polygon.io")
                .defaultHeader("Authorization", "Bearer " + polygonApiService.getApiKey())
                .build();
        this.polygonApiService = polygonApiService;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    private Mono<String> getStockData(String ticker) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/aggs/ticker/{ticker}/range/1/day/2020-06-01/2020-06-17")
                        .build(ticker))
                .retrieve()
                .bodyToMono(String.class);
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    private void getAndPrintStockData(String ticker) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/aggs/ticker/{ticker}/range/1/day/2020-06-01/2020-06-17")
                        .build(ticker))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(data -> System.out.println("Received data: " + data))
                .subscribe();
    }

}

