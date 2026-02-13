package com.portability.addresses_service.enm;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PrintSize {
    STOCK_4X6("STOCK_4X6"),
    PAPER_7X4("PAPER_7X4.75");

    private final String value;

    PrintSize(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
