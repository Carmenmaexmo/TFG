package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de un nuevo usuario en el sistema.
 * Incluye todos los campos necesarios para registrar un usuario, 
 * incluyendo datos personales, de contacto y su carrito inicial.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioCreateDTO {

    /** Nombre de usuario utilizado para autenticación */
    private String nombreUsuario;

    /** Nombre real del usuario */
    private String nombre;

    /** Apellidos del usuario */
    private String apellidos;

    /** Correo electrónico del usuario */
    private String email;

    /** Contraseña del usuario (en texto plano, se cifrará al guardar) */
    private String password;

    /** Teléfono de contacto del usuario */
    private String telefono;

    /** Rol asignado al usuario (ej: CLIENTE, ADMINISTRADOR) */
    private String rol;

    /** Documento nacional de identidad del usuario */
    private String dni;

    /** Carrito de compra del usuario representado como cadena JSON */
    private String carrito;
}
