package com.mayik.payments.infrastructure.output.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "payments") // Apunta a la colección real
public class PaymentDocument {

    @Id
    private UUID id;
    private BigDecimal amount;
    private String currency; // Para guardar la moneda de tu objeto Money
    private String status;
    private LocalDateTime updatedAt;

    // Constructor vacío requerido por Spring Data MongoDB
    public PaymentDocument() {}

    // Getters y Setters (puedes usar Lombok @Data si quieres)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}