// src/main/java/com/example/backend/dto/ComentarioForoUpdateDTO.java
package com.example.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioForoUpdateDTO {
    private String contenido;
    private LocalDateTime fechaComentario;
    private Long idUsuario;
    private Long idTema;
    private Long idComentarioPadre;
}
