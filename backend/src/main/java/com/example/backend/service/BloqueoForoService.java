package com.example.backend.service;

import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.dto.BloqueoForoUpdateDTO;
import com.example.backend.dto.BloqueoForoCreateDTO;

import java.util.List;

/**
 * Servicio que gestiona los bloqueos aplicados a los foros.
 */
public interface BloqueoForoService {

    /**
     * Obtiene la lista completa de bloqueos registrados en el sistema.
     *
     * @return lista de objetos BloqueoForoDTO
     */
    List<BloqueoForoDTO> findAll();

    /**
     * Busca un bloqueo específico a partir de su ID.
     *
     * @param id identificador único del bloqueo
     * @return objeto BloqueoForoDTO con los datos del bloqueo
     */
    BloqueoForoDTO findById(Long id);

    /**
     * Crea un nuevo bloqueo sobre un foro.
     *
     * @param dto datos necesarios para crear el bloqueo
     * @return el nuevo bloqueo creado como DTO
     */
    BloqueoForoDTO create(BloqueoForoCreateDTO dto);

    /**
     * Actualiza los datos de un bloqueo existente.
     *
     * @param id identificador del bloqueo a modificar
     * @param dto datos actualizados del bloqueo
     * @return el bloqueo actualizado como DTO
     */
    BloqueoForoDTO update(Long id, BloqueoForoUpdateDTO dto);

    /**
     * Elimina un bloqueo del sistema según su ID.
     *
     * @param id identificador del bloqueo a eliminar
     */
    void delete(Long id);
}
