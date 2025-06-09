package com.example.backend.controller;

import com.example.backend.model.Usuario;
import com.example.backend.payload.request.LoginRequest;
import com.example.backend.payload.request.SignupRequest;
import com.example.backend.payload.response.JwtResponse;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.security.JwtUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador encargado de gestionar la autenticación de usuarios.
 * Incluye login y registro.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para login y registro de usuarios")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica al usuario y devuelve un token JWT junto con su nombre de usuario, roles e ID"
    )
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest r) {
        // Autenticación del usuario con nombre y contraseña
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                r.getNombreUsuario(),
                r.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Usuario usuario = usuarioRepo.findByNombreUsuario(r.getNombreUsuario())
            .orElseThrow(() -> new UsernameNotFoundException("No existe"));

        Long idUsuario = usuario.getIdUsuario(); 
        String token = jwtUtils.generateJwtToken(auth);

        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        String username = userDetails.getUsername();
        List<String> roles = userDetails.getAuthorities()
            .stream()
            .map(a -> a.getAuthority())
            .toList();

        return ResponseEntity.ok(new JwtResponse(token, username, roles, idUsuario));
    }

    @PostMapping("/signup")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea un nuevo usuario con rol CLIENTE. Verifica si el nombre de usuario o email ya están en uso"
    )
    public ResponseEntity<?> signup(@RequestBody SignupRequest r) {
        if (usuarioRepo.existsByNombreUsuario(r.getNombreUsuario())) {
            return ResponseEntity
                .status(409)
                .body(Map.of("message", "El nombre de usuario ya existe"));
        }
        if (usuarioRepo.existsByEmail(r.getEmail())) {
            return ResponseEntity
                .status(409)
                .body(Map.of("message", "El email ya está registrado"));
        }        

        Usuario u = new Usuario();
        u.setNombreUsuario(r.getNombreUsuario());
        u.setPassword(encoder.encode(r.getPassword()));
        u.setEmail(r.getEmail());
        u.setNombre(r.getNombre());
        u.setApellidos(r.getApellidos());
        u.setTelefono(r.getTelefono());
        u.setDni(r.getDni());
        u.setRol("CLIENTE");

        usuarioRepo.save(u);
        return ResponseEntity.ok(Map.of("message", "Usuario registrado correctamente"));
    }
}
