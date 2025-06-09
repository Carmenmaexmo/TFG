package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidad que representa a un usuario del sistema.
 * Incluye información personal, credenciales, rol y relaciones con pedidos, foros y eventos.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    /**
     * Nombre del usuario.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Apellidos del usuario.
     */
    @Column(nullable = false)
    private String apellidos;

    /**
     * Nombre de usuario único para autenticación.
     */
    @Column(nullable = false, unique = true)
    private String nombreUsuario;

    /**
     * Correo electrónico del usuario.
     */
    @Column(nullable = false)
    private String email;

    /**
     * Contraseña codificada del usuario.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Número de teléfono del usuario.
     */
    @Column(nullable = false)
    private String telefono;

    /**
     * Documento de identidad del usuario.
     */
    @Column(nullable = false)
    private String dni;

    /**
     * Rol del usuario (e.g. ADMIN, EMPLEADO, CLIENTE).
     */
    @Column(nullable = false)
    private String rol;

    /**
     * Carrito de compra del usuario en formato JSON.
     */
    @Lob
    @Column(nullable = true, columnDefinition = "LONGTEXT")
    private String carrito;

    /**
     * Pedidos realizados por el usuario.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Pedido> pedidos;

    /**
     * Direcciones de envío asociadas al usuario.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DireccionEnvio> direccionesEnvio;

    /**
     * Temas de foro creados por el usuario.
     */
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<TemaForo> temasForo;

    /**
     * Comentarios en foros hechos por el usuario.
     */
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<ComentarioForo> comentarios;

    /**
     * Bloqueos de foros asociados al usuario.
     */
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<BloqueoForo> bloqueos;

    /**
     * Asistencias a eventos en las que participa el usuario.
     */
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<AsistenciaEvento> asistenciaEventos;
}
