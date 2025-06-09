package com.example.backend.service;

import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.dto.AsistenciaEventoUpdateDTO;
import com.example.backend.dto.AsistenciaEventoCreateDTO;

import java.util.List;

/**
 * Servicio para gestionar la lógica relacionada con las asistencias a eventos.
 */
public interface AsistenciaEventoService {

    /**
     * Devuelve una lista con todas las asistencias registradas.
     * 
     * @return lista de AsistenciaEventoDTO
     */
    List<AsistenciaEventoDTO> findAll();

    /**
     * Devuelve una asistencia concreta a partir de su ID.
     * 
     * @param id identificador de la asistencia
     * @return asistencia encontrada como DTO
     */
    AsistenciaEventoDTO findById(Long id);

    /**
     * Crea una nueva asistencia a evento.
     * 
     * @param dto objeto con los datos para crear la asistencia
     * @return asistencia creada como DTO
     */
    AsistenciaEventoDTO create(AsistenciaEventoCreateDTO dto);

    /**
     * Actualiza una asistencia existente (por ejemplo, confirmación).
     * 
     * @param id identificador de la asistencia
     * @param dto objeto con los datos actualizados
     * @return asistencia modificada como DTO
     */
    AsistenciaEventoDTO update(Long id, AsistenciaEventoUpdateDTO dto);

    /**
     * Elimina una asistencia a evento por su ID.
     * 
     * @param id identificador de la asistencia a eliminar
     */
    void delete(Long id);

    /**
     * Marca como confirmada una asistencia específica.
     * 
     * @param id identificador de la asistencia
     * @return asistencia actualizada con el campo confirmado en true
     */
    AsistenciaEventoDTO confirmar(Long id);

    /**
     * Busca todas las asistencias de un evento concreto.
     * 
     * @param idEvento identificador del evento
     * @return lista de asistencias correspondientes a ese evento
     */
    List<AsistenciaEventoDTO> findByEvento(Long idEvento);
}
