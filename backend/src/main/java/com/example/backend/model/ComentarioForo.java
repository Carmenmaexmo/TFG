package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Entidad que representa un comentario dentro de un tema de foro.
 * Puede ser un comentario inicial o una respuesta a otro comentario.
 */
@Entity
@Table(name = "comentarios_foro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioForo {

    /**
     * Identificador único del comentario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Contenido textual del comentario.
     */
    @Column(nullable = false)
    private String contenido;

    /**
     * Fecha y hora en que se realizó el comentario.
     */
    @Column(nullable = false)
    private LocalDateTime fechaComentario;

    /**
     * Usuario que publicó el comentario.
     * Relación muchos a uno con la entidad Usuario.
     */
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /**
     * Tema del foro al que pertenece el comentario.
     * Relación muchos a uno con la entidad TemaForo.
     */
    @ManyToOne
    @JoinColumn(name = "idTema", nullable = false)
    @JsonBackReference
    private TemaForo tema;

    /**
     * Comentario al que este responde, si es una respuesta.
     * Relación recursiva muchos a uno con la misma entidad.
     */
    @ManyToOne
    @JoinColumn(name = "idComentarioPadre")
    @JsonBackReference
    private ComentarioForo comentarioPadre;

    /**
     * Lista de respuestas asociadas a este comentario.
     * Relación uno a muchos con la misma entidad.
     */
    @OneToMany(mappedBy = "comentarioPadre")
    @JsonBackReference
    private List<ComentarioForo> respuestas;
}
