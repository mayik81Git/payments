package com.mayik.payments.application.services;

import com.mayik.payments.domain.exceptions.DomainException;
import com.mayik.payments.domain.model.Payment;
import com.mayik.payments.domain.ports.in.ProcessPaymentUseCase;
import com.mayik.payments.domain.ports.out.PaymentEventPublisher;
import com.mayik.payments.domain.ports.out.PaymentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService implements ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher eventPublisher;

    @Override
    @Transactional // Asegura consistencia atómica
    @CircuitBreaker(name = "paymentsService", fallbackMethod = "fallback")
    public void process(UUID paymentId) {
        log.info("Iniciando procesamiento del pago: {}", paymentId);

        // 1. Recuperar el Agregado del puerto de salida
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DomainException("Pago no encontrado: " + paymentId));

        // 2. Ejecutar lógica de negocio (El Agregado valida sus reglas)
        payment.complete();

        // 3. Persistir los cambios (Resiliencia aplicada en el adaptador)
        paymentRepository.save(payment);

        // 4. Notificar al resto del sistema vía Kafka
        eventPublisher.publish(payment);

        log.info("Pago {} procesado y evento publicado exitosamente", paymentId);
    }

    // El fallback debe tener la misma firma + la excepción
    public void fallback(UUID id, Exception e) {
        System.out.println("### FALLBACK EJECUTADO por error: " + e.getMessage());
    }
}
