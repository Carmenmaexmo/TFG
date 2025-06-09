package com.example.backend.dto;

import lombok.*;

/**
 * DTO que representa una dirección de envío registrada.
 * Contiene la información detallada necesaria para mostrar o procesar envíos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionEnvioDTO {

    /** ID único de la dirección de envío */
    private Long id;

    /** Ciudad asociada a la dirección */
    private String ciudad;

    /** Código postal correspondiente */
    private String codigoPostal;

    /** Dirección completa del envío (calle, número, etc.) */
    private String direccion;

    /** Teléfono de contacto asociado a la dirección */
    private String telefono;
}
