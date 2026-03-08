package com.skala.process_monitoring_ai.domain.simulation.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.simulation.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/start")
    public ResponseEntity<JsonNode> start(
            @RequestParam(defaultValue = "0") int faultType,
            @RequestParam(defaultValue = "3.0") double interval,
            @RequestParam(required = false) String scenario) {
        return ResponseEntity.ok(simulationService.startSimulation(faultType, interval, scenario));
    }

    @PostMapping("/stop")
    public ResponseEntity<JsonNode> stop() {
        return ResponseEntity.ok(simulationService.stopSimulation());
    }

    @GetMapping("/status")
    public ResponseEntity<JsonNode> status() {
        return ResponseEntity.ok(simulationService.getStatus());
    }

    @GetMapping("/sensors/realtime")
    public ResponseEntity<JsonNode> realtimeSensors() {
        return ResponseEntity.ok(simulationService.getRealtimeSensors());
    }
}
