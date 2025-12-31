package com.mayik.payments.infrastructure.output.messaging.adapter;

import com.mayik.payments.infrastructure.output.persistence.entity.OutboxEvent;
import com.mayik.payments.infrastructure.output.persistence.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000) // Cada 5 segundos busca eventos pendientes
    public void publishEvents() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByProcessedFalse();

        for (OutboxEvent event : pendingEvents) {
            try {
                kafkaTemplate.send("payments-topic", event.getAggregateId(), event.getPayload());
                event.setProcessed(true);
                outboxRepository.save(event);
                log.info("🚀 Evento publicado desde Outbox: ID {}", event.getAggregateId());
            } catch (Exception e) {
                log.error("❌ Error enviando a Kafka: {}", e.getMessage());
            }
        }
    }
}
