package com.example.backend.repository;

import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario.
 * Proporciona operaciones CRUD y consultas personalizadas sobre usuarios.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su email.
     *
     * @param email email del usuario.
     * @return Optional con el usuario si existe.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si ya existe un usuario con el email proporcionado.
     *
     * @param email email a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si ya existe un usuario con el nombre de usuario especificado.
     *
     * @param nombreUsuario nombre de usuario a comprobar.
     * @return true si ya existe, false en caso contrario.
     */
    boolean existsByNombreUsuario(String nombreUsuario);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario a buscar.
     * @return Optional con el usuario si existe.
     */
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    /**
     * Verifica si ya existe un usuario con el DNI proporcionado.
     *
     * @param dni DNI a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByDni(String dni);

    /**
     * Verifica si ya existe otro usuario (distinto al especificado por id) con el mismo nombre de usuario.
     * Utilizado para validaciones durante actualizaciones.
     *
     * @param nombreUsuario nuevo nombre de usuario.
     * @param idUsuario ID del usuario actual (a excluir de la comprobación).
     * @return true si existe otro usuario con ese nombre de usuario, false en caso contrario.
     */
    boolean existsByNombreUsuarioAndIdUsuarioNot(String nombreUsuario, Long idUsuario);

    /**
     * Verifica si ya existe otro usuario (distinto al especificado por id) con el mismo email.
     * Utilizado para validaciones durante actualizaciones.
     *
     * @param email nuevo email.
     * @param idUsuario ID del usuario actual (a excluir de la comprobación).
     * @return true si existe otro usuario con ese email, false en caso contrario.
     */
    boolean existsByEmailAndIdUsuarioNot(String email, Long idUsuario);
}
