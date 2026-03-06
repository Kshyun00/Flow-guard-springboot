package com.skala.process_monitoring_ai.domain.chat.service;

import com.skala.process_monitoring_ai.domain.chat.dto.ChatRequest;
import com.skala.process_monitoring_ai.domain.chat.dto.ChatResponse;
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
public class ChatService {

    private final WebClient fastApiClient;

    public ChatResponse chat(ChatRequest request, String userEmail) {
        try {
            return fastApiClient.post()
                    .uri("/ai/chat")
                    .bodyValue(new FastApiChatRequest(request.message(), userEmail))
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("FastAPI 챗봇 요청 실패 - status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException("챗봇 서비스에 일시적인 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY);
        } catch (Exception e) {
            log.error("FastAPI 챗봇 연결 오류: {}", e.getMessage());
            throw new BusinessException("챗봇 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    // FastAPI 전송용 내부 record
    private record FastApiChatRequest(String message, String userEmail) {}
}
