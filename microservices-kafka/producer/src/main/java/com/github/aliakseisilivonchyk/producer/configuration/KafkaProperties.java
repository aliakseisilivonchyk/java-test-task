package com.github.aliakseisilivonchyk.producer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.github.aliakseisilivonchyk.kafka")
public record KafkaProperties(String requestTopic, String replyTopic, String messageType, String producerId) {}
