package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioCreateDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String telefono;
    private String dni;
}
