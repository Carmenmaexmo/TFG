package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asistencia_evento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AsistenciaEvento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idEvento", nullable = false)
    private Evento evento;
}