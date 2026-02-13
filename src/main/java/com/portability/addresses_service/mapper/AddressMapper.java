package com.portability.addresses_service.mapper;

import com.portability.addresses_service.dto.EnviaAddress;
import com.portability.addresses_service.model.Address;

public class AddressMapper {
    public static EnviaAddress toEnviaAddress(Address address) {
        EnviaAddress enviaAddress = new EnviaAddress();
        enviaAddress.setStreet(address.getStreet());
        enviaAddress.setNumber(address.getNumber());
        enviaAddress.setDistrict(address.getDistrict());
        enviaAddress.setCity(address.getCity());
        enviaAddress.setState(address.getState());
        enviaAddress.setPostalCode(address.getPostalCode());
        enviaAddress.setCountry(address.getCountry());
        enviaAddress.setReference(address.getReference());
        return enviaAddress;
    }
}
