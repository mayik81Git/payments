package com.mayik.payments.domain.exceptions;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    // Opcional: Útil si quieres envolver otra excepción técnica pero categorizarla como de dominio
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
