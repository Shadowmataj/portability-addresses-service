package com.portability.addresses_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portability.addresses_service.model.EnviaCancelLabel;

@Repository
public interface EnviaCancelLabelRepository extends JpaRepository<EnviaCancelLabel, Long> {

    Optional<EnviaCancelLabel> findByOrderId(String orderId);

}
