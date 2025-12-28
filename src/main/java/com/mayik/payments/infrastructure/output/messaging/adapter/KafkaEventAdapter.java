package com.mayik.payments.infrastructure.output.messaging.adapter;

import com.mayik.payments.domain.model.Payment;
import com.mayik.payments.domain.ports.out.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventAdapter implements PaymentEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Payment payment) {
        PaymentEventDTO event = new PaymentEventDTO(payment.getId().toString(), payment.getStatus().name());
        kafkaTemplate.send("payments-topic", event.id(), event);
    }

    public record PaymentEventDTO(String id, String status) {}
}
