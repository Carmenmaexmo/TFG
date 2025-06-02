package com.example.backend.service.impl;

import com.example.backend.dto.UsuarioCreateDTO;
import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.UsuarioMapper;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;  // inyectado
    

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }
    
    @Override
    public UsuarioDTO create(UsuarioCreateDTO dto) {
        // Validación manual de campos
        String nombreUsuario = dto.getNombreUsuario().trim();
        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de usuario ya existe");
        }
        String dni = dto.getDni().trim().toUpperCase();
        if (!validarDni(dni)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI inválido");
        }
        String email = dto.getEmail().trim();
        if (!validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido");
        }
        String telefono = dto.getTelefono().trim();
        if (!validarTelefono(telefono)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teléfono inválido");
        }
        String rol = dto.getRol().trim().toUpperCase();
        if (!rol.equals("ADMINISTRADOR") &&
            !rol.equals("EMPLEADO")      &&
            !rol.equals("CLIENTE")       &&
            !rol.equals("MODERADOR")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Rol inválido. Debe ser ADMINISTRADOR, EMPLEADO, CLIENTE o MODERADOR"
            );
        }

        // Mapeo y cifrado de contraseña
        Usuario usuario = usuarioMapper.fromCreateDTO(dto);
        usuario.setDni(dni);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setRol(rol);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
    

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

    @Override
    public UsuarioDTO partialUpdate(Long id, UsuarioUpdateDTO dto) {
        Usuario u = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // 1) nombreUsuario
        if (dto.getNombreUsuario() != null
            && !dto.getNombreUsuario().equals(u.getNombreUsuario())) {

            if (usuarioRepository.existsByNombreUsuarioAndIdUsuarioNot(
                    dto.getNombreUsuario(), id)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre de usuario ya existe"
                );
            }
            u.setNombreUsuario(dto.getNombreUsuario());
        }

        // 2) nombre
        if (dto.getNombre() != null) {
            u.setNombre(dto.getNombre());
        }

        // 3) apellidos
        if (dto.getApellidos() != null) {
            u.setApellidos(dto.getApellidos());
        }

        // 4) email
        if (dto.getEmail() != null
            && !dto.getEmail().equals(u.getEmail())) {

            if (!validarEmail(dto.getEmail())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email inválido"
                );
            }
            if (usuarioRepository.existsByEmailAndIdUsuarioNot(
                    dto.getEmail(), id)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El email ya está registrado"
                );
            }
            u.setEmail(dto.getEmail());
        }

        // 5) teléfono
        if (dto.getTelefono() != null) {
            if (!validarTelefono(dto.getTelefono())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Teléfono inválido"
                );
            }
            u.setTelefono(dto.getTelefono());
        }

        // 6) dni
        if (dto.getDni() != null) {
            String dni = dto.getDni().trim().toUpperCase();
            if (!validarDni(dni)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "DNI inválido"
                );
            }
            u.setDni(dni);
        }

        // 7) rol
        if (dto.getRol() != null) {
            String rol = dto.getRol().trim().toUpperCase();
            if (!rol.equals("ADMINISTRADOR") &&
                !rol.equals("EMPLEADO")      &&
                !rol.equals("CLIENTE")       &&
                !rol.equals("MODERADOR")) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rol inválido. Debe ser ADMINISTRADOR, EMPLEADO, CLIENTE o MODERADOR"
                );
            }
            u.setRol(rol);
        }

        // 8) password
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 9) carrito
        if (dto.getCarrito() != null) {
            u.setCarrito(dto.getCarrito());
        }

        // 10) guardas y devuelves el DTO
        Usuario actualizado = usuarioRepository.save(u);
        return usuarioMapper.toDTO(actualizado);
    }


    // Validadores de DNI, email y teléfono
    private boolean validarDni(String dni) {
        if (dni == null) return false;
        String d = dni.trim().toUpperCase();
        if (!d.matches("\\d{8}[A-Z]")) return false;
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numero = Integer.parseInt(d.substring(0, 8));
        return d.charAt(8) == letras.charAt(numero % 23);
    }

    private boolean validarEmail(String email) {
        if (email == null) return false;
        // patrón simple: algo@algo.algo
        return email.trim().matches("^[\\w.+\\-]+@[\\w\\-]+\\.[A-Za-z]{2,}$");
    }

    private boolean validarTelefono(String telefono) {
        if (telefono == null) return false;
        String t = telefono.trim();
        // España: 9 dígitos, empieza por 6,7,8 o 9
        return t.matches("^[6789]\\d{8}$");
    }

    @Override
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no existe");
        }
        usuarioRepository.deleteById(id);
       
    }
}
