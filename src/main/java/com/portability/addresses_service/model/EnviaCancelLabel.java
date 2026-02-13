package com.portability.addresses_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "envia_canceled_labels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnviaCancelLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Order ID cannot be empty")
    @Column(unique = true, nullable = false)
    private String orderId;

    private String carrier;

    private String service;

    private String trackingNumber;

    private String folio;

    private boolean balanceReturned;

    private LocalDateTime balanceReturnedDate;
}
