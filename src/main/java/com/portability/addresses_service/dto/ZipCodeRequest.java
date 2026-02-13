package com.portability.addresses_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for zip code")
public class ZipCodeRequest {

    @Schema(description = "Zip code to be validated", example = "12345", requiredMode = RequiredMode.REQUIRED)
    private String zipCode;
}
