package com.example.backend.dto;

import lombok.*;

/**
 * DTO para representar una asistencia a un evento.
 * Contiene la información completa de la relación entre
 * un usuario y un evento, así como su estado de confirmación.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaEventoDTO {

    /** ID único de la asistencia */
    private Long id;

    /** Datos del usuario que asiste al evento */
    private UsuarioDTO usuario;

    /** Datos del evento al que se asiste */
    private EventoDTO evento;

    /** Indica si la asistencia ha sido confirmada */
    private Boolean confirmado;
}
