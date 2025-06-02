// src/main/java/com/example/backend/repository/ComentarioForoRepository.java
package com.example.backend.repository;

import com.example.backend.model.ComentarioForo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioForoRepository extends JpaRepository<ComentarioForo, Long> {
    List<ComentarioForo> findByComentarioPadre_Id(Long padreId);

    List<ComentarioForo> findByTemaId(Long idTema);

}
