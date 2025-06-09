package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la actualización de los datos de un proveedor.
 * Permite modificar el nombre, correo, teléfono y dirección del proveedor.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorUpdateDTO {

    /** Nuevo nombre del proveedor */
    private String nombre;

    /** Nuevo correo electrónico del proveedor */
    private String email;

    /** Nuevo teléfono de contacto del proveedor */
    private String telefono;

    /** Nueva dirección física del proveedor */
    private String direccion;
}
