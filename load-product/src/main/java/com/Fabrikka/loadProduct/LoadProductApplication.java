package com.Fabrikka.loadProduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoadProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadProductApplication.class, args);
	}

}
