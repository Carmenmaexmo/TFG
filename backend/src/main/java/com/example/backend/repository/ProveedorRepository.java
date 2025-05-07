package com.example.backend.repository;

import com.example.backend.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    boolean existsByNombre(String nombre);
    boolean existsByEmail(String email);

    // Para el update
    boolean existsByNombreAndIdNot(String nombre, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
}
