package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de un nuevo foro.
 * Contiene los datos básicos necesarios para registrar un foro temático.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForoCreateDTO {

    /** Nombre del foro */
    private String nombre;

    /** Descripción del foro */
    private String descripcion;
}
