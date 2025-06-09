// src/main/java/com/example/backend/repository/AsistenciaEventoRepository.java
package com.example.backend.repository;

import com.example.backend.model.AsistenciaEvento;
import com.example.backend.model.Evento;
import com.example.backend.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para gestionar las entidades de tipo AsistenciaEvento.
 * Proporciona operaciones CRUD y consultas personalizadas relacionadas con la asistencia a eventos.
 */
public interface AsistenciaEventoRepository extends JpaRepository<AsistenciaEvento, Long> {

    /**
     * Verifica si ya existe una asistencia registrada para un usuario y un evento específicos.
     *
     * @param usuario el usuario a consultar.
     * @param evento el evento a consultar.
     * @return true si existe una asistencia registrada, false en caso contrario.
     */
    boolean existsByUsuarioAndEvento(Usuario usuario, Evento evento);

    /**
     * Obtiene una lista de asistencias para un evento concreto.
     *
     * @param idEvento ID del evento.
     * @return lista de asistencias asociadas a dicho evento.
     */
    List<AsistenciaEvento> findByEventoId(Long idEvento);
}
