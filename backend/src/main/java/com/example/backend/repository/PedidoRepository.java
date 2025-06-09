package com.example.backend.repository;

import com.example.backend.model.Pedido;
import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Pedido.
 * Proporciona operaciones CRUD y consultas específicas sobre pedidos.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Devuelve una lista de pedidos realizados por un usuario específico.
     *
     * @param usuario el usuario cuyos pedidos se desean recuperar.
     * @return lista de pedidos asociados al usuario.
     */
    List<Pedido> findByUsuario(Usuario usuario);
}
