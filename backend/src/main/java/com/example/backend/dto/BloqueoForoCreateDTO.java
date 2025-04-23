package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoForoCreateDTO {
    private Long idUsuario;
    private Long idForo;
    private String motivo;
}
