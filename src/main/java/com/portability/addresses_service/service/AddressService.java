package com.portability.addresses_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portability.addresses_service.dto.AddressFilterRequest;
import com.portability.addresses_service.dto.AddressRequest;
import com.portability.addresses_service.dto.AddressResponse;
import com.portability.addresses_service.dto.PagedResponse;
import com.portability.addresses_service.dto.ZipCodeRequest;
import com.portability.addresses_service.dto.ZipCodeResponse;
import com.portability.addresses_service.exception.InvalidAddressException;
import com.portability.addresses_service.exception.ResourceNotFoundException;
import com.portability.addresses_service.model.Address;
import com.portability.addresses_service.repository.AddressRepository;
import com.portability.addresses_service.specification.AddressSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddressService {
    
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EnviaIntegrationService enviaIntegrationService;

    @Transactional(readOnly = true)
    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<AddressResponse> getAllAddresses(AddressFilterRequest filter, Pageable pageable) {
        Specification<Address> spec = AddressSpecification.filterBy(filter);
        Page<Address> addressPage = addressRepository.findAll(spec, pageable);

        List<AddressResponse> addressResponses = addressPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<AddressResponse>builder()
                .content(addressResponses)
                .pageNumber(addressPage.getNumber())
                .pageSize(addressPage.getSize())
                .totalElements(addressPage.getTotalElements())
                .totalPages(addressPage.getTotalPages())
                .last(addressPage.isLast())
                .first(addressPage.isFirst())
                .empty(addressPage.isEmpty())
                .build();
    }

    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));
        return convertToResponse(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressesByCustomerId(Long customerId) {
        List<Address> addresses = addressRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Address not found with Customer ID: " + customerId));
        
        return addresses.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse createAddress(AddressRequest addressRequest) {
        
        ZipCodeRequest zipCodeRequest = ZipCodeRequest.builder()
            .zipCode(addressRequest.getPostalCode())
            .build();

        ZipCodeResponse validationResponse = enviaIntegrationService.validateAddress(zipCodeRequest);

        if (!validationResponse.isValid()) {
            throw new InvalidAddressException("Invalid address for the provided zip code");
        }

        Address address = convertToEntity(addressRequest, validationResponse);
        Address savedAddress = addressRepository.save(address);
        return convertToResponse(savedAddress);
    }

    @Transactional
    public AddressResponse updateAddress(Long id, AddressRequest addressRequest) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with ID: " + id));

        existingAddress.setCustomerId(addressRequest.getCustomerId());
        existingAddress.setStreet(addressRequest.getStreet());
        existingAddress.setReference(addressRequest.getReference());
        existingAddress.setDistrict(addressRequest.getDistrict());
        existingAddress.setNumber(addressRequest.getNumber());
        
        if(!existingAddress.getPostalCode().equals(addressRequest.getPostalCode())){
            ZipCodeRequest zipCodeRequest = ZipCodeRequest.builder()
                .zipCode(addressRequest.getPostalCode())
                .build();

            ZipCodeResponse validationResponse = enviaIntegrationService.validateAddress(zipCodeRequest);

            if (!validationResponse.isValid()) {
                throw new InvalidAddressException("Invalid address for the provided zip code");
            }
            existingAddress.setPostalCode(addressRequest.getPostalCode());
            existingAddress.setCountry(validationResponse.getCountryCode());
            existingAddress.setCity(validationResponse.getCity());
            existingAddress.setState(validationResponse.getState());
        }
        

        Address updatedAddress = addressRepository.save(existingAddress);
        return convertToResponse(updatedAddress);
    }

    @Transactional
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address not found with ID: " + id);
        }
        addressRepository.deleteById(id);
    }

    private AddressResponse convertToResponse(Address address) {
        AddressResponse dto = new AddressResponse();
        dto.setId(address.getId());
        dto.setCustomerId(address.getCustomerId());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setDistrict(address.getDistrict());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setReference(address.getReference());
        dto.setAddressType(address.getAddressType());
        return dto;
    }

    private Address convertToEntity(AddressRequest dto, ZipCodeResponse validationResponse) {
        Address address = new Address();
        address.setCustomerId(dto.getCustomerId());
        address.setStreet(dto.getStreet());
        address.setDistrict(dto.getDistrict());
        address.setNumber(dto.getNumber());
        address.setCity(validationResponse.getCity());
        address.setState(validationResponse.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(validationResponse.getCountryCode());
        address.setReference(dto.getReference());
        address.setAddressType(dto.getAddressType());
        return address;
    }
}
