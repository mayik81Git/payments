package com.mayik.payments.domain.ports.out;

import com.mayik.payments.domain.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findById(UUID id);
}
