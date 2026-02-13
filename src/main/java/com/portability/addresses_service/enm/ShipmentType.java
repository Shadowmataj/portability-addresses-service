package com.portability.addresses_service.enm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ShipmentType {
    PARCEL_SERVICE(1),
    LTL(2),
    FTL(3);

    private final int value;

    ShipmentType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static ShipmentType fromValue(int value) {
        for (ShipmentType p : values()) {
            if (p.value == value) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid priority value: " + value);
    }
}
