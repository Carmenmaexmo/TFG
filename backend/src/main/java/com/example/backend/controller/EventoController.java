// src/main/java/com/example/backend/controller/EventoController.java
package com.example.backend.controller;

import com.example.backend.dto.EventoCreateDTO;
import com.example.backend.dto.EventoDTO;
import com.example.backend.dto.EventoUpdateDTO;
import com.example.backend.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public List<EventoDTO> getAll() {
        return eventoService.findAll();
    }

    @GetMapping("/{id}")
    public EventoDTO getById(@PathVariable Long id) {
        return eventoService.findById(id);
    }

    @PostMapping
    public EventoDTO create(@RequestBody EventoCreateDTO dto) {
        return eventoService.create(dto);
    }

    @PutMapping("/{id}")
    public EventoDTO update(
            @PathVariable Long id,
            @RequestBody EventoUpdateDTO dto
    ) {
        return eventoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
