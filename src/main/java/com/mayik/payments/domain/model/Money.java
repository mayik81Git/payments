package com.mayik.payments.domain.model;

import com.mayik.payments.domain.exceptions.DomainException;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("El monto debe ser positivo y no nulo");
        }
        if (currency == null || currency.length() != 3) {
            throw new DomainException("El código de moneda debe ser de 3 caracteres (ISO)");
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
