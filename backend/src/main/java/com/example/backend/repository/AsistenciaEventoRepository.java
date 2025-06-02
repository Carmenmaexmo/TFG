// src/main/java/com/example/backend/repository/AsistenciaEventoRepository.java
package com.example.backend.repository;


import com.example.backend.model.AsistenciaEvento;
import com.example.backend.model.Evento;
import com.example.backend.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenciaEventoRepository extends JpaRepository<AsistenciaEvento, Long> {
    boolean existsByUsuarioAndEvento(Usuario usuario, Evento evento);
    List<AsistenciaEvento> findByEventoId(Long idEvento);
}
