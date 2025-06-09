package com.example.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada personalizado para manejar errores de autenticación.
 * Se activa cuando se intenta acceder a un recurso protegido sin estar autenticado.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Maneja las solicitudes no autorizadas enviando un error HTTP 401 (Unauthorized).
     *
     * @param request solicitud HTTP entrante
     * @param response respuesta HTTP
     * @param authException excepción lanzada durante el intento de autenticación
     * @throws IOException en caso de fallo al escribir la respuesta
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado");
    }
}
