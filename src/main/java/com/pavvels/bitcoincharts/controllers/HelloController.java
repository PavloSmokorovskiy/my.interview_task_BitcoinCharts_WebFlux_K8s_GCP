package com.pavvels.bitcoincharts.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class HelloController {

    @GetMapping(value = "/")
    public Flux<String> hello() {
        return Flux.interval(Duration.ofSeconds(1))
                .take(10)
                .map(sequence -> " + 1 = " + (sequence + 1));
    }
}
