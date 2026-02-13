package com.portability.addresses_service.enm;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PrintFormat {
    PDF("PDF"),
    PNG("PNG"),
    ZPLII("ZPLII");

    private final String value;

    PrintFormat(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
