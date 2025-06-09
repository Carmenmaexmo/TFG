package com.example.backend.payload.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase de respuesta utilizada tras un proceso de autenticación exitoso.
 * Contiene la información que se devuelve al cliente junto con el JWT (token).
 */
@Getter @Setter
public class JwtResponse {

    /**
     * Token JWT generado para el usuario autenticado.
     */
    private String token;

    /**
     * Nombre de usuario utilizado en la autenticación.
     */
    private String nombreUsuario;

    /**
     * Identificador único del usuario autenticado.
     */
    private Long idUsuario;

    /**
     * Lista de roles asignados al usuario autenticado.
     */
    private List<String> roles;

    /**
     * Constructor para inicializar la respuesta con todos los campos necesarios.
     *
     * @param token Token JWT generado.
     * @param nombreUsuario Nombre de usuario autenticado.
     * @param roles Lista de roles del usuario.
     * @param idUsuario Identificador del usuario.
     */
    public JwtResponse(String token, String nombreUsuario, List<String> roles, Long idUsuario) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        this.roles = roles;
    }
}
