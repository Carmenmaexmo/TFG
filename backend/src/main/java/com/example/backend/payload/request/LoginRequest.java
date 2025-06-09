package com.example.backend.payload.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa la solicitud de inicio de sesión.
 * Se utiliza para recibir las credenciales del usuario en el proceso de autenticación.
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * Nombre de usuario del usuario que desea iniciar sesión.
     */
    private String nombreUsuario;

    /**
     * Contraseña asociada al usuario.
     */
    private String password;
}
