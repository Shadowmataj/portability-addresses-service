package com.portability.addresses_service.model;

import java.time.LocalDateTime;

import com.portability.addresses_service.enm.AddressType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "Street cannot be empty")
    @Size(max = 200, message = "Street cannot exceed 200 characters")
    @Column(nullable = false, length = 200)
    private String street;

    @Column(length = 20)
    @Size(max = 20, message = "Number cannot exceed 20 characters")
    private String number;

    @Size(max = 100, message = "District cannot exceed 100 characters")
    @Column(length = 100)
    private String district;

    @NotBlank(message = "City cannot be empty")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State/province cannot be empty")
    @Size(max = 100, message = "State/province cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String state;

    @NotBlank(message = "Postal code cannot be empty")
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @NotBlank(message = "Country cannot be empty")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String country;

    @Column(length = 500)
    private String reference;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
