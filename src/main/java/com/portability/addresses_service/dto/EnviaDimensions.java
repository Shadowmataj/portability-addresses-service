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
@Schema(description = "DTO for Dimensions")
public class EnviaDimensions {

    @Schema(description = "Width of the package", example = "10")
    @Builder.Default
    private Number width = 1;

    @Schema(description = "Height of the package", example = "5")
    @Builder.Default
    private Number height = 15;

    @Schema(description = "Length of the package", example = "15")
    @Builder.Default
    private Number length = 15;
}
