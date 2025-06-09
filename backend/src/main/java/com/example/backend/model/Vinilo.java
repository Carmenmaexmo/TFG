package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidad que representa un vinilo musical dentro del catálogo.
 * Contiene información como título, artista, precio, stock y proveedor asociado.
 */
@Entity
@Table(name = "vinilos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vinilo {

    /**
     * Identificador único del vinilo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título del vinilo.
     */
    @Column(nullable = false)
    private String titulo;

    /**
     * Nombre del artista o grupo musical.
     */
    @Column(nullable = false)
    private String artista;

    /**
     * Género musical al que pertenece el vinilo (e.g., Rock, Jazz, Pop...).
     */
    @Column(nullable = false)
    private String genero;

    /**
     * Precio de venta del vinilo.
     */
    @Column(nullable = false)
    private double precio;

    /**
     * Cantidad de unidades disponibles en stock.
     */
    @Column(nullable = false)
    private int stock;

    /**
     * Ruta o URL de la imagen de portada del vinilo.
     */
    @Column(nullable = true)
    private String imagen;

    /**
     * Descripción opcional del vinilo (e.g., información extra, reedición, etc.).
     */
    @Column(nullable = true)
    private String descripcion;

    /**
     * Proveedor que suministra este vinilo.
     */
    @ManyToOne
    @JoinColumn(name = "idProveedor", nullable = false)
    @JsonBackReference
    private Proveedor proveedor;

    /**
     * Lista de detalles de pedido que incluyen este vinilo.
     */
    @OneToMany(mappedBy = "vinilo")
    @JsonManagedReference
    private List<DetallePedido> detalles;
}
