package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalles_pedidos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetallePedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precio;

    @ManyToOne
    @JoinColumn(name = "idPedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "idVinilo", nullable = false)
    private Vinilo vinilo;
}