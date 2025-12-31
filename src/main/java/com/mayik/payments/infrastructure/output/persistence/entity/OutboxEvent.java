package com.mayik.payments.infrastructure.output.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "outbox_events")
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class OutboxEvent {
    @Id
    private String id;
    private String aggregateId;
    private String type;
    private String payload;
    private LocalDateTime createdAt;
    private boolean processed;
}