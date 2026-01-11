package com.github.aliakseisilivonchyk.producer.service;

import com.github.aliakseisilivonchyk.producer.configuration.KafkaProperties;
import com.github.aliakseisilivonchyk.producer.dto.MessageDto;
import com.github.aliakseisilivonchyk.producer.model.Event;
import com.github.aliakseisilivonchyk.producer.model.EventStatus;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class KafkaProducerService {

    private static final String PRODUCER_ID_HEADER = "producerId";

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaProperties kafkaProperties;
    private final ReplyingKafkaTemplate<String, MessageDto, String> replyingKafkaTemplate;
    private final EventService eventService;

    public KafkaProducerService(KafkaProperties kafkaProperties, ReplyingKafkaTemplate<String, MessageDto, String> replyingKafkaTemplate,
                                EventService eventService) {
        this.kafkaProperties = kafkaProperties;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.eventService = eventService;
    }

    @Scheduled(fixedRateString = "${com.github.aliakseisilivonchyk.kafka.message-rate}")
    public void produce() {
        Event event = eventService.save(null);
        ProducerRecord<String, MessageDto> producerRecord = createRecord(kafkaProperties.messageType(), kafkaProperties.producerId());

        logger.info("Sending: {}", producerRecord);

        replyingKafkaTemplate.sendAndReceive(producerRecord)
                .thenAccept((consumerRecord) -> {
                    logger.info("Received reply: {}", consumerRecord.value());

                    event.setStatus(EventStatus.PROCESSED);
                    eventService.save(event);
                });
    }

    private ProducerRecord<String, MessageDto> createRecord(String type, String producerId) {
        var message = new MessageDto(type);
        var producerRecord = new ProducerRecord<String, MessageDto>(kafkaProperties.requestTopic(), message);
        producerRecord.headers().add(PRODUCER_ID_HEADER, producerId.getBytes(StandardCharsets.UTF_8));

        return producerRecord;
    }
}
