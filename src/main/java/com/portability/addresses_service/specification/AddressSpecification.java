package com.portability.addresses_service.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.portability.addresses_service.dto.AddressFilterRequest;
import com.portability.addresses_service.model.Address;

import jakarta.persistence.criteria.Predicate;

public class AddressSpecification {

    public static Specification<Address> filterBy(AddressFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search filter (searches in street, city, state, or country)
            if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
                String searchPattern = "%" + filter.getSearch().toLowerCase() + "%";
                Predicate streetPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("street")), searchPattern);
                Predicate cityPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city")), searchPattern);
                Predicate statePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("state")), searchPattern);
                Predicate countryPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("country")), searchPattern);
                Predicate postalCodePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("postalCode")), searchPattern);
                predicates.add(criteriaBuilder.or(streetPredicate, cityPredicate, statePredicate, countryPredicate, postalCodePredicate));
            }

            // Street filter
            if (filter.getStreet() != null && !filter.getStreet().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("street")),
                        "%" + filter.getStreet().toLowerCase() + "%"));
            }

            // City filter
            if (filter.getCity() != null && !filter.getCity().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("city")),
                        "%" + filter.getCity().toLowerCase() + "%"));
            }

            // State filter
            if (filter.getState() != null && !filter.getState().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("state")),
                        "%" + filter.getState().toLowerCase() + "%"));
            }

            // Postal code filter
            if (filter.getPostalCode() != null && !filter.getPostalCode().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("postalCode")),
                        "%" + filter.getPostalCode().toLowerCase() + "%"));
            }

            // Country filter
            if (filter.getCountry() != null && !filter.getCountry().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("country")),
                        "%" + filter.getCountry().toLowerCase() + "%"));
            }

            // Customer ID filter
            if (filter.getCustomerId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("customerId"), filter.getCustomerId()));
            }

            // Address Type filter
            if (filter.getAddressType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("addressType"), filter.getAddressType()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
