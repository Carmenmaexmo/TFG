package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidad que representa a un proveedor.
 * Cada proveedor puede estar asociado a múltiples vinilos.
 */
@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    /**
     * Identificador único del proveedor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del proveedor.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Correo electrónico de contacto del proveedor.
     */
    @Column(nullable = false)
    private String email;

    /**
     * Teléfono de contacto del proveedor.
     */
    @Column(nullable = false)
    private String telefono;

    /**
     * Dirección física del proveedor.
     */
    @Column(nullable = false)
    private String direccion;

    /**
     * Lista de vinilos suministrados por este proveedor.
     */
    @OneToMany(mappedBy = "proveedor")
    @JsonManagedReference
    private List<Vinilo> vinilos;
}
