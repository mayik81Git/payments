package com.mayik.payments.infrastructure.input.rest.controller;

import com.mayik.payments.application.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments API")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{id}/process")
    @Operation(summary = "Procesar pago mediante ID")
    public ResponseEntity<Void> process(@PathVariable java.util.UUID id) {
        paymentService.process(id);
        return ResponseEntity.accepted().build();
    }
}
