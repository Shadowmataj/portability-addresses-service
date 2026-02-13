package com.portability.addresses_service.enm;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PackageType {
    ENVELOPE("envelope"),
    BOX("box"),
    PALLET("pallet"),
    FULL_TRUCK_LOAD("full_truck_load");

    private final String value;

    PackageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}