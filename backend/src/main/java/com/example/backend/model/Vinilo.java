package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "vinilos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vinilo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String artista;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "idProveedor", nullable = false)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "vinilo")
    private List<DetallePedido> detalles;
}