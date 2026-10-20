package com.supermercado.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion.
 *
 * @SpringBootApplication escanea el paquete donde esta esta clase (y todos los
 * subpaquetes: controller, service, repository, entity...) buscando clases
 * anotadas con @RestController, @Service, @Repository y @Entity.
 */
@SpringBootApplication
public class SupermercadoApplication {
        public static void main(String[] args) {
        SpringApplication.run(SupermercadoApplication.class, args);
    }
}
