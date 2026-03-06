package com.skala.process_monitoring_ai.domain.report.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<JsonNode> getReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(reportService.getReports(page, size));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<JsonNode> getReport(@PathVariable String reportId) {
        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @PostMapping("/generate")
    public ResponseEntity<JsonNode> generateReport(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(reportService.generateReport(request));
    }
}
