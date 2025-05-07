// src/main/java/com/example/backend/controller/BloqueoForoController.java
package com.example.backend.controller;

import com.example.backend.dto.BloqueoForoCreateDTO;
import com.example.backend.dto.BloqueoForoDTO;
import com.example.backend.dto.BloqueoForoUpdateDTO;
import com.example.backend.service.BloqueoForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bloqueos-foro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BloqueoForoController {

    private final BloqueoForoService bloqueoService;

    @GetMapping
    public List<BloqueoForoDTO> getAll() { return bloqueoService.findAll(); }

    @GetMapping("/{id}")
    public BloqueoForoDTO getById(@PathVariable Long id) {
        return bloqueoService.findById(id);
    }

    @PostMapping
    public BloqueoForoDTO create(@RequestBody BloqueoForoCreateDTO dto) {
        return bloqueoService.create(dto);
    }

    @PutMapping("/{id}")
    public BloqueoForoDTO update(@PathVariable Long id, @RequestBody BloqueoForoUpdateDTO dto) {
        return bloqueoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bloqueoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
