package com.example.backend.dto;

import lombok.Data;

@Data
public class TemaForoUpdateDTO {
    private String titulo;
    private String contenido;
    private Long idForo;
    private Long idUsuario;
}
