package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de una asistencia a evento.
 * Contiene los datos mínimos necesarios para registrar la relación
 * entre un usuario y un evento, así como su estado de confirmación.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaEventoCreateDTO {

    /** ID del usuario que asiste al evento */
    private Long idUsuario;

    /** ID del evento al que se asiste */
    private Long idEvento;

    /** Indica si la asistencia está confirmada */
    private Boolean confirmado;
}
