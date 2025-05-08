package com.example.backend.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponse {
    private String token;
    private String nombreUsuario;
    private List<String> roles;    // ← nuevo campo

    public JwtResponse(String token, String nombreUsuario, List<String> roles) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.roles = roles;        // ← asignación
    }
}
