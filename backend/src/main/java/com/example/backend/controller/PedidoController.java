package com.example.backend.controller;

import com.example.backend.dto.PedidoCreateDTO;
import com.example.backend.dto.PedidoDTO;
import com.example.backend.dto.PedidoUpdateDTO;
import com.example.backend.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public List<PedidoDTO> getAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public PedidoDTO getById(@PathVariable Long id) {
        return pedidoService.findById(id);
    }

    @PostMapping
    public PedidoDTO create(@RequestBody PedidoCreateDTO dto) {
        return pedidoService.create(dto);
    }

    @PutMapping("/{id}")
    public PedidoDTO update(@PathVariable Long id,
                            @RequestBody PedidoUpdateDTO dto) {
        return pedidoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
