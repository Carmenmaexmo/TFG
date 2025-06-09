package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Entidad que representa una dirección de envío asociada a un usuario.
 * Una dirección puede estar vinculada a múltiples pedidos.
 */
@Entity
@Table(name = "direcciones_envio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionEnvio {

    /**
     * Identificador único de la dirección de envío.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Dirección completa (calle, número, etc.).
     */
    @Column(nullable = false)
    private String direccion;

    /**
     * Ciudad correspondiente a la dirección.
     */
    @Column(nullable = false)
    private String ciudad;

    /**
     * Código postal de la dirección.
     */
    @Column(nullable = false)
    private String codigoPostal;

    /**
     * Teléfono de contacto asociado a esta dirección.
     */
    @Column(nullable = false)
    private String telefono;

    /**
     * Usuario propietario de la dirección de envío.
     */
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    /**
     * Lista de pedidos que se enviaron a esta dirección.
     */
    @OneToMany(mappedBy = "direccionEnvio")
    @JsonBackReference
    private List<Pedido> pedidos;
}
