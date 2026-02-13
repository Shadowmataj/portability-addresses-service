package com.portability.addresses_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "envia.api")
public class EnviaConfig {

    private String token;
    private String urlAddressValidation;
    private String urlGetCarriers;
    private String urlShip;
}
