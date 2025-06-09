package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidad que representa un foro dentro de la plataforma.
 * Un foro puede tener múltiples temas y bloqueos asociados.
 */
@Entity
@Table(name = "foro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foro {

    /**
     * Identificador único del foro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del foro.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Descripción opcional del foro.
     */
    @Column(nullable = true)
    private String descripcion;

    /**
     * Lista de temas asociados a este foro.
     */
    @OneToMany(mappedBy = "foro")
    @JsonManagedReference
    private List<TemaForo> temas;

    /**
     * Lista de bloqueos aplicados a este foro.
     */
    @OneToMany(mappedBy = "foro")
    @JsonManagedReference
    private List<BloqueoForo> bloqueos;
}
