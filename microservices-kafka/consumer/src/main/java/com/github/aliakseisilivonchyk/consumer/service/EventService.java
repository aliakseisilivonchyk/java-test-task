package com.github.aliakseisilivonchyk.consumer.service;

import com.github.aliakseisilivonchyk.consumer.dto.EventDto;
import com.github.aliakseisilivonchyk.consumer.model.Event;
import com.github.aliakseisilivonchyk.consumer.repository.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void save(String producer, Date createdAt, String type) {
        var event = new Event(producer, createdAt, type);
        eventRepository.save(event);
    }

    public List<EventDto> findAll(String producer, String type, Date startDate, Date endDate, Integer pageNumber, Integer pageSize) {
        Pageable pageable;
        if (pageNumber != null && pageSize != null) {
            pageable = PageRequest.of(pageNumber, pageSize);
        } else {
            pageable = Pageable.unpaged();
        }

        Specification<Event> specification = Specification.unrestricted();

        if (producer != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("producer"), producer));
        }

        if (type != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (startDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("receivedDate"), startDate));
        }

        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("receivedDate"), endDate));
        }

        return eventRepository.findAll(specification, pageable)
                .stream()
                .map(event -> new EventDto(event.getProducer(), event.getReceivedDate(), event.getType()))
                .collect(Collectors.toList());
    }
}

