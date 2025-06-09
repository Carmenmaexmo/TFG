package com.example.backend.service;

import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.dto.ComentarioForoUpdateDTO;
import com.example.backend.dto.ComentarioForoCreateDTO;

import java.util.List;

/**
 * Interfaz que define las operaciones del servicio para la gestión de comentarios en los temas del foro.
 */
public interface ComentarioForoService {

    /**
     * Obtiene todos los comentarios registrados en el sistema.
     *
     * @return lista de objetos ComentarioForoDTO
     */
    List<ComentarioForoDTO> findAll();

    /**
     * Obtiene los detalles de un comentario específico por su ID.
     *
     * @param id identificador del comentario
     * @return objeto ComentarioForoDTO correspondiente al comentario encontrado
     */
    ComentarioForoDTO findById(Long id);

    /**
     * Crea un nuevo comentario en un tema del foro.
     *
     * @param dto datos necesarios para crear el comentario
     * @return objeto ComentarioForoDTO del comentario creado
     */
    ComentarioForoDTO create(ComentarioForoCreateDTO dto);

    /**
     * Actualiza los datos de un comentario existente.
     *
     * @param id identificador del comentario a actualizar
     * @param dto datos nuevos para la actualización
     * @return objeto ComentarioForoDTO actualizado
     */
    ComentarioForoDTO update(Long id, ComentarioForoUpdateDTO dto);

    /**
     * Elimina un comentario por su ID.
     *
     * @param id identificador del comentario a eliminar
     */
    void delete(Long id);

    /**
     * Obtiene todos los comentarios asociados a un tema específico del foro.
     *
     * @param idTema identificador del tema del foro
     * @return lista de comentarios pertenecientes al tema
     */
    List<ComentarioForoDTO> findByTema(Long idTema);
}
