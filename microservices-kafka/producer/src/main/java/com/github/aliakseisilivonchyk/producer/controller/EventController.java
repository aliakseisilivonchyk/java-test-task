package com.github.aliakseisilivonchyk.producer.controller;

import com.github.aliakseisilivonchyk.producer.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public Map<String, Integer> getEventsStats() {
        return eventService.getEventsStats();
    }
}
