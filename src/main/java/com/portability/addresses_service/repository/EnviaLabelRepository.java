package com.portability.addresses_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portability.addresses_service.model.EnviaLabel;

@Repository
public interface EnviaLabelRepository extends JpaRepository<EnviaLabel, Long> {
    Optional<EnviaLabel> findByOrderId(String orderId);
}
