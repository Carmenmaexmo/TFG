package com.example.backend.repository;

import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByNombreUsuario(String nombreUsuario);
    
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    boolean existsByNombreUsuarioAndIdUsuarioNot(String nombreUsuario, Long idUsuario);
    boolean existsByEmailAndIdUsuarioNot(String email, Long idUsuario);
}
