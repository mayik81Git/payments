package com.mayik.payments.domain.ports.in;

import java.util.UUID;

public interface ProcessPaymentUseCase {
    void process(UUID paymentId);
}
