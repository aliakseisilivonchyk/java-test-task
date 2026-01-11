package com.github.aliakseisilivonchyk.producer.repository;

import com.github.aliakseisilivonchyk.producer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
