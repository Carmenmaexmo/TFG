package com.example.backend.controller;

import com.example.backend.dto.PedidoCreateDTO;
import com.example.backend.dto.PedidoDTO;
import com.example.backend.dto.PedidoUpdateDTO;
import com.example.backend.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con la gestión de pedidos realizados por los usuarios")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Devuelve todos los pedidos registrados en el sistema")
    public List<PedidoDTO> getAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Obtiene los datos de un pedido específico mediante su ID")
    public PedidoDTO getById(@PathVariable Long id) {
        return pedidoService.findById(id);
    }

    @GetMapping("/por-usuario/{idUsuario}")
    @Operation(summary = "Listar pedidos por usuario", description = "Devuelve todos los pedidos realizados por un usuario concreto")
    public List<PedidoDTO> getByUsuario(@PathVariable Long idUsuario) {
        return pedidoService.findByUsuario(idUsuario);
    }

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Registra un nuevo pedido con los datos proporcionados")
    public PedidoDTO create(@RequestBody PedidoCreateDTO dto) {
        return pedidoService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido", description = "Modifica los datos de un pedido existente mediante su ID")
    public PedidoDTO update(@PathVariable Long id,
                            @RequestBody PedidoUpdateDTO dto) {
        return pedidoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido existente mediante su ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
