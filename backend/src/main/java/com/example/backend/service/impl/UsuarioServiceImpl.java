// Implementación del servicio de gestión de usuarios.
// Este servicio se encarga de las operaciones CRUD sobre usuarios, incluyendo validaciones,
// encriptación de contraseñas y verificación de reglas de negocio.
package com.example.backend.service.impl;

import com.example.backend.dto.*;
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
@RequiredArgsConstructor // Inyecta los repositorios y utilidades marcadas como final
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;  // Utilizado para encriptar contraseñas

    /**
     * Obtiene todos los usuarios del sistema en formato DTO.
     */
    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    /**
     * Busca un usuario por su ID. Lanza excepción si no existe.
     */
    @Override
    public UsuarioDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    /**
     * Busca un usuario por su nombre de usuario (sin DTO).
     */
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    /**
     * Crea un nuevo usuario validando todos los campos críticos:
     * nombre de usuario, DNI, email, teléfono, rol y contraseña.
     * Encripta la contraseña antes de guardar.
     */
    @Override
    public UsuarioDTO create(UsuarioCreateDTO dto) {
        // Validaciones manuales
        String nombreUsuario = dto.getNombreUsuario().trim();
        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de usuario ya existe");
        }

        String dni = dto.getDni().trim().toUpperCase();
        if (!validarDni(dni)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI inválido");
        }

        if (usuarioRepository.existsByDni(dni)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI ya registrado");
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
            !rol.equals("EMPLEADO") &&
            !rol.equals("CLIENTE") &&
            !rol.equals("MODERADOR")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Rol inválido. Debe ser ADMINISTRADOR, EMPLEADO, CLIENTE o MODERADOR");
        }

        // Mapeo y codificación de la contraseña
        Usuario usuario = usuarioMapper.fromCreateDTO(dto);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setDni(dni);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setRol(rol);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword())); // encriptación segura

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(guardado);
    }

    /**
     * Actualización parcial de un usuario. Solo actualiza los campos presentes en el DTO.
     * Incluye validaciones de unicidad, formatos y encriptación si se cambia la contraseña.
     */
    @Override
    public UsuarioDTO partialUpdate(Long id, UsuarioUpdateDTO dto) {
        Usuario u = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // Validación y cambio de nombre de usuario
        if (dto.getNombreUsuario() != null &&
            !dto.getNombreUsuario().equals(u.getNombreUsuario())) {
            if (usuarioRepository.existsByNombreUsuarioAndIdUsuarioNot(dto.getNombreUsuario(), id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El nombre de usuario ya existe");
            }
            u.setNombreUsuario(dto.getNombreUsuario());
        }

        // Nombre y apellidos
        if (dto.getNombre() != null) u.setNombre(dto.getNombre());
        if (dto.getApellidos() != null) u.setApellidos(dto.getApellidos());

        // Validación de email
        if (dto.getEmail() != null &&
            !dto.getEmail().equals(u.getEmail())) {
            if (!validarEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido");
            }
            if (usuarioRepository.existsByEmailAndIdUsuarioNot(dto.getEmail(), id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
            }
            u.setEmail(dto.getEmail());
        }

        // Validación de teléfono
        if (dto.getTelefono() != null) {
            if (!validarTelefono(dto.getTelefono())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teléfono inválido");
            }
            u.setTelefono(dto.getTelefono());
        }

        // Validación de DNI
        if (dto.getDni() != null) {
            String dni = dto.getDni().trim().toUpperCase();
            if (!validarDni(dni)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI inválido");
            }
            if (usuarioRepository.existsByDni(dni)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DNI ya registrado");
            }
            u.setDni(dni);
        }

        // Rol
        if (dto.getRol() != null) {
            String rol = dto.getRol().trim().toUpperCase();
            if (!rol.equals("ADMINISTRADOR") &&
                !rol.equals("EMPLEADO") &&
                !rol.equals("CLIENTE") &&
                !rol.equals("MODERADOR")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rol inválido. Debe ser ADMINISTRADOR, EMPLEADO, CLIENTE o MODERADOR");
            }
            u.setRol(rol);
        }

        // Cambio de contraseña
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Carrito (campo especial si se utiliza)
        if (dto.getCarrito() != null) {
            u.setCarrito(dto.getCarrito());
        }

        Usuario actualizado = usuarioRepository.save(u);
        return usuarioMapper.toDTO(actualizado);
    }

    /**
     * Elimina un usuario si existe. Lanza excepción si no se encuentra.
     */
    @Override
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no existe");
        }
        usuarioRepository.deleteById(id);
    }

    // --------------------
    // Validadores auxiliares
    // --------------------

    // Valida formato de DNI español (8 números + letra de control)
    private boolean validarDni(String dni) {
        if (dni == null) return false;
        String d = dni.trim().toUpperCase();
        if (!d.matches("\\d{8}[A-Z]")) return false;
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numero = Integer.parseInt(d.substring(0, 8));
        return d.charAt(8) == letras.charAt(numero % 23);
    }

    // Valida un email básico con regex
    private boolean validarEmail(String email) {
        if (email == null) return false;
        return email.trim().matches("^[\\w.+\\-]+@[\\w\\-]+\\.[A-Za-z]{2,}$");
    }

    // Valida un teléfono español de 9 dígitos que comienza por 6, 7, 8 o 9
    private boolean validarTelefono(String telefono) {
        if (telefono == null) return false;
        String t = telefono.trim();
        return t.matches("^[6789]\\d{8}$");
    }
}
