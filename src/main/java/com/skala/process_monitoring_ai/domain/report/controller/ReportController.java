package com.skala.process_monitoring_ai.domain.report.controller;

import com.skala.process_monitoring_ai.domain.report.dto.ReportResponse;
import com.skala.process_monitoring_ai.domain.report.service.ReportService;
import com.skala.process_monitoring_ai.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReports() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getReports()));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ApiResponse<ReportResponse>> getReport(@PathVariable String reportId) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getReport(reportId)));
    }
}
