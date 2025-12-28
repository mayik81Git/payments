package com.mayik.payments.domain.model;

import com.mayik.payments.domain.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {

    private final UUID TEST_ID = UUID.randomUUID();
    private final Money TEST_MONEY = new Money(new BigDecimal("100.00"), "EUR");

    @Test
    @DisplayName("Un pago nuevo debe estar PENDIENTE por defecto")
    void newPaymentShouldBePending() {
        // Arrange & Act
        Payment payment = Payment.create(TEST_ID, TEST_MONEY);

        // Assert
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("Completar un pago PENDIENTE debe cambiar su estado a COMPLETED")
    void completePendingPayment() {
        // Arrange
        Payment payment = Payment.create(TEST_ID, TEST_MONEY);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);

        // Act
        payment.complete();

        // Assert
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    @DisplayName("No se puede completar un pago que ya está FAILED")
    void cannotCompleteAFailedPayment() {
        // Arrange: Creamos un pago usando el método restore para forzar el estado
        Payment failedPayment = Payment.restore(TEST_ID, TEST_MONEY, PaymentStatus.FAILED);
        assertThat(failedPayment.getStatus()).isEqualTo(PaymentStatus.FAILED);

        // Act & Assert: Verificamos que se lanza la excepción DomainException
        DomainException thrown = assertThrows(DomainException.class, failedPayment::complete);

        assertThat(thrown.getMessage()).contains("Un pago solo puede completarse si está PENDIENTE");
    }
}