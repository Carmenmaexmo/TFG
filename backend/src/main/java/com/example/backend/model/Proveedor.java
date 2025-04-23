package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "proveedores")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Proveedor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "proveedor")
    private List<Vinilo> vinilos;
}