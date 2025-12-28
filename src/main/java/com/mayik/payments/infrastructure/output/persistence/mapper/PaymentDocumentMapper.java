package com.mayik.payments.infrastructure.output.persistence.mapper;

import com.mayik.payments.domain.model.Money;
import com.mayik.payments.domain.model.Payment;
import com.mayik.payments.domain.model.PaymentStatus;
import com.mayik.payments.infrastructure.output.persistence.entity.PaymentDocument;
import org.springframework.stereotype.Component;

@Component
public class PaymentDocumentMapper {

    // Convierte del Dominio (inmutable) a la BD (mutable)
    public PaymentDocument toDocument(Payment domainPayment) {
        PaymentDocument doc = new PaymentDocument();
        doc.setId(domainPayment.getId());
        doc.setAmount(domainPayment.getMoney().getAmount());
        doc.setCurrency(domainPayment.getMoney().getCurrency());
        doc.setStatus(domainPayment.getStatus().name());
        doc.setUpdatedAt(domainPayment.getUpdatedAt());
        return doc;
    }

    // Convierte de la BD al Dominio (usando tu método restore estático)
    public Payment toDomain(PaymentDocument doc) {
        // Asegúrate de tener la clase Money disponible
        // Money money = new Money(doc.getAmount(), doc.getCurrency());
        return Payment.restore(
                doc.getId(),
                new Money(doc.getAmount(), doc.getCurrency()),
                PaymentStatus.valueOf(doc.getStatus())
        );
    }
}