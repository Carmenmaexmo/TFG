package com.example.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador global para manejar excepciones lanzadas por el backend.
 * Este handler captura excepciones del tipo ResponseStatusException
 * y construye una respuesta JSON estructurada para facilitar el
 * tratamiento de errores desde el frontend.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo ResponseStatusException.
     * 
     * @param ex La excepción lanzada.
     * @param request La solicitud HTTP que causó la excepción.
     * @return ResponseEntity con detalles del error.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        int statusCode = ex.getStatusCode().value();
        HttpStatus resolved = HttpStatus.resolve(statusCode);

        // Construcción del cuerpo de la respuesta
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now()); // Marca temporal del error
        body.put("status", statusCode);       // Código de estado HTTP
        body.put("error", resolved != null
                ? resolved.getReasonPhrase()  // Descripción del estado
                : String.valueOf(statusCode));
        body.put("message", ex.getReason());  // Mensaje de error definido en la excepción
        body.put("path", request.getRequestURI()); // Ruta que causó el error

        return ResponseEntity
                .status(statusCode)
                .body(body);
    }
}
