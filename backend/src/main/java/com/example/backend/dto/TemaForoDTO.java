package com.example.backend.dto;

import lombok.*;

/**
 * DTO que representa un tema dentro de un foro.
 * Contiene la información completa del tema, su autor y el foro al que pertenece.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemaForoDTO {

    /** ID único del tema del foro */
    private Long id;

    /** Título del tema */
    private String titulo;

    /** Contenido principal del tema */
    private String contenido;

    /** Usuario que creó el tema */
    private UsuarioDTO usuario;

    /** Foro al que pertenece el tema */
    private ForoDTO foro;
}
