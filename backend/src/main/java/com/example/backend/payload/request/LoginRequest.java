package com.example.backend.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String nombreUsuario;
    private String password;
}
