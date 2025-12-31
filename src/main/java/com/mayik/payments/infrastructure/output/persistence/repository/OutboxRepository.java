package com.mayik.payments.infrastructure.output.persistence.repository;

import com.mayik.payments.infrastructure.output.persistence.entity.OutboxEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OutboxRepository extends MongoRepository<OutboxEvent, String> {
    List<OutboxEvent> findByProcessedFalse();
}