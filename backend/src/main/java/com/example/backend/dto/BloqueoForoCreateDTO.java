package com.example.backend.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoForoCreateDTO {
    private Long idUsuario;
    private Long idForo;
    private LocalDateTime fechaBloqueo;
    private String estado;
    private String motivo;
}
