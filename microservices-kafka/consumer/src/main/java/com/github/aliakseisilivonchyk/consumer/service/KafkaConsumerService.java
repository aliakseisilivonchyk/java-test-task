package com.github.aliakseisilivonchyk.consumer.service;

import com.github.aliakseisilivonchyk.consumer.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class KafkaConsumerService {

    private static final String PRODUCER_ID_HEADER = "producerId";

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final EventService eventService;

    public KafkaConsumerService(EventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(topics = "#{'${com.github.aliakseisilivonchyk.kafka.request-topic}'.split(',')}")
    @SendTo
    public String consume(MessageDto message, @Header(PRODUCER_ID_HEADER) String producerId,
                          @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {
        logger.info("Consuming: {}", message);

        var receivedDate = Date.from(new Timestamp(timestamp).toInstant());

        eventService.save(producerId, receivedDate, message.type());

        return "Processed " + message;
    }
}
