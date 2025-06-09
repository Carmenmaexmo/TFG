package com.example.backend.service;

import com.example.backend.dto.EventoDTO;
import com.example.backend.dto.EventoUpdateDTO;
import com.example.backend.dto.EventoCreateDTO;

import java.util.List;

/**
 * Interfaz que define los métodos del servicio para la gestión de eventos.
 * Incluye operaciones CRUD estándar.
 */
public interface EventoService {

    /**
     * Devuelve la lista de todos los eventos registrados en el sistema.
     * @return lista de objetos EventoDTO
     */
    List<EventoDTO> findAll();

    /**
     * Busca un evento específico por su identificador.
     * @param id identificador del evento
     * @return objeto EventoDTO correspondiente
     */
    EventoDTO findById(Long id);

    /**
     * Crea un nuevo evento a partir de los datos recibidos.
     * @param dto DTO con la información necesaria para crear el evento
     * @return evento creado
     */
    EventoDTO create(EventoCreateDTO dto);

    /**
     * Actualiza un evento existente con los nuevos datos proporcionados.
     * @param id identificador del evento a actualizar
     * @param dto DTO con los datos a modificar
     * @return evento actualizado
     */
    EventoDTO update(Long id, EventoUpdateDTO dto);

    /**
     * Elimina un evento por su identificador.
     * @param id identificador del evento a eliminar
     */
    void delete(Long id);
}
