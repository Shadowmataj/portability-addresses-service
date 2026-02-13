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
@Schema(description = "Response DTO for Envia Quote")
public class EnviaQuoteResponse {

    @Schema(description = "Carrier name")
    private String carrier;

    @Schema(description = "Carrier description")
    private String carrierDescription;

    @Schema(description = "Service name")
    private String service;

    @Schema(description = "Service description")
    private String serviceDescription;

    @Schema(description = "Delivery estimate")
    private String deliveryEstimate;

    @Schema(description = "Delivery date")
    private EnviaDeliveryDate deliveryDate;

    @Schema(description = "Total price of the quote")
    private Float totalPrice;

    @Schema(description = "Base price of the quote")
    private Float basePrice;

    @Schema(description = "Taxes applied to the quote")
    private Float taxes;
}
