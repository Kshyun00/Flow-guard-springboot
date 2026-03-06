package com.skala.process_monitoring_ai.domain.inference.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.inference.dto.InferenceRequest;
import com.skala.process_monitoring_ai.domain.inference.service.InferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inference")
@RequiredArgsConstructor
public class InferenceController {

    private final InferenceService inferenceService;

    @GetMapping("/sample")
    public ResponseEntity<JsonNode> getSampleData(
            @RequestParam(defaultValue = "1") int faultType,
            @RequestParam(defaultValue = "50") int rows
    ) {
        return ResponseEntity.ok(inferenceService.getSampleData(faultType, rows));
    }

    @PostMapping
    public ResponseEntity<JsonNode> runInference(@Valid @RequestBody InferenceRequest request) {
        return ResponseEntity.ok(inferenceService.runInference(request.values()));
    }
}
