package com.mayik.payments.infrastructure.input.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayik.payments.infrastructure.input.messaging.dto.PaymentEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payments-topic", groupId = "payments-group")
    public void consume(String payload) {
        try {
            // Si el payload viene envuelto en comillas por la serialización del Outbox, lo limpiamos
            if (payload.startsWith("\"") && payload.endsWith("\"")) {
                payload = objectMapper.readValue(payload, String.class);
            }

            PaymentEventDTO event = objectMapper.readValue(payload, PaymentEventDTO.class);
            log.info("✅ Mensaje procesado con éxito: {} - Tipo: {}", event.id(), event.type());

        } catch (Exception e) {
            log.error("❌ Error de deserialización: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @DltHandler
    public void handleDlt(String payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("💀 MENSAJE EN DLQ: Topic {}. Datos: {}", topic, payload);
    }
}
