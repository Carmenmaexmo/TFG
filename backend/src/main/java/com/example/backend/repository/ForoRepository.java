// src/main/java/com/example/backend/repository/ForoRepository.java
package com.example.backend.repository;

import com.example.backend.model.Foro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForoRepository extends JpaRepository<Foro, Long> {
}
