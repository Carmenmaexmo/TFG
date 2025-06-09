package com.example.backend.dto;

import lombok.*;

/**
 * DTO utilizado para operaciones de actualización o inserción (upsert)
 * de un detalle de pedido. Permite identificar el detalle existente
 * o crear uno nuevo, especificando el vinilo, su precio y la cantidad.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoUpsertDTO {

    /** ID del detalle de pedido (puede ser null si es nuevo) */
    private Long id;

    /** ID del vinilo incluido en el pedido */
    private Long idVinilo;

    /** Precio unitario del vinilo en el pedido */
    private Double precio;

    /** Cantidad de unidades del vinilo pedidas */
    private Integer cantidad;
}
