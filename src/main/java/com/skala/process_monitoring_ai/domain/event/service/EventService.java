package com.skala.process_monitoring_ai.domain.event.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.event.dto.EventCreateRequest;
import com.skala.process_monitoring_ai.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final WebClient fastApiClient;

    public JsonNode getEvents(int page, int size) {
        try {
            return fastApiClient.get()
                    .uri(uri -> uri.path("/ai/events")
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 이벤트 목록 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("이벤트 목록을 불러올 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode getEvent(Long eventId) {
        try {
            return fastApiClient.get()
                    .uri("/ai/events/{eventId}", eventId)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw BusinessException.notFound("이벤트를 찾을 수 없습니다.");
        } catch (WebClientResponseException e) {
            log.error("FastAPI 이벤트 조회 실패 - status: {}", e.getStatusCode());
            throw new BusinessException("이벤트를 불러올 수 없습니다.", HttpStatus.BAD_GATEWAY);
        }
    }

    public JsonNode createEvent(EventCreateRequest request) {
        try {
            return fastApiClient.post()
                    .uri("/ai/events")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 이벤트 생성 실패 - status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException("이벤트 생성에 실패했습니다.", HttpStatus.BAD_GATEWAY);
        }
    }
}
