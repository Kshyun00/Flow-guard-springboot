package com.skala.process_monitoring_ai.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FastApiConfig {

    @Value("${fastapi.base-url}")
    private String fastApiBaseUrl;

    @Bean
    public WebClient fastApiClient() {
        return WebClient.builder()
                .baseUrl(fastApiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
