package com.example.backend.repository;

import com.example.backend.model.Pedido;
import com.example.backend.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuario(Usuario usuario);

}
