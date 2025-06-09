package com.example.backend.repository;

import com.example.backend.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad Proveedor.
 * Proporciona operaciones CRUD y consultas personalizadas relacionadas con proveedores.
 */
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /**
     * Verifica si ya existe un proveedor con el nombre especificado.
     *
     * @param nombre nombre del proveedor.
     * @return true si existe un proveedor con ese nombre, false en caso contrario.
     */
    boolean existsByNombre(String nombre);

    /**
     * Verifica si ya existe un proveedor con el email especificado.
     *
     * @param email correo electrónico del proveedor.
     * @return true si existe un proveedor con ese email, false en caso contrario.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si ya existe un proveedor con el mismo nombre, excluyendo el proveedor con el ID dado.
     * Utilizado principalmente durante la actualización para evitar conflictos de nombre duplicado.
     *
     * @param nombre nombre del proveedor.
     * @param id     ID del proveedor actual (a excluir de la comprobación).
     * @return true si hay otro proveedor con el mismo nombre, false en caso contrario.
     */
    boolean existsByNombreAndIdNot(String nombre, Long id);

    /**
     * Verifica si ya existe un proveedor con el mismo email, excluyendo el proveedor con el ID dado.
     * Utilizado durante la actualización para evitar conflictos de email duplicado.
     *
     * @param email correo electrónico del proveedor.
     * @param id    ID del proveedor actual (a excluir de la comprobación).
     * @return true si hay otro proveedor con el mismo email, false en caso contrario.
     */
    boolean existsByEmailAndIdNot(String email, Long id);
}
