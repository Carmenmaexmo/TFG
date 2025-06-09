package com.example.backend.controller;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.model.Usuario;
import com.example.backend.dto.UsuarioCreateDTO;
import com.example.backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para la gestión de usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Operaciones para consultar, registrar, actualizar o eliminar usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios registrados en el sistema")
    public List<UsuarioDTO> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario específico mediante su ID")
    public UsuarioDTO getById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @GetMapping("/por-nombre/{username}")
    @Operation(summary = "Buscar usuario por nombre de usuario", description = "Devuelve los datos de un usuario mediante su nombre de usuario")
    public ResponseEntity<Usuario> obtenerPorNombre(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.findByNombreUsuario(username);
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Registra un nuevo usuario con los datos proporcionados")
    public UsuarioDTO create(@RequestBody UsuarioCreateDTO dto) {
        return usuarioService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente mediante su ID")
    public UsuarioDTO update(
        @PathVariable Long id,
        @RequestBody UsuarioUpdateDTO dto
    ) {
        log.info("Datos recibidos para update: {}", dto);
        log.info("Campo carrito recibido: {}", dto.getCarrito());
        return usuarioService.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema mediante su ID")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "Usuario con ID " + id + " eliminado correctamente");
        return ResponseEntity.ok().body(resp);
    }
}
