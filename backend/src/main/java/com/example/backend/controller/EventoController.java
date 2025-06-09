// src/main/java/com/example/backend/controller/EventoController.java
package com.example.backend.controller;

import com.example.backend.dto.EventoCreateDTO;
import com.example.backend.dto.EventoDTO;
import com.example.backend.dto.EventoUpdateDTO;
import com.example.backend.service.EventoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de eventos.
 */
@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Eventos", description = "Operaciones relacionadas con la creación, consulta, actualización y eliminación de eventos")
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    @Operation(summary = "Listar eventos", description = "Devuelve todos los eventos disponibles")
    public List<EventoDTO> getAll() {
        return eventoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID", description = "Obtiene los datos de un evento específico mediante su ID")
    public EventoDTO getById(@PathVariable Long id) {
        return eventoService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear evento", description = "Registra un nuevo evento")
    public EventoDTO create(@RequestBody EventoCreateDTO dto) {
        return eventoService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento", description = "Modifica los datos de un evento existente mediante su ID")
    public EventoDTO update(
            @PathVariable Long id,
            @RequestBody EventoUpdateDTO dto
    ) {
        return eventoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento", description = "Elimina un evento existente mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
