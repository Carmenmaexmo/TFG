package com.example.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Utilidad para gestionar tokens JWT: generación, validación y extracción de datos.
 */
@Component
public class JwtUtils {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    /** Genera la clave de firma HMAC a partir del secreto configurado */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un JWT para el usuario autenticado, incluyendo sus roles y ID.
     */
    public String generateJwtToken(org.springframework.security.core.Authentication auth) {
        String nombreUsuario = auth.getName();

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<String> authorities = auth.getAuthorities().stream()
            .map(a -> a.getAuthority())  // ej: ROLE_CLIENTE
            .toList();

        Date now = new Date();

        return Jwts.builder()
            .setSubject(nombreUsuario) // nombreUsuario como "subject"
            .claim("idUsuario", usuario.getIdUsuario())
            .claim("roles", authorities)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + jwtExpirationMs))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     */
    public String getNombreUsuarioFromJwt(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    /**
     * Valida la firma y estructura del token JWT.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // token inválido o expirado
        }
    }

    /**
     * Extrae el ID del usuario desde los claims del JWT.
     */
    public Long getIdUsuarioFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();

        return Long.valueOf(claims.get("idUsuario").toString());
    }

    /**
     * Devuelve todos los claims del JWT (por si necesitas acceder a más datos).
     */
    public Claims getAllClaimsFromJwt(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Extrae y devuelve la lista de roles del token.
     */
    public List<String> getRolesFromJwt(String token) {
        Claims claims = getAllClaimsFromJwt(token);
        Object rolesObj = claims.get("roles");

        if (rolesObj instanceof List<?> rawList) {
            return rawList.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .toList();
        }

        return List.of(); // Si no hay roles o están mal formateados
    }
}
