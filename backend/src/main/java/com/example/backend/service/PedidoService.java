package com.example.backend.service;

import com.example.backend.dto.PedidoDTO;
import com.example.backend.dto.PedidoUpdateDTO;
import com.example.backend.dto.PedidoCreateDTO;

import java.util.List;

/**
 * Interfaz que define los métodos del servicio para la gestión de pedidos.
 * Permite realizar operaciones CRUD y consultar pedidos por usuario.
 */
public interface PedidoService {

    /**
     * Recupera todos los pedidos almacenados.
     * @return lista de objetos PedidoDTO
     */
    List<PedidoDTO> findAll();

    /**
     * Busca un pedido por su identificador único.
     * @param id identificador del pedido
     * @return objeto PedidoDTO correspondiente
     */
    PedidoDTO findById(Long id);

    /**
     * Crea un nuevo pedido a partir de los datos recibidos.
     * @param dto DTO con los datos del nuevo pedido
     * @return pedido creado
     */
    PedidoDTO create(PedidoCreateDTO dto);

    /**
     * Actualiza un pedido existente.
     * @param id identificador del pedido a modificar
     * @param dto DTO con los nuevos datos del pedido
     * @return pedido actualizado
     */
    PedidoDTO update(Long id, PedidoUpdateDTO dto);

    /**
     * Elimina un pedido por su identificador.
     * @param id identificador del pedido a eliminar
     */
    void delete(Long id);

    /**
     * Devuelve todos los pedidos realizados por un usuario concreto.
     * @param idUsuario identificador del usuario
     * @return lista de pedidos del usuario
     */
    List<PedidoDTO> findByUsuario(Long idUsuario);
}
