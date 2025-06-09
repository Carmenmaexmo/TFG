package com.example.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que representa un pedido realizado por un usuario.
 * Incluye datos generales del pedido, su estado, el usuario asociado,
 * la dirección de envío y el listado de productos pedidos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {

    /** ID único del pedido */
    private Long id;

    /** Fecha y hora en que se realizó el pedido */
    private LocalDateTime fechaPedido;

    /** Importe total del pedido */
    private Double total;

    /** Estado actual del pedido (PENDIENTE, ENVIADO, ENTREGADO, etc.) */
    private String estado;

    /** Usuario que realizó el pedido */
    private UsuarioDTO usuario;

    /** Dirección de envío seleccionada para el pedido */
    private DireccionEnvioDTO direccionEnvio;

    /** Lista de detalles del pedido (vinilos, cantidades, precios) */
    private List<DetallePedidoDTO> detalles;
}
