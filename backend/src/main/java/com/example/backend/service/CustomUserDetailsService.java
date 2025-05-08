package com.example.backend.service;

import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario)
            throws UsernameNotFoundException {
        // 1) Carga del usuario
        Usuario u = usuarioRepo.findByNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 2) Convierte tu campo rol ("CLIENTE", "ADMINISTRADOR", ...) en GrantedAuthority
        String rolBd = u.getRol(); // p.e. "CLIENTE"
        String autoridad = "ROLE_" + rolBd.toUpperCase();

        // 3) Construye el UserDetails con la autoridad real
        return User.builder()
                .username(u.getNombreUsuario())
                .password(u.getPassword())      // ya está hasheada
                .authorities(new SimpleGrantedAuthority(autoridad))
                .build();
    }
}
