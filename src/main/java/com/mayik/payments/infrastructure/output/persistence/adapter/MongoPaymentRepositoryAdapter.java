package com.mayik.payments.infrastructure.output.persistence.adapter;

import com.mayik.payments.domain.model.Payment;
import com.mayik.payments.domain.ports.out.PaymentRepository;
import com.mayik.payments.infrastructure.output.persistence.repository.MongoPaymentDocumentRepository;
import com.mayik.payments.infrastructure.output.persistence.entity.PaymentDocument;
import com.mayik.payments.infrastructure.output.persistence.mapper.PaymentDocumentMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class MongoPaymentRepositoryAdapter implements PaymentRepository {

    private final MongoPaymentDocumentRepository mongoRepo;
    private final PaymentDocumentMapper mapper;

    public MongoPaymentRepositoryAdapter(MongoPaymentDocumentRepository mongoRepo, PaymentDocumentMapper mapper) {
        this.mongoRepo = mongoRepo;
        this.mapper = mapper;
    }

    @Override
    @CircuitBreaker(name = "mongodbAdapter")
    @Retry(name = "mongodbAdapter")          // <--- ESTO activa los 3 reintentos
    public void save(Payment payment) {
        // 1. Convertimos el objeto de Dominio (Payment) al Documento de MongoDB
        PaymentDocument doc = mapper.toDocument(payment);
        // 2. Guardamos usando el repositorio de Spring Data
        mongoRepo.save(doc);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return mongoRepo.findById(id).map(mapper::toDomain);
    }
}
