package com.mayik.payments.domain.ports.out;

import com.mayik.payments.domain.model.Payment;

public interface PaymentEventPublisher {
    void publish(Payment payment);
}