package com.example.backend.service;

import com.example.backend.dto.DireccionEnvioDTO;
import com.example.backend.dto.DireccionEnvioCreateDTO;

import java.util.List;

/**
 * Interfaz para la lógica de negocio relacionada con direcciones de envío.
 * Define las operaciones CRUD y búsquedas relacionadas con direcciones de usuarios.
 */
public interface DireccionEnvioService {

    /**
     * Devuelve una lista con todas las direcciones de envío del sistema.
     * @return lista de DireccionEnvioDTO
     */
    List<DireccionEnvioDTO> findAll();

    /**
     * Busca una dirección de envío por su ID.
     * @param id identificador de la dirección
     * @return objeto DireccionEnvioDTO correspondiente
     */
    DireccionEnvioDTO findById(Long id);

    /**
     * Crea una nueva dirección de envío a partir del DTO recibido.
     * @param dto objeto con los datos necesarios para crear una dirección
     * @return dirección de envío creada
     */
    DireccionEnvioDTO create(DireccionEnvioCreateDTO dto);

    /**
     * Actualiza una dirección de envío existente con los datos proporcionados.
     * @param id identificador de la dirección a actualizar
     * @param dto datos actualizados
     * @return dirección de envío actualizada
     */
    DireccionEnvioDTO update(Long id, DireccionEnvioCreateDTO dto);

    /**
     * Elimina una dirección de envío por su ID.
     * @param id identificador de la dirección a eliminar
     */
    void delete(Long id);

    /**
     * Busca todas las direcciones de envío asociadas a un usuario específico.
     * @param idUsuario identificador del usuario
     * @return lista de direcciones de ese usuario
     */
    List<DireccionEnvioDTO> findByUsuario(Long idUsuario);
}
