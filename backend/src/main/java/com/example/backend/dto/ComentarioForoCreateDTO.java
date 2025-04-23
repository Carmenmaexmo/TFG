package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioForoCreateDTO {
    private String contenido;
    private Long idUsuario;
    private Long idTema;
}
