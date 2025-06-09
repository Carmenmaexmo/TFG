// src/main/java/com/example/backend/service/TemaForoService.java
package com.example.backend.service;

import com.example.backend.dto.TemaForoDTO;
import com.example.backend.dto.TemaForoUpdateDTO;
import com.example.backend.dto.TemaForoCreateDTO;

import java.util.List;

/**
 * Interfaz del servicio para la gestión de temas dentro de los foros.
 * Proporciona las operaciones básicas para crear, actualizar, eliminar y consultar temas.
 */
public interface TemaForoService {

    /**
     * Obtiene una lista de todos los temas registrados.
     * @return lista de objetos TemaForoDTO
     */
    List<TemaForoDTO> findAll();

    /**
     * Busca un tema específico por su identificador.
     * @param id identificador del tema
     * @return objeto TemaForoDTO correspondiente
     */
    TemaForoDTO findById(Long id);

    /**
     * Crea un nuevo tema en un foro.
     * @param dto DTO con los datos del tema a crear
     * @return objeto TemaForoDTO del tema creado
     */
    TemaForoDTO create(TemaForoCreateDTO dto);

    /**
     * Actualiza los datos de un tema existente.
     * @param id identificador del tema a modificar
     * @param dto DTO con los nuevos datos
     * @return objeto TemaForoDTO actualizado
     */
    TemaForoDTO update(Long id, TemaForoUpdateDTO dto);

    /**
     * Elimina un tema del sistema.
     * @param id identificador del tema a eliminar
     */
    void delete(Long id);
}
