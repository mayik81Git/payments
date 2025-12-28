package com.mayik.payments.infrastructure.config;

import com.mayik.payments.domain.model.Money;
import com.mayik.payments.domain.model.Payment;
import com.mayik.payments.domain.ports.out.PaymentRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.UUID;

//@Component
public class DataInitializer{

    private final PaymentRepository paymentRepository; // Usamos la interfaz de dominio

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public DataInitializer(PaymentRepository paymentRepository, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.paymentRepository = paymentRepository;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Bean
    CommandLineRunner finalTest() {
        return args -> {
            System.out.println("--- TEST FINAL ARQUITECTURA ---");

            // 1. Crear pago (Dominio)
            UUID id = UUID.randomUUID();
            Payment pago = Payment.create(id, new Money(new BigDecimal("150.00") , "USD"));

            // 2. Guardar (usando el adaptador)
            paymentRepository.save(pago);
            System.out.println("✅ Pago de dominio guardado.");

            // 3. Recuperar y verificar lógica
            paymentRepository.findById(id).ifPresent(p -> {
                System.out.println("✅ Pago recuperado. Estado inicial: " + p.getStatus());

                // Probar lógica de negocio
                p.complete();
                paymentRepository.save(p);
                System.out.println("✅ Pago completado y actualizado. Nuevo estado: " + p.getStatus());
            });
        };
    }
}

