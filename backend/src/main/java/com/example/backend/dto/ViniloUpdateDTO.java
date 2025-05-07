package com.example.backend.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ViniloUpdateDTO {
    private String titulo;
    private String artista;
    private String genero;
    private Double precio;
    private Integer stock;
    private String imagen;
    private String descripcion;
    private Long idProveedor;
}
