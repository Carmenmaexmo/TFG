package com.example.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO para representar un comentario dentro de un tema del foro.
 * Incluye el contenido, la fecha, el usuario que lo hizo,
 * el tema al que pertenece y el comentario padre si es una respuesta.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioForoDTO {

    /** ID único del comentario */
    private Long id;

    /** Contenido textual del comentario */
    private String contenido;

    /** Fecha y hora en que se publicó el comentario */
    private LocalDateTime fechaComentario;

    /** Usuario que realizó el comentario */
    private UsuarioDTO usuario;

    /** Tema del foro al que pertenece el comentario */
    private TemaForoDTO tema;

    /** Comentario al que responde este comentario (puede ser null) */
    private ComentarioForoDTO comentarioPadre;
}
