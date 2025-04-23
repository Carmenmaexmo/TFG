package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemaForoCreateDTO {
    private String titulo;
    private String descripcion;
    private Long idUsuario;
    private Long idForo;
}
