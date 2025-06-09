package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de un nuevo proveedor.
 * Contiene los datos básicos necesarios para registrar un proveedor en el sistema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorCreateDTO {

    /** Nombre del proveedor */
    private String nombre;

    /** Dirección de correo electrónico del proveedor */
    private String email;

    /** Teléfono de contacto del proveedor */
    private String telefono;

    /** Dirección física del proveedor */
    private String direccion;
}
