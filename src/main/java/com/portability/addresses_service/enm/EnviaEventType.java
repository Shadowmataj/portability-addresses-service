package com.portability.addresses_service.enm;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EnviaEventType {
    SHIPMENT_CREATED("shipment_created"),
    SHIPMENT_DELIVERED("shipment_delivered"),
    SHIPMENT_EXCEPTION("shipment_exception");

    private final String value;

    EnviaEventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
