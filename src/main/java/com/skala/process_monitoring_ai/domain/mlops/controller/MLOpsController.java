package com.skala.process_monitoring_ai.domain.mlops.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.mlops.service.MLOpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mlops")
@RequiredArgsConstructor
public class MLOpsController {

    private final MLOpsService mlopsService;

    // ===== 드리프트 =====

    @GetMapping("/drift/status")
    public ResponseEntity<JsonNode> getDriftStatus() {
        return ResponseEntity.ok(mlopsService.getDriftStatus());
    }

    @GetMapping("/drift/result")
    public ResponseEntity<JsonNode> getDriftResult() {
        return ResponseEntity.ok(mlopsService.getDriftResult());
    }

    @GetMapping("/drift/logs")
    public ResponseEntity<JsonNode> getDriftLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(mlopsService.getDriftLogs(page, size));
    }

    @PostMapping("/drift/acknowledge/{driftId}")
    public ResponseEntity<JsonNode> acknowledgeDrift(@PathVariable int driftId) {
        return ResponseEntity.ok(mlopsService.acknowledgeDrift(driftId));
    }

    // ===== 모델 =====

    @GetMapping("/models")
    public ResponseEntity<JsonNode> getModels() {
        return ResponseEntity.ok(mlopsService.getModels());
    }

    @GetMapping("/models/active")
    public ResponseEntity<JsonNode> getActiveModel() {
        return ResponseEntity.ok(mlopsService.getActiveModel());
    }

    @PostMapping("/models/{versionId}/deploy")
    public ResponseEntity<JsonNode> deployModel(@PathVariable int versionId) {
        return ResponseEntity.ok(mlopsService.deployModel(versionId));
    }

    @PostMapping("/models/{versionId}/rollback")
    public ResponseEntity<JsonNode> rollbackModel(@PathVariable int versionId) {
        return ResponseEntity.ok(mlopsService.rollbackModel(versionId));
    }

    // ===== 재학습 =====

    @PostMapping("/retrain/request")
    public ResponseEntity<JsonNode> requestRetrain(
            @RequestParam(required = false) Integer driftLogId,
            @RequestParam(required = false) String description) {
        return ResponseEntity.ok(mlopsService.requestRetrain(driftLogId, description));
    }

    @GetMapping("/retrain/jobs")
    public ResponseEntity<JsonNode> getRetrainJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(mlopsService.getRetrainJobs(page, size));
    }

    @PostMapping("/retrain/{jobId}/approve")
    public ResponseEntity<JsonNode> approveRetrain(@PathVariable int jobId) {
        return ResponseEntity.ok(mlopsService.approveRetrain(jobId));
    }

    @PostMapping("/retrain/{jobId}/reject")
    public ResponseEntity<JsonNode> rejectRetrain(@PathVariable int jobId) {
        return ResponseEntity.ok(mlopsService.rejectRetrain(jobId));
    }

    @GetMapping("/retrain/{jobId}/status")
    public ResponseEntity<JsonNode> getRetrainStatus(@PathVariable int jobId) {
        return ResponseEntity.ok(mlopsService.getRetrainStatus(jobId));
    }

    // ===== 성능 =====

    @GetMapping("/performance")
    public ResponseEntity<JsonNode> getPerformance(
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(mlopsService.getPerformance(limit));
    }
}
