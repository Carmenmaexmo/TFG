package com.example.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioForoDTO {
    private Long id;
    private String contenido;
    private LocalDateTime fechaComentario;
    private UsuarioDTO usuario;
    private TemaForoDTO tema;
    private ComentarioForoDTO comentarioPadre;
}
