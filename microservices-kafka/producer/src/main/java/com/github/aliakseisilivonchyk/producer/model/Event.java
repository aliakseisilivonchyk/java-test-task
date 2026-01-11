package com.github.aliakseisilivonchyk.producer.model;

import jakarta.persistence.*;

@Entity
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.CREATED;

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
