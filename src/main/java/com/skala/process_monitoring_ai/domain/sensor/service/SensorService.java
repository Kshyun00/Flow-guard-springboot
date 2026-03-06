package com.skala.process_monitoring_ai.domain.sensor.service;

import com.skala.process_monitoring_ai.domain.sensor.dto.SensorDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorService {

    private final WebClient fastApiClient;

    // 연결된 클라이언트 목록 (thread-safe)
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        emitters.add(emitter);
        log.info("SSE 클라이언트 연결. 현재 연결 수: {}", emitters.size());

        // 연결 즉시 초기 데이터 전송
        fetchAndBroadcast();

        return emitter;
    }

    // FastAPI에서 3초마다 데이터 가져와서 모든 클라이언트에 전송
    @Scheduled(fixedDelay = 3000)
    public void fetchAndBroadcast() {
        if (emitters.isEmpty()) return;

        fetchSensorData().forEach(data -> broadcast("sensor-data", data));
    }

    private List<SensorDataResponse> fetchSensorData() {
        try {
            return fastApiClient.get()
                    .uri("/sensors/latest")
                    .retrieve()
                    .bodyToFlux(SensorDataResponse.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("FastAPI 센서 데이터 조회 실패: {}", e.getMessage());
            return List.of();
        }
    }

    private void broadcast(String eventName, Object data) {
        List<SseEmitter> dead = new CopyOnWriteArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }

        emitters.removeAll(dead);
    }
}
