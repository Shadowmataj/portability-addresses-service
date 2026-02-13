package com.portability.addresses_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.portability.addresses_service.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    
    List<Address> findByCity(String city);
    
    List<Address> findByCountry(String country);
    
    Optional<List<Address>> findByCustomerId(Long customerId);
}
