package com.example.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.web.filter.OncePerRequestFilter;
import com.example.backend.service.CustomUserDetailsService;


@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws java.io.IOException, jakarta.servlet.ServletException {
                          System.out.println("🛡️ Filtro ejecutado");               
        String header = req.getHeader("Authorization");
    
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("🔐 Token recibido: " + token);
    
            if (jwtUtils.validateJwtToken(token)) {
                System.out.println("✅ Token válido");
    
                String nombreUsuario = jwtUtils.getNombreUsuarioFromJwt(token);
                System.out.println("👤 Usuario extraído del token: " + nombreUsuario);
    
                var userDetails = userDetailsService.loadUserByUsername(nombreUsuario);
                System.out.println("🎭 Roles cargados: " + userDetails.getAuthorities());
    
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
    
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                System.out.println("❌ Token inválido");
            }
        } else {
            System.out.println("⚠️ No se encontró token en la cabecera Authorization");
        }
    
        chain.doFilter(req, res);
    }
    
}
