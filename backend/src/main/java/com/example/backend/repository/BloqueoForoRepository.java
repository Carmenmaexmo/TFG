// src/main/java/com/example/backend/repository/BloqueoForoRepository.java
package com.example.backend.repository;

import com.example.backend.model.BloqueoForo;
import com.example.backend.model.Foro;
import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para gestionar las entidades de tipo BloqueoForo.
 * Permite realizar operaciones CRUD y consultar bloqueos específicos por usuario y foro.
 */
public interface BloqueoForoRepository extends JpaRepository<BloqueoForo, Long> {

    /**
     * Verifica si un usuario ya tiene un bloqueo activo en un foro concreto.
     *
     * @param usuario el usuario a consultar.
     * @param foro el foro a consultar.
     * @return true si existe un bloqueo registrado, false en caso contrario.
     */
    boolean existsByUsuarioAndForo(Usuario usuario, Foro foro);
}
