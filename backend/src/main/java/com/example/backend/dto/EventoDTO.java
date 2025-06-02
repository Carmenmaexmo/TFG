package com.example.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoDTO {
    private Long id;
    private String titulo;
    private Double descuento;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fecha_fin;
    private String lugar;
}
