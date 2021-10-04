package com.kafka.kafkasync;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfig {

        @Value("${kafka.group.id}")
        private String groupId;
        @Value("${kafka.reply.topic}")
        private String replyTopic;




        @Bean
        public ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate(ProducerFactory<String, Student> pf,
                                                                                    ConcurrentKafkaListenerContainerFactory<String, Result> factory) {
            ConcurrentMessageListenerContainer<String, Result> replyContainer = factory.createContainer(replyTopic);
            replyContainer.getContainerProperties().setMissingTopicsFatal(false);
            replyContainer.getContainerProperties().setGroupId(groupId);

            return new ReplyingKafkaTemplate<>(pf, replyContainer);
        }
        @Bean
        public KafkaTemplate<String, Result> replyTemplate(ProducerFactory<String, Result> pf,
                                                           ConcurrentKafkaListenerContainerFactory<String, Result> factory) {
            KafkaTemplate<String, Result> kafkaTemplate = new KafkaTemplate<>(pf);
            factory.getContainerProperties().setMissingTopicsFatal(false);
            factory.setReplyTemplate(kafkaTemplate);
            return kafkaTemplate;

    }

}
