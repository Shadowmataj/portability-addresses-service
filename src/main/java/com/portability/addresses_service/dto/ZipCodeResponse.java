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
@Schema(description = "Response for Zip Code Validation")
public class ZipCodeResponse {

    @Schema(description = "Indicates if the zip code is valid", example = "true")
    private boolean valid;

    @Schema(description = "Country code associated with the zip code", example = "US")
    private String countryCode;

    @Schema(description = "State associated with the zip code", example = "CA")
    private String state;

    @Schema(description = "City associated with the zip code", example = "Los Angeles")
    private String city;
}
