package com.example.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la actualización de un bloqueo aplicado en un foro.
 * Permite modificar la relación entre el usuario y el foro, la fecha del bloqueo,
 * el estado actual del mismo y el motivo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloqueoForoUpdateDTO {

    /** ID del usuario que realiza o actualiza el bloqueo */
    private Long idUsuario;

    /** ID del foro bloqueado */
    private Long idForo;

    /** Fecha y hora en que se registró o modificó el bloqueo */
    private LocalDateTime fechaBloqueo;

    /** Estado actual del bloqueo (activo, inactivo, etc.) */
    private String estado;

    /** Motivo que justifica el bloqueo del foro */
    private String motivo;
}
