package com.portability.addresses_service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8084}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("Local development server");

        Contact contact = new Contact();
        contact.setEmail("info@portability.com");
        contact.setName("Portability Team");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("Address Management API")
                .version("1.0.0")
                .description("Microservice for managing customer home addresses. " +
                        "Provides complete CRUD operations for handling address information.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
