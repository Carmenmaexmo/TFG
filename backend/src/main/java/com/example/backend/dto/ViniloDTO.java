package com.example.backend.dto;

import lombok.*;

/**
 * DTO que representa un vinilo del catálogo.
 * Contiene toda la información necesaria para su visualización, incluyendo su proveedor.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViniloDTO {

    /** ID único del vinilo */
    private Long id;

    /** Título del vinilo */
    private String titulo;

    /** Artista o grupo musical del vinilo */
    private String artista;

    /** Género musical al que pertenece el vinilo */
    private String genero;

    /** Precio de venta del vinilo */
    private Double precio;

    /** URL o ruta de la imagen del vinilo */
    private String imagen;

    /** Unidades disponibles en stock */
    private Integer stock;

    /** Descripción adicional del vinilo */
    private String descripcion;

    /** Información del proveedor que suministra el vinilo */
    private ProveedorDTO proveedor;
}
