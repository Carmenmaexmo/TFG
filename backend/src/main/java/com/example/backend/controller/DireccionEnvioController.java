package com.example.backend.controller;

import com.example.backend.dto.DireccionEnvioDTO;
import com.example.backend.dto.DireccionEnvioCreateDTO;
import com.example.backend.service.DireccionEnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de direcciones de envío.
 */
@RestController
@RequestMapping("/api/direcciones-envio")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Direcciones de Envío", description = "Operaciones relacionadas con las direcciones de envío de los usuarios")
public class DireccionEnvioController {

    private final DireccionEnvioService direccionEnvioService;

    @GetMapping
    @Operation(summary = "Listar todas las direcciones", description = "Devuelve todas las direcciones de envío registradas en la base de datos")
    public List<DireccionEnvioDTO> getAll() {
        return direccionEnvioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dirección por ID", description = "Obtiene una dirección de envío concreta mediante su ID")
    public DireccionEnvioDTO getById(@PathVariable Long id) {
        return direccionEnvioService.findById(id);
    }

    @GetMapping("/por-usuario/{idUsuario}")
    @Operation(summary = "Listar direcciones por usuario", description = "Devuelve todas las direcciones de envío asociadas a un usuario")
    public List<DireccionEnvioDTO> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return direccionEnvioService.findByUsuario(idUsuario);
    }

    @PostMapping
    @Operation(summary = "Crear nueva dirección", description = "Registra una nueva dirección de envío")
    public DireccionEnvioDTO create(@RequestBody DireccionEnvioCreateDTO dto) {
        return direccionEnvioService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dirección", description = "Actualiza una dirección de envío existente mediante su ID")
    public DireccionEnvioDTO update(@PathVariable Long id, @RequestBody DireccionEnvioCreateDTO dto) {
        return direccionEnvioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dirección", description = "Elimina una dirección de envío mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        direccionEnvioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
