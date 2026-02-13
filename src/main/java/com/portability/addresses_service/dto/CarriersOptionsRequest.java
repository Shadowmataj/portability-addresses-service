package com.portability.addresses_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for Carriers Options")
public class CarriersOptionsRequest {

    @Schema(description = "Origin address details")
    private EnviaAddress origin;

    @Schema(description = "Destination address details")
    private EnviaAddress destination;
}
