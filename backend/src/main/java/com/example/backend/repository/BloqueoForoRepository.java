// src/main/java/com/example/backend/repository/BloqueoForoRepository.java
package com.example.backend.repository;

import com.example.backend.model.BloqueoForo;
import com.example.backend.model.Foro;
import com.example.backend.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BloqueoForoRepository extends JpaRepository<BloqueoForo, Long> {

    boolean existsByUsuarioAndForo(Usuario usuario, Foro foro);
}
