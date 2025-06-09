package com.example.backend.repository;

import com.example.backend.model.ComentarioForo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad ComentarioForo.
 * Permite realizar operaciones CRUD y consultas específicas relacionadas con los comentarios en el foro.
 */
public interface ComentarioForoRepository extends JpaRepository<ComentarioForo, Long> {

    /**
     * Obtiene una lista de respuestas asociadas a un comentario padre.
     *
     * @param padreId ID del comentario padre.
     * @return Lista de comentarios que son respuestas al comentario padre.
     */
    List<ComentarioForo> findByComentarioPadre_Id(Long padreId);

    /**
     * Obtiene todos los comentarios relacionados con un tema específico del foro.
     *
     * @param idTema ID del tema del foro.
     * @return Lista de comentarios pertenecientes al tema indicado.
     */
    List<ComentarioForo> findByTemaId(Long idTema);
}
