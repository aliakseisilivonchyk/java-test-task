package com.github.aliakseisilivonchyk.producer.service;

import com.github.aliakseisilivonchyk.producer.model.Event;
import com.github.aliakseisilivonchyk.producer.model.EventStatus;
import com.github.aliakseisilivonchyk.producer.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Map<String, Integer> getEventsStats() {
        Integer generatedCount = 0;
        Integer processedCount = 0;

        for(Event event : eventRepository.findAll()) {
            generatedCount++;
            if(event.getStatus() == EventStatus.PROCESSED) {
                processedCount++;
            }
        }

        var eventStats = new HashMap<String, Integer>();
        eventStats.put("generatedCount", generatedCount);
        eventStats.put("processedCount", processedCount);

        return eventStats;
    }

    public Event save(Event event) {
        if(event == null) {
            event = new Event();
        }

        return eventRepository.save(event);
    }
}
