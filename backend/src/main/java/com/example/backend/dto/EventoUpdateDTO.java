package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO para la actualización de un evento existente.
 * Permite modificar los campos principales del evento, excepto su ID y descuento.
 */
@Getter
@Setter
public class EventoUpdateDTO {

    /** Título del evento */
    private String titulo;

    /** Descripción del evento */
    private String descripcion;

    /** Fecha y hora de inicio del evento */
    private LocalDateTime fechaInicio;

    /** Fecha y hora de finalización del evento */
    private LocalDateTime fecha_fin;

    /** Lugar donde se llevará a cabo el evento */
    private String lugar;
}
