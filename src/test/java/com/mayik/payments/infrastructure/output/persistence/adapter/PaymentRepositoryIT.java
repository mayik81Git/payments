package com.mayik.payments.infrastructure.output.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;

import com.mayik.payments.domain.model.Money;
import com.mayik.payments.domain.model.Payment;
import com.mayik.payments.domain.model.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class PaymentRepositoryIT {

    // Levanta un MongoDB real en un puerto aleatorio
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.4");

    @Autowired
    private MongoPaymentRepositoryAdapter adapter;

    // Sobreescribe la URI de conexión de tu application.yml con la del contenedor temporal
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void debeGuardarYRecuperarUnPago() {
        // Arrange
        UUID id = UUID.randomUUID();
        Payment payment = Payment.create(id, new Money(new BigDecimal("250.00"), "EUR"));

        // Act
        adapter.save(payment);
        Optional<Payment> savedPayment = adapter.findById(id);

        // Assert
        assertThat(savedPayment).isPresent();
        assertThat(savedPayment.get().getId()).isEqualTo(id);
        assertThat(savedPayment.get().getMoney().getAmount()).isEqualByComparingTo("250.00");
        assertThat(savedPayment.get().getStatus()).isEqualTo(PaymentStatus.PENDING);
    }
}