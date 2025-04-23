package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForoCreateDTO {
    private String nombre;
    private String descripcion;
}
