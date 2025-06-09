package com.example.backend.repository;

import com.example.backend.model.DireccionEnvio;
import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad DireccionEnvio.
 * Permite realizar operaciones CRUD y consultas personalizadas relacionadas con las direcciones de envío.
 */
public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long> {

    /**
     * Obtiene todas las direcciones de envío asociadas a un usuario específico.
     *
     * @param usuario el usuario del que se desean obtener las direcciones.
     * @return lista de direcciones de envío asociadas al usuario.
     */
    List<DireccionEnvio> findByUsuario(Usuario usuario);
}
