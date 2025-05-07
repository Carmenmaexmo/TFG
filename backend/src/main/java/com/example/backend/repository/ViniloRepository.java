package com.example.backend.repository;

import com.example.backend.model.Vinilo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViniloRepository extends JpaRepository<Vinilo, Long> {
    boolean existsByTitulo(String titulo);
    boolean existsByTituloAndIdNot(String titulo, Long id);
    boolean existsByTituloAndProveedorId(String titulo, Long proveedorId);
    boolean existsByTituloAndProveedorIdAndIdNot(String titulo, Long proveedorId, Long id);
}
