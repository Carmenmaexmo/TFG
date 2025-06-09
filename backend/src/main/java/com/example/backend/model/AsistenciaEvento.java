package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa la asistencia de un usuario a un evento concreto.
 */
@Entity
@Table(name = "asistencia_evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaEvento {

    /**
     * Identificador único de la asistencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario que asiste al evento.
     * Relación muchos a uno con la entidad Usuario.
     */
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /**
     * Evento al que se asiste.
     * Relación muchos a uno con la entidad Evento.
     */
    @ManyToOne
    @JoinColumn(name = "idEvento", nullable = false)
    @JsonBackReference
    private Evento evento;

    /**
     * Indica si la asistencia ha sido confirmada por el usuario.
     */
    @Column(nullable = false)
    @JsonBackReference
    private boolean confirmado;
}
