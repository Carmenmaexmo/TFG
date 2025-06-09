package com.example.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para la creación de un nuevo evento.
 * Contiene los campos necesarios para registrar un evento promocional o informativo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoCreateDTO {

    /** Título del evento */
    private String titulo;

    /** Descripción del evento */
    private String descripcion;

    /** Fecha y hora de inicio del evento */
    private LocalDateTime fechaInicio;

    /** Fecha y hora de finalización del evento */
    private LocalDateTime fecha_fin;

    /** Porcentaje de descuento aplicado durante el evento */
    private double descuento;

    /** Lugar donde se llevará a cabo el evento */
    private String lugar;
}
