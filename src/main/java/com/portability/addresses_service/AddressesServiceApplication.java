package com.portability.addresses_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableFeignClients
@EnableAspectJAutoProxy
public class AddressesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressesServiceApplication.class, args);
	}

}
