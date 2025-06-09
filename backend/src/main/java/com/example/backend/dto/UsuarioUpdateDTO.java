package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la actualización de los datos de un usuario.
 * Permite modificar tanto información personal como de autenticación,
 * rol y carrito del usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioUpdateDTO {

    /** Nombre de usuario (puede ser modificado si se permite) */
    private String nombreUsuario;

    /** Nombre real del usuario */
    private String nombre;

    /** Apellidos del usuario */
    private String apellidos;

    /** Correo electrónico del usuario */
    private String email;

    /** Nueva contraseña del usuario (si aplica) */
    private String password;

    /** Teléfono de contacto del usuario */
    private String telefono;

    /** Documento nacional de identidad */
    private String dni;

    /** Rol actualizado del usuario (CLIENTE, ADMINISTRADOR, etc.) */
    private String rol;

    /** Carrito representado como JSON (productos en el carrito) */
    private String carrito;
}
