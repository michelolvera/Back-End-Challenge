package dev.michel.registryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * En este microservicio habilitamos eureka server, nos ayudara a mantener un registro de los servicios en ejecución.
 */

@SpringBootApplication
@EnableEurekaServer
public class RegistryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistryServiceApplication.class, args);
	}

}
