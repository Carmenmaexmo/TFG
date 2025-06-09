package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de una dirección de envío.
 * Contiene los datos necesarios para asociar una nueva dirección a un usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionEnvioCreateDTO {

    /** ID del usuario al que se asocia la dirección de envío */
    private Long idUsuario;

    /** Ciudad donde se realiza el envío */
    private String ciudad;

    /** Dirección completa del envío (calle, número, piso, etc.) */
    private String direccion;

    /** Teléfono de contacto asociado a la dirección */
    private String telefono;

    /** Código postal correspondiente a la dirección */
    private String codigoPostal;
}
