// src/main/java/com/example/backend/repository/EventoRepository.java
package com.example.backend.repository;

import com.example.backend.model.Evento;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    boolean existsByTituloAndFechaInicio(String titulo, LocalDateTime fechaInicio);
}
