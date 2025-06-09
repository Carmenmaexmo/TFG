package com.example.backend.repository;

import com.example.backend.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad DetallePedido.
 * Proporciona acceso a operaciones CRUD básicas sobre los detalles de los pedidos.
 */
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}
