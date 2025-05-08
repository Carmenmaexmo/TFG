package com.example.backend.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequest {
    private String nombreUsuario;
    private String password;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String dni;
    private String rol;
}
