package com.example.backend.payload.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa la solicitud de registro de un nuevo usuario.
 * Se utiliza para recibir todos los datos necesarios durante el proceso de creación de cuenta.
 */
@Getter
@Setter
public class SignupRequest {

    /**
     * Nombre de usuario único que identificará al usuario en el sistema.
     */
    private String nombreUsuario;

    /**
     * Contraseña que el usuario utilizará para autenticarse.
     */
    private String password;

    /**
     * Nombre real del usuario.
     */
    private String nombre;

    /**
     * Apellidos del usuario.
     */
    private String apellidos;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Número de teléfono del usuario.
     */
    private String telefono;

    /**
     * Documento Nacional de Identidad del usuario.
     */
    private String dni;

    /**
     * Rol asignado al usuario (por ejemplo: ADMIN, EMPLEADO, CLIENTE).
     */
    private String rol;
}
