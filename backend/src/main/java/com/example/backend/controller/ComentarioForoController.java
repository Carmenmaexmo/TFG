// src/main/java/com/example/backend/controller/ComentarioForoController.java
package com.example.backend.controller;

import com.example.backend.dto.ComentarioForoCreateDTO;
import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.dto.ComentarioForoUpdateDTO;
import com.example.backend.service.ComentarioForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios-foro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ComentarioForoController {

    private final ComentarioForoService comentarioService;

    @GetMapping
    public List<ComentarioForoDTO> getAll() { return comentarioService.findAll(); }

    @GetMapping("/{id}")
    public ComentarioForoDTO getById(@PathVariable Long id) {
        return comentarioService.findById(id);
    }

    @GetMapping("/por-tema/{idTema}")
    public List<ComentarioForoDTO> getByTema(@PathVariable Long idTema) {
        return comentarioService.findByTema(idTema);
    }

    @PostMapping
    public ComentarioForoDTO create(@RequestBody ComentarioForoCreateDTO dto) {
        return comentarioService.create(dto);
    }

    @PutMapping("/{id}")
    public ComentarioForoDTO update(@PathVariable Long id, @RequestBody ComentarioForoUpdateDTO dto) {
        return comentarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        comentarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
