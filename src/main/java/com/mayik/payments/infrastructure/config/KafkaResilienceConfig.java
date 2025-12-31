package com.mayik.payments.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
@EnableKafka
public class KafkaResilienceConfig {

    @Bean
    public RetryTopicConfiguration paymentRetryTopic(KafkaTemplate<String, Object> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .fixedBackOff(5000) // 5s de pausa
                .maxAttempts(3)
                .includeTopic("payments-topic")
                .dltHandlerMethod("paymentConsumer", "handleDlt")
                .create(template);
    }
}
