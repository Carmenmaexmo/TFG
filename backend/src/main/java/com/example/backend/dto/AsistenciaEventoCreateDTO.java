package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaEventoCreateDTO {
    private Long idUsuario;
    private Long idEvento;
    private Boolean confirmado;
}
