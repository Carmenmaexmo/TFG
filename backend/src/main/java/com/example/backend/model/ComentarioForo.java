package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "comentarios_foro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ComentarioForo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaComentario;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idTema", nullable = false)
    private TemaForo tema;

    @ManyToOne
    @JoinColumn(name = "idComentarioPadre")
    private ComentarioForo comentarioPadre;

    @OneToMany(mappedBy = "comentarioPadre")
    private List<ComentarioForo> respuestas;
}