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
public class EnviaDeliveryDate {

    @Schema(description = "Estimated delivery date in format YYYY-MM-DD", example = "2026-02-06")
    private String date;

    @Schema(description = "Difference in days from the shipment date", example = "7")
    private Integer dateDifference;

    @Schema(description = "Time unit for delivery", example = "days")
    private String timeUnit;

    @Schema(description = "Time of delivery", example = "20:00")
    private String time;
}
