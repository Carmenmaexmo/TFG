package com.example.backend.dto;

import lombok.*;

/**
 * DTO que representa el detalle de un pedido.
 * Incluye la cantidad solicitada, el precio unitario y la información del vinilo correspondiente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoDTO {

    /** ID único del detalle de pedido */
    private Long id;

    /** Cantidad de unidades solicitadas del vinilo */
    private Integer cantidad;

    /** Precio unitario del vinilo en el momento de la compra */
    private Double precio;

    /** Información del vinilo asociado al detalle del pedido */
    private ViniloDTO vinilo;
}
