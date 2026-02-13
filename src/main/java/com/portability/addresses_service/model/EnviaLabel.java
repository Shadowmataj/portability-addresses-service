package com.portability.addresses_service.model;

import java.util.List;

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
@Table(name = "envia_labels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnviaLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Order ID cannot be empty")
    @Column(unique = true, nullable = false)
    private String orderId;

    private String carrier;

    private String service;

    private Integer shipmentId;

    private String trackingNumber;

    private String trackUrl;

    private String label;

    private List<String> additionalFiles;

    private Double totalPrice;

    private String currentBalance;

    private String currency;

}
