package com.portability.addresses_service.dto;

import com.portability.addresses_service.enm.PackageType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for Envia Packages")
public class EnviaPackages {

    @Schema(description = "Shipment type", example = "envelope")
    @Builder.Default
    private PackageType type = PackageType.ENVELOPE;

    @Schema(description = "Length unit", example = "CM")
    @Builder.Default
    private String lengthUnit = "CM";

    @Schema(description = "Weight", example = "0.01")
    @Builder.Default
    private double weight = 0.01;

    @Schema(description = "Weight unit", example = "KG")
    @Builder.Default
    private String weightUnit = "KG";

    @Schema(description = "Dimensions of the package")
    private EnviaDimensions dimensions;

    @Schema(description = "Declared value of the package", example = "100.00")
    @Builder.Default
    private Number declareValue = 100.00;

    @Schema(description = "Amount of packages", example = "1")
    @Builder.Default
    private int amount = 1;

    @Schema(description = "Content description of the package", example = "Documents")
    @Builder.Default
    private String content = "Sim Card";
}
