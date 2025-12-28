package com.mayik.payments.infrastructure.input.rest.controller;


import com.mayik.payments.application.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest; // Nuevo paquete en 4.0
import org.springframework.test.context.bean.override.mockito.MockitoBean; // API de 2025
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // API estándar en Spring Boot 4.x
    private PaymentService paymentService;

    @Test
    void debeRetornarAcceptedAlProcesarPago() throws Exception {
        UUID paymentId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/payments/" + paymentId + "/process")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        verify(paymentService, times(1)).process(paymentId);
    }
}