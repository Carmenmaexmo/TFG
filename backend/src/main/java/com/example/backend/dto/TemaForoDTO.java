package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemaForoDTO {
    private Long id;
    private String titulo;
    private String contenido;
    private UsuarioDTO usuario;
    private ForoDTO foro;
}
