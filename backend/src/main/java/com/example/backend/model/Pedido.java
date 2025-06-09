package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Entidad que representa un pedido realizado por un usuario.
 * Cada pedido incluye detalles, usuario asociado y dirección de envío.
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    /**
     * Identificador único del pedido.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Total del importe del pedido.
     */
    @Column(nullable = false)
    private double total;

    /**
     * Estado actual del pedido (e.g., ENVIADO, ENTREGADO).
     */
    @Column(nullable = false)
    private String estado;

    /**
     * Fecha en la que se realizó el pedido.
     */
    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    /**
     * Usuario que realizó el pedido.
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    /**
     * Dirección de envío asociada al pedido.
     */
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idDireccionEnvio", nullable = false)
    private DireccionEnvio direccionEnvio;

    /**
     * Lista de detalles de productos incluidos en el pedido.
     */
    @JsonBackReference
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;
}
