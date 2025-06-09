package com.example.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO que representa un evento registrado en el sistema.
 * Contiene todos los detalles necesarios para mostrar o procesar un evento.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoDTO {

    /** ID único del evento */
    private Long id;

    /** Título del evento */
    private String titulo;

    /** Porcentaje de descuento aplicado durante el evento */
    private Double descuento;

    /** Descripción del evento */
    private String descripcion;

    /** Fecha y hora de inicio del evento */
    private LocalDateTime fechaInicio;

    /** Fecha y hora de finalización del evento */
    private LocalDateTime fecha_fin;

    /** Lugar donde se celebra el evento */
    private String lugar;
}
