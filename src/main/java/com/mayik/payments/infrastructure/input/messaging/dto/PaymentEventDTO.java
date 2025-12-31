package com.mayik.payments.infrastructure.input.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que representa el evento de pago para Kafka.
 * Implementa Serializable para asegurar la compatibilidad con algunos serializadores,
 * aunque con JSON no es estrictamente obligatorio, es una buena práctica.
 */
public record PaymentEventDTO(
        String id,
        BigDecimal amount,
        String currency,
        String type, // PAYMENT_CREATED, PAYMENT_VALIDATED, etc.

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp
) {
    /**
     * Constructor compacto para validaciones opcionales antes de la creación
     */
    public PaymentEventDTO {
        if (id == null || type == null) {
            throw new IllegalArgumentException("El ID y el Tipo de evento no pueden ser nulos");
        }
    }

    /**
     * Helper para facilitar la creación de nuevas versiones del evento
     * manteniendo los datos originales (Patrón Wither)
     */
    public PaymentEventDTO withType(String newType) {
        return new PaymentEventDTO(id, amount, currency, newType, LocalDateTime.now());
    }
}
