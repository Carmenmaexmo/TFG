package com.example.backend.dto;

import java.time.LocalDateTime;

import lombok.*;

/**
 * DTO para la creación de un comentario en un tema del foro.
 * Contiene la información necesaria para registrar un nuevo comentario,
 * incluyendo la posibilidad de ser respuesta a otro comentario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioForoCreateDTO {

    /** Contenido textual del comentario */
    private String contenido;

    /** ID del usuario que realiza el comentario */
    private Long idUsuario;

    /** Fecha y hora en que se realiza el comentario */
    private LocalDateTime fechaComentario;

    /** ID del tema del foro al que pertenece el comentario */
    private Long idTema;

    /** ID del comentario al que responde, si es una respuesta (puede ser null) */
    private Long idComentarioPadre;
}
