// src/main/java/com/example/backend/dto/EventoUpdateDTO.java
package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class EventoUpdateDTO {
    private String titulo;
    private String descripcion;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_fin;
    private String lugar;
}
