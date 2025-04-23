package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
