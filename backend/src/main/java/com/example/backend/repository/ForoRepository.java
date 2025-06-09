package com.example.backend.repository;

import com.example.backend.model.Foro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad Foro.
 * Permite realizar operaciones CRUD y consultas personalizadas relacionadas con los foros.
 */
public interface ForoRepository extends JpaRepository<Foro, Long> {

    /**
     * Verifica si existe un foro con el nombre proporcionado.
     *
     * @param nombre el nombre del foro a verificar.
     * @return true si existe un foro con ese nombre, false en caso contrario.
     */
    boolean existsByNombre(String nombre);

    /**
     * Busca un foro por su nombre exacto.
     *
     * @param nombre el nombre del foro.
     * @return el foro encontrado o null si no existe.
     */
    Foro findByNombre(String nombre);
}
