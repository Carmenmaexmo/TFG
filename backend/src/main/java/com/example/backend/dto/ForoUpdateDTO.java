package com.example.backend.dto;

import lombok.Data;

/**
 * DTO para la actualización de un foro existente.
 * Permite modificar el nombre y la descripción del foro.
 */
@Data
public class ForoUpdateDTO {

    /** Nuevo nombre del foro */
    private String nombre;

    /** Nueva descripción del foro */
    private String descripcion;
}
