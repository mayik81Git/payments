package com.mayik.payments.application.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayik.payments.domain.model.Payment;
import com.mayik.payments.domain.ports.out.PaymentRepository;
import com.mayik.payments.infrastructure.input.messaging.dto.PaymentEventDTO;
import com.mayik.payments.infrastructure.output.persistence.entity.OutboxEvent;
import com.mayik.payments.infrastructure.output.persistence.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void process(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        payment.complete();
        paymentRepository.save(payment);

        try {
            // Creamos el DTO antes de serializarlo
            PaymentEventDTO eventDTO = new PaymentEventDTO(
                    payment.getId().toString(),
                    payment.getMoney().getAmount(),
                    payment.getMoney().getCurrency(),
                    "PAYMENT_CREATED", // <-- Aquí ya no será nulo
                    LocalDateTime.now()
            );

            OutboxEvent event = OutboxEvent.builder()
                    .aggregateId(payment.getId().toString())
                    .type(eventDTO.type())
                    .payload(objectMapper.writeValueAsString(eventDTO)) // Serializamos el DTO, no el Payment
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();

            outboxRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar el DTO para el Outbox", e);
        }
    }
}
