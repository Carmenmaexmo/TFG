package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "temas_foro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TemaForo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "tema")
    private List<ComentarioForo> comentarios;

    @ManyToOne
    @JoinColumn(name = "id_foro", nullable = false)
    private Foro foro;

}