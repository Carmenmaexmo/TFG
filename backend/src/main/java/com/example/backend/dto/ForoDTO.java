package com.example.backend.dto;

import lombok.*;

/**
 * DTO que representa un foro temático del sistema.
 * Incluye los datos básicos de identificación y descripción del foro.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForoDTO {

    /** ID único del foro */
    private Long id;

    /** Nombre del foro */
    private String nombre;

    /** Descripción general del foro */
    private String descripcion;
}
