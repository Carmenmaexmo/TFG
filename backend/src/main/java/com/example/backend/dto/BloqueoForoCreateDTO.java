package com.example.backend.dto;

import java.time.LocalDateTime;

import lombok.*;

/**
 * DTO para la creación de un bloqueo en un foro.
 * Incluye información sobre el usuario que bloquea, el foro afectado,
 * la fecha del bloqueo, el estado actual y el motivo correspondiente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoForoCreateDTO {

    /** ID del usuario que realiza el bloqueo */
    private Long idUsuario;

    /** ID del foro que se desea bloquear */
    private Long idForo;

    /** Fecha y hora en que se aplica el bloqueo */
    private LocalDateTime fechaBloqueo;

    /** Estado actual del bloqueo (activo, inactivo, etc.) */
    private String estado;

    /** Motivo o justificación del bloqueo */
    private String motivo;
}
