package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la actualización de un vinilo existente en el catálogo.
 * Permite modificar los datos principales del vinilo, incluyendo su proveedor.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViniloUpdateDTO {

    /** Nuevo título del vinilo */
    private String titulo;

    /** Artista o grupo musical actualizado */
    private String artista;

    /** Género musical actualizado */
    private String genero;

    /** Nuevo precio del vinilo */
    private Double precio;

    /** Stock actualizado disponible del vinilo */
    private Integer stock;

    /** URL o ruta de la imagen del vinilo */
    private String imagen;

    /** Descripción actualizada del vinilo */
    private String descripcion;

    /** ID del nuevo proveedor (si aplica) */
    private Long idProveedor;
}
