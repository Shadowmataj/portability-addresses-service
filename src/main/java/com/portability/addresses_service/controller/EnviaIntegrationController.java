package com.portability.addresses_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portability.addresses_service.dto.CarriersOptionsRequest;
import com.portability.addresses_service.dto.CreateLabelRequest;
import com.portability.addresses_service.dto.EnviaQuoteResponse;
import com.portability.addresses_service.dto.EnviaTrackingResponse;
import com.portability.addresses_service.dto.ZipCodeRequest;
import com.portability.addresses_service.dto.ZipCodeResponse;
import com.portability.addresses_service.model.EnviaCancelLabel;
import com.portability.addresses_service.model.EnviaLabel;
import com.portability.addresses_service.service.EnviaIntegrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/envia-integration")
@Tag(name = "Envia Integration", description = "API for Envia integration services")
public class EnviaIntegrationController {

    @Autowired
    private EnviaIntegrationService enviaIntegrationService;

    @Operation(summary = "Validate the address", description = "Validates the address by zip code using Envia's address validation service")
    @ApiResponse(responseCode = "200", description = "Address validated successfully")
    @PostMapping("validate-address")
    public ResponseEntity<ZipCodeResponse> validateAddress(@Valid @RequestBody ZipCodeRequest zipCode) {
        return ResponseEntity.ok(enviaIntegrationService.validateAddress(zipCode));
    }

    @Operation(summary = "Get the carriers options for a shipment", description = "Validates the address by zip code using Envia's address validation service")
    @ApiResponse(responseCode = "200", description = "Address validated successfully")
    @PostMapping("carriers-options")
    public ResponseEntity<List<EnviaQuoteResponse>> getShipmentOptions(
            @Valid @RequestBody CarriersOptionsRequest request) {
        return ResponseEntity.ok(enviaIntegrationService.getShipmentOptions(request));
    }

    @Operation(summary = "Create a label", description = "Creates a shipping label using Envia's label creation service")
    @ApiResponse(responseCode = "200", description = "Label created successfully")
    @PostMapping("create-label/{orderId}")
    public ResponseEntity<EnviaLabel> createShipmentLabel(
            @PathVariable String orderId,
            @Valid @RequestBody CreateLabelRequest request) {
        return ResponseEntity.ok(enviaIntegrationService.createShipmentLabel(orderId, request));
    }

    @Operation(summary = "Cancel a label", description = "Cancels a shipping label using Envia's label cancellation service")
    @ApiResponse(responseCode = "200", description = "Label cancelled successfully")
    @PostMapping("cancel-label/{orderId}")
    public ResponseEntity<EnviaCancelLabel> cancelShipmentLabel(
            @PathVariable String orderId) {
        return ResponseEntity.ok(enviaIntegrationService.cancelShipmentLabel(orderId));
    }

    @Operation(summary = "get general track", description = "Gets the general track information for a shipment using Envia's general track service")
    @ApiResponse(responseCode = "200", description = "General track information retrieved successfully")
    @PostMapping("general-track/{orderId}")
    public ResponseEntity<EnviaTrackingResponse> getGeneralTrack(
            @PathVariable String orderId) {
        return ResponseEntity.ok(enviaIntegrationService.getGeneralTrack(orderId));
    }

    @Operation(summary = "Get label by order id", description = "Retrieves an EnviaLabel by order id")
    @ApiResponse(responseCode = "200", description = "Label retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Label not found")
    @GetMapping("label/{orderId}")
    public ResponseEntity<EnviaLabel> getLabelByOrderId(
            @PathVariable String orderId) {
        return ResponseEntity.ok(enviaIntegrationService.getLabelByOrderId(orderId));
    }

}
