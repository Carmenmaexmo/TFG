package com.example.backend.service;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.model.Usuario;
import com.example.backend.dto.UsuarioCreateDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de gestión de usuarios.
 * Define las operaciones disponibles para manipular datos de usuarios.
 */
public interface UsuarioService {

    /**
     * Devuelve una lista de todos los usuarios registrados.
     * @return lista de objetos UsuarioDTO
     */
    List<UsuarioDTO> findAll();

    /**
     * Busca un usuario por su identificador único.
     * @param id identificador del usuario
     * @return objeto UsuarioDTO correspondiente
     */
    UsuarioDTO findById(Long id);

    /**
     * Crea un nuevo usuario en el sistema.
     * @param dto DTO con los datos necesarios para la creación
     * @return objeto UsuarioDTO creado
     */
    UsuarioDTO create(UsuarioCreateDTO dto);

    /**
     * Actualiza parcialmente los datos de un usuario existente.
     * @param id identificador del usuario a actualizar
     * @param dto DTO con los campos a modificar
     * @return objeto UsuarioDTO actualizado
     */
    UsuarioDTO partialUpdate(Long id, UsuarioUpdateDTO dto);

    /**
     * Elimina un usuario del sistema por su identificador.
     * @param id identificador del usuario a eliminar
     */
    void delete(Long id);

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username nombre de usuario (único)
     * @return objeto Usuario (opcional)
     */
    Optional<Usuario> findByNombreUsuario(String username);
}
