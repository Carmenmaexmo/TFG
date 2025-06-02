package com.example.backend.repository;

import com.example.backend.model.DireccionEnvio;
import com.example.backend.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long> {

    List<DireccionEnvio> findByUsuario(Usuario usuario);
}
