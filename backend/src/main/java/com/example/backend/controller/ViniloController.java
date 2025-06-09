package com.example.backend.controller;

import com.example.backend.dto.ViniloDTO;
import com.example.backend.dto.ViniloUpdateDTO;
import com.example.backend.dto.ViniloCreateDTO;
import com.example.backend.service.ViniloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión del catálogo de vinilos.
 */
@RestController
@RequestMapping("/api/vinilos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Vinilos", description = "Operaciones para consultar, registrar, actualizar o eliminar vinilos del catálogo")
public class ViniloController {

    private static final Logger log = LoggerFactory.getLogger(ViniloController.class);

    private final ViniloService viniloService;

    @GetMapping
    @Operation(summary = "Listar vinilos", description = "Devuelve todos los vinilos disponibles en el catálogo")
    public List<ViniloDTO> getAll() {
        log.info("Accediendo al listado de vinilos");
        return viniloService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vinilo por ID", description = "Obtiene los datos de un vinilo específico mediante su ID")
    public ViniloDTO getById(@PathVariable Long id) {
        return viniloService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear vinilo", description = "Registra un nuevo vinilo en el catálogo")
    public ViniloDTO create(@RequestBody ViniloCreateDTO dto) {
        return viniloService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vinilo", description = "Modifica los datos de un vinilo existente mediante su ID")
    public ViniloDTO update(@PathVariable Long id, @RequestBody ViniloUpdateDTO dto) {
        return viniloService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vinilo", description = "Elimina un vinilo del catálogo mediante su ID")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        viniloService.delete(id);
        Map<String, String> response = Map.of(
            "message", "Vinilo con id " + id + " eliminado correctamente"
        );
        return ResponseEntity.ok(response);
    }
}
