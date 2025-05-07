// src/main/java/com/example/backend/dto/AsistenciaEventoUpdateDTO.java
package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AsistenciaEventoUpdateDTO {
    private Long idUsuario;
    private Long idEvento;
    private Boolean confirmado;
}
