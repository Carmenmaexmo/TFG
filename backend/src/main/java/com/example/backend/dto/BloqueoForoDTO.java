package com.example.backend.dto;

import java.time.LocalDateTime;

import lombok.*;

/**
 * DTO que representa un bloqueo aplicado sobre un foro.
 * Contiene la información del usuario que bloqueó, el foro afectado,
 * la fecha del bloqueo y el motivo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoForoDTO {

    /** ID único del bloqueo */
    private Long id;

    /** Usuario que realizó el bloqueo */
    private UsuarioDTO usuario;

    /** Foro al que se aplicó el bloqueo */
    private ForoDTO foro;

    /** Fecha y hora del bloqueo */
    private LocalDateTime fechaBloqueo;

    /** Motivo por el cual se realizó el bloqueo */
    private String motivo;
}
