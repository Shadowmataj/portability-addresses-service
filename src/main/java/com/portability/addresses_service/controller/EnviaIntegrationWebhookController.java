package com.portability.addresses_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portability.addresses_service.dto.EnviaWebhookRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/envia-integration/webhook")
public class EnviaIntegrationWebhookController {

    @PostMapping("/tracking-update")
    public void handleTrackingUpdate(
            @Valid @RequestBody EnviaWebhookRequest trackingRequest) {

        System.out.println("Received tracking update for tracking number: " + trackingRequest.trackingNumber()
                + " with event type: " + trackingRequest.eventType());
    }

}
