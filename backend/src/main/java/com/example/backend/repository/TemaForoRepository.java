package com.example.backend.repository;

import com.example.backend.model.TemaForo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad TemaForo.
 * Proporciona acceso a las operaciones CRUD y consultas personalizadas sobre los temas de los foros.
 */
public interface TemaForoRepository extends JpaRepository<TemaForo, Long> {

    /**
     * Verifica si ya existe un tema con el mismo título dentro de un foro específico.
     * Esto se utiliza para evitar duplicados dentro del mismo foro.
     *
     * @param titulo título del tema del foro.
     * @param foroId identificador del foro.
     * @return true si ya existe un tema con ese título en el foro, false en caso contrario.
     */
    boolean existsByTituloAndForoId(String titulo, Long foroId);
}
