package com.portability.addresses_service.dto;

import com.portability.addresses_service.enm.ShipmentType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for Shipment details")
public class EnviaCreateLabelShipment {

    @Schema(description = "Type of the shipment")
    @Builder.Default
    private ShipmentType shipmentType = ShipmentType.PARCEL_SERVICE;

    @Schema(description = "Carrier for the shipment")
    private String carrier;

    @Schema(description = "Service level for the shipment")
    private String service;
}
