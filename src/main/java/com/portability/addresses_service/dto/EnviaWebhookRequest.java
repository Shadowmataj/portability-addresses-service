package com.portability.addresses_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.portability.addresses_service.enm.EnviaEventType;

public record EnviaWebhookRequest(
        @JsonProperty("tracking_number")
        String trackingNumber,
        @JsonProperty("event_type")
        EnviaEventType eventType
        ) {

}
