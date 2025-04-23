package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String dni;

    @Column(nullable = false)
    private String rol;

    @Lob
    @Column(nullable = false)
    private String carrito;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<DireccionEnvio> direccionesEnvio;

    @OneToMany(mappedBy = "usuario")
    private List<TemaForo> temasForo;

    @OneToMany(mappedBy = "usuario")
    private List<ComentarioForo> comentarios;

    @OneToMany(mappedBy = "usuario")
    private List<BloqueoForo> bloqueos;

    @OneToMany(mappedBy = "usuario")
    private List<AsistenciaEvento> asistenciaEventos;

}