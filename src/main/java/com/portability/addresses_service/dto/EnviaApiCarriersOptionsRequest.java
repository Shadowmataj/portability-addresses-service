package com.portability.addresses_service.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for Envia")
public class EnviaApiCarriersOptionsRequest {

    @Schema(description = "Origin address details")
    private EnviaAddress origin;

    @Schema(description = "Destination address details")
    private EnviaAddress destination;

    @Schema(description = "Shipment details")
    private List<EnviaPackages> packages;

    @Schema(description = "Shipment information")
    private EnviaCarriersOptionsShipment shipment;
}
