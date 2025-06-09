package com.example.backend.service;

import com.example.backend.dto.ProveedorDTO;
import com.example.backend.dto.ProveedorUpdateDTO;
import com.example.backend.dto.ProveedorCreateDTO;

import java.util.List;

/**
 * Interfaz del servicio para la gestión de proveedores.
 * Define las operaciones CRUD disponibles para proveedores.
 */
public interface ProveedorService {

    /**
     * Recupera todos los proveedores registrados.
     * @return lista de objetos ProveedorDTO
     */
    List<ProveedorDTO> findAll();

    /**
     * Busca un proveedor por su identificador único.
     * @param id identificador del proveedor
     * @return objeto ProveedorDTO correspondiente
     */
    ProveedorDTO findById(Long id);

    /**
     * Crea un nuevo proveedor a partir de los datos recibidos.
     * @param dto DTO con los datos del nuevo proveedor
     * @return proveedor creado
     */
    ProveedorDTO create(ProveedorCreateDTO dto);

    /**
     * Actualiza un proveedor existente con nuevos datos.
     * @param id identificador del proveedor a modificar
     * @param dto DTO con los nuevos datos del proveedor
     * @return proveedor actualizado
     */
    ProveedorDTO update(Long id, ProveedorUpdateDTO dto);

    /**
     * Elimina un proveedor por su identificador.
     * @param id identificador del proveedor a eliminar
     */
    void delete(Long id);
}
