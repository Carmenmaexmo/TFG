package com.example.backend.dto;

import lombok.*;

/**
 * DTO que representa un proveedor registrado en el sistema.
 * Contiene información de contacto y localización del proveedor.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorDTO {

    /** ID único del proveedor */
    private Long id;

    /** Nombre del proveedor */
    private String nombre;

    /** Correo electrónico del proveedor */
    private String email;

    /** Teléfono de contacto del proveedor */
    private String telefono;

    /** Dirección física del proveedor */
    private String direccion;
}
