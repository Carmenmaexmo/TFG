package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de un nuevo tema dentro de un foro.
 * Contiene los datos necesarios para registrar un tema asociado a un usuario y un foro.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemaForoCreateDTO {

    /** Título del tema del foro */
    private String titulo;

    /** Contenido principal del tema */
    private String contenido;

    /** ID del usuario que crea el tema */
    private Long idUsuario;

    /** ID del foro al que pertenece el tema */
    private Long idForo;
}
