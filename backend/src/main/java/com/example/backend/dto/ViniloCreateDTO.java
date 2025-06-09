package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de un nuevo vinilo en el catálogo.
 * Incluye toda la información necesaria para registrar un producto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViniloCreateDTO {

    /** Título del vinilo */
    private String titulo;

    /** Artista o grupo musical del vinilo */
    private String artista;

    /** Género musical del vinilo (ej. Rock, Jazz, Pop) */
    private String genero;

    /** Precio de venta del vinilo */
    private Double precio;

    /** URL o ruta de la imagen del vinilo */
    private String imagen;

    /** Cantidad disponible en stock */
    private Integer stock;

    /** ID del proveedor que suministra este vinilo */
    private Long idProveedor;

    /** Descripción adicional del vinilo */
    private String descripcion;
}
