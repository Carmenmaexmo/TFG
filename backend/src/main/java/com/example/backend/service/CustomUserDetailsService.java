package com.example.backend.service;

import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Implementación personalizada de UserDetailsService que permite
 * la autenticación de usuarios a partir del nombre de usuario.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    /**
     * Carga un usuario por su nombre de usuario (username).
     * Este método es utilizado por Spring Security durante el proceso de autenticación.
     *
     * @param nombreUsuario nombre de usuario del sistema
     * @return UserDetails con los datos de autenticación y roles
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String nombreUsuario)
            throws UsernameNotFoundException {

        // 1. Buscar el usuario en la base de datos mediante su nombre de usuario
        Usuario u = usuarioRepo.findByNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 2. Obtener el rol desde la base de datos y formatearlo como autoridad (Spring requiere el prefijo "ROLE_")
        String rolBd = u.getRol(); // Ejemplo: "CLIENTE"
        String autoridad = "ROLE_" + rolBd.toUpperCase(); // Resultado: "ROLE_CLIENTE"

        // 3. Construir un objeto UserDetails con los datos del usuario y sus autoridades
        return User.builder()
                .username(u.getNombreUsuario())
                .password(u.getPassword()) // La contraseña ya debe estar encriptada (hashed)
                .authorities(new SimpleGrantedAuthority(autoridad))
                .build();
    }

    /**
     * Método adicional (aún no implementado) para cargar un usuario por su ID.
     * Podría ser útil en casos donde se quiera autenticar o recuperar información directamente por ID.
     *
     * @param idUsuario identificador del usuario
     * @return objeto UserDetails correspondiente
     */
    public Object loadUserById(Long idUsuario) {
        // Método no implementado
        throw new UnsupportedOperationException("Unimplemented method 'loadUserById'");
    }

}
