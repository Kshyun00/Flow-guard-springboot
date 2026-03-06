package com.skala.process_monitoring_ai.domain.event.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.event.dto.EventCreateRequest;
import com.skala.process_monitoring_ai.domain.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<JsonNode> getEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(eventService.getEvents(page, size));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<JsonNode> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @PostMapping
    public ResponseEntity<JsonNode> createEvent(@Valid @RequestBody EventCreateRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }
}
