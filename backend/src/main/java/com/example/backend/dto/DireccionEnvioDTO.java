package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionEnvioDTO {
    private Long id;
    private String ciudad;
    private String codigoPostal;
    private String direccion;
    private String telefono;
}
