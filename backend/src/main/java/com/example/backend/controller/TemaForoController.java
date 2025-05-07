// src/main/java/com/example/backend/controller/TemaForoController.java
package com.example.backend.controller;

import com.example.backend.dto.TemaForoCreateDTO;
import com.example.backend.dto.TemaForoDTO;
import com.example.backend.dto.TemaForoUpdateDTO;
import com.example.backend.service.TemaForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temas-foro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TemaForoController {

    private final TemaForoService temaService;

    @GetMapping
    public List<TemaForoDTO> getAll() { return temaService.findAll(); }

    @GetMapping("/{id}")
    public TemaForoDTO getById(@PathVariable Long id) { return temaService.findById(id); }

    @PostMapping
    public TemaForoDTO create(@RequestBody TemaForoCreateDTO dto) {
        return temaService.create(dto);
    }

    @PutMapping("/{id}")
    public TemaForoDTO update(@PathVariable Long id, @RequestBody TemaForoUpdateDTO dto) {
        return temaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        temaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
