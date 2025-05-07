// src/main/java/com/example/backend/repository/TemaForoRepository.java
package com.example.backend.repository;

import com.example.backend.model.TemaForo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemaForoRepository extends JpaRepository<TemaForo, Long> {
    boolean existsByTituloAndForoId(String titulo, Long foroId);
}
