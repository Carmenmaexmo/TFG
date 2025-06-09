package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Entidad que representa un tema dentro de un foro.
 * Cada tema pertenece a un foro y puede contener múltiples comentarios.
 */
@Entity
@Table(name = "temas_foro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemaForo {

    /**
     * Identificador único del tema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título del tema del foro.
     */
    @Column(nullable = false)
    private String titulo;

    /**
     * Contenido principal del tema.
     */
    @Column(nullable = false)
    private String contenido;

    /**
     * Usuario que creó el tema.
     */
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /**
     * Lista de comentarios asociados al tema.
     */
    @OneToMany(mappedBy = "tema")
    @JsonBackReference
    private List<ComentarioForo> comentarios;

    /**
     * Foro al que pertenece este tema.
     */
    @ManyToOne
    @JoinColumn(name = "id_foro", nullable = false)
    @JsonBackReference
    private Foro foro;
}
