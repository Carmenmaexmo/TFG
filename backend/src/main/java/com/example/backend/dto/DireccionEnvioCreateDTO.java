package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionEnvioCreateDTO {
    private Long idUsuario; // para asociarla al usuario
    private String ciudad;
    private String direccion;
    private String telefono;
    private String codigoPostal;
}
