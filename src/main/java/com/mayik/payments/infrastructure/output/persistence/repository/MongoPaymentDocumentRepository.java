package com.mayik.payments.infrastructure.output.persistence.repository;

import com.mayik.payments.infrastructure.output.persistence.entity.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MongoPaymentDocumentRepository extends MongoRepository<PaymentDocument, UUID> {
    // Spring te da los métodos CRUD para PaymentDocument
}
