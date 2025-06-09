package com.example.backend.dto;

import lombok.Data;

/**
 * DTO para la actualización de un tema del foro.
 * Permite modificar el título, el contenido, el foro asociado y el autor del tema.
 */
@Data
public class TemaForoUpdateDTO {

    /** Nuevo título del tema */
    private String titulo;

    /** Nuevo contenido del tema */
    private String contenido;

    /** ID del foro al que se desea asociar el tema */
    private Long idForo;

    /** ID del usuario autor del tema */
    private Long idUsuario;
}
