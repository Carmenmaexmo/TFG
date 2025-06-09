package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un detalle de un pedido, es decir,
 * un producto individual (vinilo) dentro de una orden de compra.
 */
@Entity
@Table(name = "detalles_pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido {

    /**
     * Identificador único del detalle del pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cantidad de unidades del vinilo en el pedido.
     */
    @Column(nullable = false)
    private int cantidad;

    /**
     * Precio unitario del vinilo en el momento del pedido.
     */
    @Column(nullable = false)
    private double precio;

    /**
     * Pedido al que pertenece este detalle.
     * Relación muchos a uno con la entidad Pedido.
     */
    @ManyToOne
    @JoinColumn(name = "idPedido", nullable = false)
    @JsonBackReference
    private Pedido pedido;

    /**
     * Vinilo asociado a este detalle del pedido.
     * Relación muchos a uno con la entidad Vinilo.
     */
    @ManyToOne
    @JoinColumn(name = "idVinilo", nullable = false)
    @JsonBackReference
    private Vinilo vinilo;
}
