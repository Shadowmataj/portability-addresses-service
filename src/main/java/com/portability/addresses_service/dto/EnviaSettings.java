package com.portability.addresses_service.dto;

import com.portability.addresses_service.enm.PrintFormat;
import com.portability.addresses_service.enm.PrintSize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Envia Settings DTO")
public class EnviaSettings {

    @Schema(description = "Print format for the label", example = "PDF")
    @Builder.Default
    private PrintFormat printFormat = PrintFormat.PDF;

    @Schema(description = "Print size for the label", example = "STOCK_4X6")
    @Builder.Default
    private PrintSize printSize = PrintSize.STOCK_4X6;
}
