// src/main/java/com/example/backend/repository/ComentarioForoRepository.java
package com.example.backend.repository;

import com.example.backend.model.ComentarioForo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioForoRepository extends JpaRepository<ComentarioForo, Long> {
}
