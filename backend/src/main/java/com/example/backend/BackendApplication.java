package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que arranca la aplicación Spring Boot.
 * Es el punto de entrada del backend de la aplicación de gestión de vinilos.
 */
@SpringBootApplication // Anotación que configura automáticamente la aplicación como un proyecto Spring Boot
public class BackendApplication {

    /**
     * Método principal que inicia la ejecución del backend.
     * @param args argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
