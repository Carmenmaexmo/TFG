package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViniloDTO {
    private Long id;
    private String titulo;
    private String artista;
    private String genero;
    private Double precio;
    private String imagenUrl;
    private Integer stock;
}

