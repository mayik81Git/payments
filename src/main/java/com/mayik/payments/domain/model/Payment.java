package com.mayik.payments.domain.model;

import com.mayik.payments.domain.exceptions.DomainException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private final UUID id;
    private final Money money;
    private PaymentStatus status;
    private LocalDateTime updatedAt;

    // Constructor privado: Obliga a usar el método estático de creación
    private Payment(UUID id, Money money) {
        this.id = id;
        this.money = money;
        this.status = PaymentStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }

    private Payment(UUID id, Money money, PaymentStatus status) {
        this.id = id;
        this.money = money;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // Método de factoría: Punto de entrada para nuevos pagos
    public static Payment create(UUID id, Money money) {
        return new Payment(id, money);
    }

    // Método para RECONSTRUIR un pago existente (usado por el Mapper)
    public static Payment restore(UUID id, Money money, PaymentStatus status) {
        return new Payment(id, money, status);
    }

    // Lógica de negocio pura (Transición de estado)
    public void complete() {
        if (this.status != PaymentStatus.PENDING) {
            throw new DomainException("Un pago solo puede completarse si está PENDIENTE");
        }
        this.status = PaymentStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters inmutables
    public UUID getId() { return id; }
    public Money getMoney() { return money; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
