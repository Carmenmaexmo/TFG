package com.example.backend.dto;

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
    private String motivo;
}
