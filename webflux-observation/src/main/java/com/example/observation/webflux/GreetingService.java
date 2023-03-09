package com.example.observation.webflux;

import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.function.Supplier;

@Service
@Slf4j
class GreetingService {

    private final Supplier<Long> latency = () -> new Random().nextLong(500);
    private final ObservationRegistry registry;
    private final WebClient webClient;

    GreetingService(ObservationRegistry registry, final WebClient.Builder webClientBuilder) {
        this.registry = registry;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8686")
                                 .observationRegistry(registry)
                                 .build();
    }

    public Mono<Greeting> greeting(String name) {
        Long lat = latency.get();
        return Mono
                       .just(new Greeting(name))
                       .delayElement(Duration.ofMillis(lat))
                       .name("greeting.call")
                       .tag("latency", lat > 250 ? "high" : "low")
                       .tap(Micrometer.observation(registry));
    }

    public Mono<Greeting> greetingClient(String name) {
        Long lat = latency.get();
        return this.webClient.get().uri("/hello/{name}", name)
                       .accept(MediaType.APPLICATION_JSON)
                       .retrieve().bodyToMono(Greeting.class)
                       .name("greeting.client.call")
                       .tag("latency", lat > 250 ? "high" : "low")
                       .tap(Micrometer.observation(registry));
    }
}

record Greeting(String name) {
}