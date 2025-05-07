package com.example.backend.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoForoDTO {
    private Long id;
    private UsuarioDTO usuario;
    private ForoDTO foro;
    private LocalDateTime fechaBloqueo;
    private String motivo;
}
