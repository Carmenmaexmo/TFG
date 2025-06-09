package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidad que representa un evento disponible en la plataforma.
 * Un evento puede tener asistentes registrados.
 */
@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    /**
     * Identificador único del evento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título o nombre del evento.
     */
    @Column(nullable = false)
    private String titulo;

    /**
     * Descripción opcional del evento.
     */
    @Column(nullable = true)
    private String descripcion;

    /**
     * Fecha y hora de inicio del evento.
     */
    @Column(nullable = false, name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    /**
     * Fecha y hora de finalización del evento.
     */
    @Column(nullable = false)
    private LocalDateTime fecha_fin;

    /**
     * Lugar donde se celebra el evento.
     */
    @Column(nullable = false)
    private String lugar;

    /**
     * Descuento aplicado durante el evento.
     */
    @Column(nullable = false)
    private double descuento;

    /**
     * Lista de asistencias registradas a este evento.
     */
    @OneToMany(mappedBy = "evento")
    @JsonManagedReference
    private List<AsistenciaEvento> asistentes;
}
