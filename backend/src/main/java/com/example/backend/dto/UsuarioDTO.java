package com.example.backend.dto;

import lombok.*;

/**
 * DTO que representa un usuario registrado en el sistema.
 * Contiene todos los datos personales, de contacto, rol y su carrito asociado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    /** ID único del usuario */
    private Long idUsuario;

    /** Nombre de usuario utilizado para autenticación */
    private String nombreUsuario;

    /** Nombre real del usuario */
    private String nombre;

    /** Apellidos del usuario */
    private String apellidos;

    /** Correo electrónico del usuario */
    private String email;

    /** Teléfono de contacto del usuario */
    private String telefono;

    /** Documento nacional de identidad del usuario */
    private String dni;

    /** Rol asignado al usuario (CLIENTE, ADMINISTRADOR, etc.) */
    private String rol;

    /** Carrito del usuario representado como cadena JSON */
    private String carrito;
}
