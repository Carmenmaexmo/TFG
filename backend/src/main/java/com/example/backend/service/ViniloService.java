package com.example.backend.service;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloUpdateDTO;
import com.example.backend.dto.ViniloCreateDTO;

import java.util.List;

/**
 * Interfaz del servicio de gestión de vinilos.
 * Define las operaciones CRUD disponibles para la entidad Vinilo.
 */
public interface ViniloService {

    /**
     * Obtiene la lista de todos los vinilos disponibles.
     * @return lista de objetos ViniloDTO
     */
    List<ViniloDTO> findAll();

    /**
     * Busca un vinilo por su identificador.
     * @param id identificador del vinilo
     * @return objeto ViniloDTO correspondiente
     */
    ViniloDTO findById(Long id);

    /**
     * Crea un nuevo vinilo.
     * @param dto DTO con los datos del nuevo vinilo
     * @return objeto ViniloDTO creado
     */
    ViniloDTO create(ViniloCreateDTO dto);

    /**
     * Actualiza un vinilo existente.
     * @param id identificador del vinilo a actualizar
     * @param dto DTO con los nuevos datos
     * @return objeto ViniloDTO actualizado
     */
    ViniloDTO update(Long id, ViniloUpdateDTO dto);

    /**
     * Elimina un vinilo del sistema.
     * @param id identificador del vinilo a eliminar
     */
    void delete(Long id);
}
