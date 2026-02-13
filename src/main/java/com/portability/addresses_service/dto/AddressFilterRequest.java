package com.portability.addresses_service.dto;

import com.portability.addresses_service.enm.AddressType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressFilterRequest {

    private AddressType addressType;
    private String search;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Long customerId;
}
