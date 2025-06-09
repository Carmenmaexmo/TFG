package com.example.backend.dto;

import lombok.*;

/**
 * DTO para la creación de un detalle de pedido.
 * Representa cada línea de un pedido con la información del vinilo,
 * su precio en el momento de la compra y la cantidad solicitada.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoCreateDTO {

    /** ID del vinilo asociado al detalle del pedido */
    private Long idVinilo;

    /** Precio unitario del vinilo en el momento del pedido */
    private Double precio;

    /** Cantidad de unidades solicitadas de ese vinilo */
    private Integer cantidad;
}
