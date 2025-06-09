package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la actualización de una asistencia a evento.
 * Permite modificar el usuario asociado, el evento y el estado de confirmación.
 */
@Getter
@Setter
public class AsistenciaEventoUpdateDTO {

    /** ID del usuario que asiste al evento */
    private Long idUsuario;

    /** ID del evento al que se asiste */
    private Long idEvento;

    /** Indica si la asistencia está confirmada */
    private Boolean confirmado;
}
