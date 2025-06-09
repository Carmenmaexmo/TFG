package com.example.backend.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la creación de un nuevo pedido.
 * Contiene la información del usuario, dirección de envío, estado, total,
 * fecha del pedido y el listado de detalles del mismo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCreateDTO {

    /** ID del usuario que realiza el pedido */
    private Long idUsuario;

    /** ID de la dirección de envío seleccionada */
    private Long idDireccionEnvio;

    /** Estado inicial del pedido (por ejemplo: PENDIENTE, ENVIADO, ENTREGADO) */
    private String estado;

    /** Importe total del pedido */
    private Double total;

    /** Fecha y hora en que se realizó el pedido */
    private LocalDateTime fechaPedido;

    /** Lista de detalles que componen el pedido (productos, cantidades, precios) */
    private List<DetallePedidoCreateDTO> detalles;
}
