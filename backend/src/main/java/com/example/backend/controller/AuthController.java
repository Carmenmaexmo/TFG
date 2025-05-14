package com.example.backend.controller;

import com.example.backend.model.Usuario;
import com.example.backend.payload.request.LoginRequest;
import com.example.backend.payload.request.SignupRequest;
import com.example.backend.payload.response.JwtResponse;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest r) {
        // 1) Autenticar con nombreUsuario + password
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                r.getNombreUsuario(),
                r.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 2) Generar JWT
        String token = jwtUtils.generateJwtToken(auth);

        // 3) Sacar datos del principal
        var userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        String username = userDetails.getUsername();
        List<String> roles = userDetails.getAuthorities()
            .stream()
            .map(a -> a.getAuthority())
            .toList();

        // 4) Devolver token, usuario y roles
        return ResponseEntity.ok(
            new JwtResponse(token, username, roles)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest r) {
        if (usuarioRepo.existsByNombreUsuario(r.getNombreUsuario())) {
            return ResponseEntity
                .badRequest()
                .body("Error: el nombre de usuario ya existe");
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
        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}
