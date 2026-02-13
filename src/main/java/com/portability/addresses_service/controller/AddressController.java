package com.portability.addresses_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portability.addresses_service.dto.AddressFilterRequest;
import com.portability.addresses_service.dto.AddressRequest;
import com.portability.addresses_service.dto.AddressResponse;
import com.portability.addresses_service.dto.PagedResponse;
import com.portability.addresses_service.enm.AddressType;
import com.portability.addresses_service.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Addresses", description = "API for customer address management")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Operation(summary = "Get all addresses", description = "Retrieves a paginated list of all addresses with optional filters")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    @GetMapping
    public ResponseEntity<PagedResponse<AddressResponse>> getAllAddresses(
            @Parameter(description = "Filter by address type")
            @RequestParam(required = false) AddressType addressType,
            @Parameter(description = "Search term for street, city, state, or country")
            @RequestParam(required = false) String search,
            @Parameter(description = "Filter by street")
            @RequestParam(required = false) String street,
            @Parameter(description = "Filter by city")
            @RequestParam(required = false) String city,
            @Parameter(description = "Filter by state")
            @RequestParam(required = false) String state,
            @Parameter(description = "Filter by postal code")
            @RequestParam(required = false) String postalCode,
            @Parameter(description = "Filter by country")
            @RequestParam(required = false) String country,
            @Parameter(description = "Filter by customer ID")
            @RequestParam(required = false) Long customerId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "createdAt")
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "desc")
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {

        AddressFilterRequest filter = AddressFilterRequest.builder()
                .addressType(addressType)
                .search(search)
                .street(street)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .customerId(customerId)
                .build();

        // Create sort object
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponse<AddressResponse> addresses = addressService.getAllAddresses(filter, pageable);

        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Get address by ID", description = "Returns a specific address by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "404", description = "Address not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(
            @Parameter(description = "Address ID") @PathVariable Long id) {
        AddressResponse address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @Operation(summary = "Get address by Customer ID", description = "Returns a specific address by its Customer ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "404", description = "Address not found", content = @Content)
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AddressResponse>> getAddressesByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        List<AddressResponse> addresses = addressService.getAddressesByCustomerId(customerId);
        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Create new address", description = "Creates a new address for a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Address successfully created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse createdAddress = addressService.createAddress(addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @Operation(summary = "Update address", description = "Updates an existing address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address successfully updated",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "404", description = "Address not found", content = @Content),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @Parameter(description = "Address ID") @PathVariable Long id,
            @Valid @RequestBody AddressRequest addressRequest) {
        AddressResponse updatedAddress = addressService.updateAddress(id, addressRequest);
        return ResponseEntity.ok(updatedAddress);
    }

    @Operation(summary = "Delete address", description = "Deletes an address by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Address successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Address not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "Address ID") @PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
