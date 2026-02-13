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
@Schema(description = "DTO for Envia Address")
public class EnviaAddress {

    @Schema(description = "Name of the person at the address", example = "John Doe")
    @Builder.Default
    private String name = "John Doe";

    @Schema(description = "Company name at the address", example = "portabilidad")
    @Builder.Default
    private String company = "";

    @Schema(description = "Email of the person at the address", example = "test@gmail.com")
    @Builder.Default
    private String email = "test@gmail.com";

    @Schema(description = "Phone number of the person at the address", example = "5566778899")
    @Builder.Default
    private String phone = "5566778899";

    @Schema(description = "Street name of the address", example = "Test street")
    @Builder.Default
    private String street = "Test street";

    @Schema(description = "Number of the address", example = "101")
    @Builder.Default
    private String number = "101";

    @Schema(description = "District of the address", example = "District 1")
    @Builder.Default
    private String district = "District 1";

    @Schema(description = "City of the address", example = "Test city")
    @Builder.Default
    private String city = "Test city";

    @Schema(description = "State of the address", example = "CMX")
    @Builder.Default
    private String state = "CMX";

    @Schema(description = "Country of the address", example = "MX")
    @Builder.Default
    private String country = "MX";

    @Schema(description = "Postal code of the address", example = "11111")
    @Builder.Default
    private String postalCode = "11111";

    @Schema(description = "Reference or additional information for the address", example = "Cerca de la tienda")
    @Builder.Default
    private String reference = "Near the store";

    @Schema(description = "Type of the address", example = "origin")
    @Builder.Default
    private String type = "origin";
}
