package com.example.backend.service;

import com.example.backend.dto.ForoDTO;
import com.example.backend.dto.ForoUpdateDTO;
import com.example.backend.dto.ForoCreateDTO;

import java.util.List;

/**
 * Interfaz que define los métodos del servicio para la gestión de foros.
 * Proporciona operaciones CRUD básicas.
 */
public interface ForoService {

    /**
     * Recupera una lista de todos los foros existentes.
     * @return lista de objetos ForoDTO
     */
    List<ForoDTO> findAll();

    /**
     * Busca un foro por su identificador único.
     * @param id identificador del foro
     * @return objeto ForoDTO correspondiente
     */
    ForoDTO findById(Long id);

    /**
     * Crea un nuevo foro a partir de los datos recibidos.
     * @param dto DTO con la información del nuevo foro
     * @return foro creado
     */
    ForoDTO create(ForoCreateDTO dto);

    /**
     * Actualiza un foro existente con los datos proporcionados.
     * @param id identificador del foro a modificar
     * @param dto DTO con los nuevos datos
     * @return foro actualizado
     */
    ForoDTO update(Long id, ForoUpdateDTO dto);

    /**
     * Elimina un foro por su identificador.
     * @param id identificador del foro a eliminar
     */
    void delete(Long id);
}
