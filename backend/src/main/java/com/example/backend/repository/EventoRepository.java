package com.example.backend.repository;

import com.example.backend.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * Repositorio JPA para la entidad Evento.
 * Proporciona operaciones CRUD y consultas personalizadas para la gestión de eventos.
 */
public interface EventoRepository extends JpaRepository<Evento, Long> {

    /**
     * Verifica si existe un evento con el mismo título y fecha de inicio.
     * 
     * @param titulo el título del evento.
     * @param fechaInicio la fecha de inicio del evento.
     * @return true si existe un evento con ese título y fecha de inicio, false en caso contrario.
     */
    boolean existsByTituloAndFechaInicio(String titulo, LocalDateTime fechaInicio);
}
