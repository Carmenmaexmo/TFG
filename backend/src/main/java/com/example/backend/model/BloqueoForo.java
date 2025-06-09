package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Entidad que representa un bloqueo aplicado a un usuario en un foro.
 */
@Entity
@Table(name = "bloqueos_foro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueoForo {

    /**
     * Identificador único del bloqueo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Motivo por el cual se ha aplicado el bloqueo.
     */
    @Column(nullable = false)
    private String motivo;

    /**
     * Fecha en la que se aplicó el bloqueo.
     */
    @Column(nullable = false)
    private LocalDateTime fechaBloqueo;

    /**
     * Estado del bloqueo (por ejemplo: activo, inactivo, etc.).
     */
    @Column(nullable = false)
    private String estado;

    /**
     * Usuario al que se le ha aplicado el bloqueo.
     * Relación muchos a uno con la entidad Usuario.
     */
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /**
     * Foro en el que se ha producido el bloqueo.
     * Relación muchos a uno con la entidad Foro.
     */
    @ManyToOne
    @JoinColumn(name = "id_foro", nullable = false)
    @JsonBackReference
    private Foro foro;

}
