package com.mayik.payments.infrastructure.input.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayik.payments.infrastructure.input.messaging.dto.PaymentEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentConsumer {

    private final ObjectMapper objectMapper;

    public PaymentConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payments-topic", groupId = "payments-group")
    public void consume(String message) {
        try {
            // Convertimos el JSON manual para saltarnos el lío de los paquetes
            PaymentEventDTO event = objectMapper.readValue(message, PaymentEventDTO.class);

            System.out.println("✅ Evento procesado: " + event.id() + " - " + event.status());

        } catch (JsonProcessingException e) {
            System.err.println("❌ Error de formato JSON: " + e.getMessage());
            // No lances excepción aquí si quieres que el mensaje se ignore y no reintente
        }
    }
}
