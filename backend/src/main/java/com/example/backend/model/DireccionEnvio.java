package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "direcciones_envio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DireccionEnvio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String codigoPostal;

    @Column(nullable = false)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "direccionEnvio")
    @Column(nullable = false)
    private List<Pedido> pedidos;
}
