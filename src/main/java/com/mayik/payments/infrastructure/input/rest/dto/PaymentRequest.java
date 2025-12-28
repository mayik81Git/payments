package com.mayik.payments.infrastructure.input.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(UUID id, BigDecimal amount, String currency) {}
