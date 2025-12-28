package com.mayik.payments.infrastructure.input.messaging.dto;

import java.io.Serializable;

/**
 * DTO que representa el evento de pago para Kafka.
 * Implementa Serializable para asegurar la compatibilidad con algunos serializadores,
 * aunque con JSON no es estrictamente obligatorio, es una buena práctica.
 */
public record PaymentEventDTO(
        String id,
        String status
) implements Serializable {}
