// src/main/java/com/example/backend/controller/ComentarioForoController.java
package com.example.backend.controller;

import com.example.backend.dto.ComentarioForoCreateDTO;
import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.dto.ComentarioForoUpdateDTO;
import com.example.backend.service.ComentarioForoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de comentarios en los temas del foro.
 */
@RestController
@RequestMapping("/api/comentarios-foro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Comentarios de Foro", description = "Operaciones para crear, consultar, modificar o eliminar comentarios en los temas del foro")
public class ComentarioForoController {

    private final ComentarioForoService comentarioService;

    @GetMapping
    @Operation(summary = "Listar todos los comentarios", description = "Devuelve una lista con todos los comentarios registrados en el foro")
    public List<ComentarioForoDTO> getAll() {
        return comentarioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comentario por ID", description = "Obtiene un comentario específico mediante su ID")
    public ComentarioForoDTO getById(@PathVariable Long id) {
        return comentarioService.findById(id);
    }

    @GetMapping("/por-tema/{idTema}")
    @Operation(summary = "Listar comentarios por tema", description = "Devuelve todos los comentarios asociados a un tema concreto del foro")
    public List<ComentarioForoDTO> getByTema(@PathVariable Long idTema) {
        return comentarioService.findByTema(idTema);
    }

    @PostMapping
    @Operation(summary = "Crear comentario", description = "Crea un nuevo comentario en un tema del foro")
    public ComentarioForoDTO create(@RequestBody ComentarioForoCreateDTO dto) {
        return comentarioService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar comentario", description = "Modifica un comentario existente mediante su ID")
    public ComentarioForoDTO update(@PathVariable Long id, @RequestBody ComentarioForoUpdateDTO dto) {
        return comentarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar comentario", description = "Elimina un comentario del foro mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comentarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
