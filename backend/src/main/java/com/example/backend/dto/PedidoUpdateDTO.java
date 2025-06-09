// src/main/java/com/example/backend/dto/PedidoUpdateDTO.java
package com.example.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la actualización de un pedido existente.
 * Permite modificar los datos generales del pedido y sus detalles.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoUpdateDTO {

    /** ID del usuario que realizó el pedido */
    private Long idUsuario;

    /** ID de la dirección de envío asociada */
    private Long idDireccionEnvio;

    /** Estado actual del pedido (ej. PENDIENTE, ENVIADO, ENTREGADO) */
    private String estado;

    /** Fecha y hora del pedido */
    private LocalDateTime fechaPedido;

    /** Importe total del pedido */
    private Double total;

    /** Lista de productos del pedido con posibilidad de crear o actualizar */
    private List<DetallePedidoUpsertDTO> detalles;
}
