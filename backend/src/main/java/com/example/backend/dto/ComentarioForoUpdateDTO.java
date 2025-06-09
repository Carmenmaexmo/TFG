package com.example.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para la actualización de un comentario en un tema del foro.
 * Permite modificar el contenido, la fecha, el autor, el tema asociado
 * y la referencia al comentario padre en caso de respuesta.
 */
@Data
public class ComentarioForoUpdateDTO {

    /** Contenido actualizado del comentario */
    private String contenido;

    /** Fecha y hora de modificación o publicación del comentario */
    private LocalDateTime fechaComentario;

    /** ID del usuario que realiza el comentario */
    private Long idUsuario;

    /** ID del tema del foro asociado al comentario */
    private Long idTema;

    /** ID del comentario padre si se trata de una respuesta (puede ser null) */
    private Long idComentarioPadre;
}
