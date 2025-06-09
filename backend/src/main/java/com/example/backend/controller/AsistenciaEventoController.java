// src/main/java/com/example/backend/controller/AsistenciaEventoController.java
package com.example.backend.controller;

import com.example.backend.dto.AsistenciaEventoCreateDTO;
import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.dto.AsistenciaEventoUpdateDTO;
import com.example.backend.service.AsistenciaEventoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de asistencias a eventos.
 */
@RestController
@RequestMapping("/api/asistencias-evento")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Asistencias a Eventos", description = "Operaciones relacionadas con el control de asistencias a eventos")
public class AsistenciaEventoController {

    private final AsistenciaEventoService asistenciaService;

    @GetMapping
    @Operation(summary = "Listar asistencias", description = "Devuelve una lista con todas las asistencias registradas a eventos")
    public List<AsistenciaEventoDTO> getAll() {
        return asistenciaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar asistencia por ID", description = "Obtiene una asistencia específica mediante su ID")
    public AsistenciaEventoDTO getById(@PathVariable Long id) {
        return asistenciaService.findById(id);
    }

    @GetMapping("/por-evento/{idEvento}")
    @Operation(summary = "Listar asistencias por evento", description = "Devuelve todas las asistencias registradas para un evento concreto")
    public List<AsistenciaEventoDTO> getByEvento(@PathVariable Long idEvento) {
        return asistenciaService.findByEvento(idEvento);
    }

    @PostMapping
    @Operation(summary = "Crear asistencia", description = "Registra una nueva asistencia a un evento")
    public AsistenciaEventoDTO create(@RequestBody AsistenciaEventoCreateDTO dto) {
        return asistenciaService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar asistencia", description = "Modifica una asistencia existente mediante su ID")
    public AsistenciaEventoDTO update(
            @PathVariable Long id,
            @RequestBody AsistenciaEventoUpdateDTO dto
    ) {
        return asistenciaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar asistencia", description = "Elimina una asistencia existente mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        asistenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
