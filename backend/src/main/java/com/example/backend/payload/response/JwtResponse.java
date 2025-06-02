package com.example.backend.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {
    private String token;
    private String nombreUsuario;
    private Long idUsuario;
    private List<String> roles;    // ← nuevo campo

    public JwtResponse(String token, String nombreUsuario, List<String> roles, Long idUsuario) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.idUsuario = idUsuario;
        this.roles = roles;        // ← asignación
    }
}
