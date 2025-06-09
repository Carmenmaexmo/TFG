package com.example.backend.repository;

import com.example.backend.model.Vinilo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad Vinilo.
 * Proporciona operaciones CRUD y validaciones personalizadas.
 */
public interface ViniloRepository extends JpaRepository<Vinilo, Long> {

    /**
     * Verifica si existe un vinilo con el título dado.
     *
     * @param titulo título del vinilo.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByTitulo(String titulo);

    /**
     * Verifica si existe otro vinilo (excluyendo uno específico por ID) con el mismo título.
     *
     * @param titulo título a comprobar.
     * @param id ID del vinilo a excluir.
     * @return true si existe otro con el mismo título, false si no.
     */
    boolean existsByTituloAndIdNot(String titulo, Long id);

    /**
     * Verifica si existe un vinilo con ese título y proveedor.
     *
     * @param titulo título del vinilo.
     * @param proveedorId ID del proveedor.
     * @return true si ya existe uno igual, false si no.
     */
    boolean existsByTituloAndProveedorId(String titulo, Long proveedorId);

    /**
     * Verifica si existe otro vinilo con el mismo título y proveedor, excluyendo uno específico.
     *
     * @param titulo título a verificar.
     * @param proveedorId ID del proveedor.
     * @param id ID del vinilo a excluir.
     * @return true si existe otro duplicado, false si no.
     */
    boolean existsByTituloAndProveedorIdAndIdNot(String titulo, Long proveedorId, Long id);
}
