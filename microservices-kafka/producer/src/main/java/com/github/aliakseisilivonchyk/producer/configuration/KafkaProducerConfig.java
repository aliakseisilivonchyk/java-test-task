package com.github.aliakseisilivonchyk.producer.configuration;

import com.github.aliakseisilivonchyk.producer.dto.MessageDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public NewTopic requestTopic() {
        return TopicBuilder.name(kafkaProperties.requestTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic replyTopic() {
        return TopicBuilder.name(kafkaProperties.replyTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer(
            ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties(kafkaProperties.replyTopic());
        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, MessageDto, String> replyingKafkaTemplate(
            ProducerFactory<String, MessageDto> producerFactory, KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer) {
        return new ReplyingKafkaTemplate<>(producerFactory, kafkaMessageListenerContainer);
    }
}
