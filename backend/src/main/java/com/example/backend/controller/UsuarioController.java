package com.example.backend.controller;

import com.example.backend.dto.UsuarioDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.model.Usuario;
import com.example.backend.dto.UsuarioCreateDTO;
import com.example.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public UsuarioDTO getById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @GetMapping("/por-nombre/{username}")
    public ResponseEntity<Usuario> obtenerPorNombre(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.findByNombreUsuario(username);
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }    

    @PostMapping
    public UsuarioDTO create(@RequestBody UsuarioCreateDTO dto) {
        return usuarioService.create(dto);
    }

   @PutMapping("/{id}")
    public UsuarioDTO update(
        @PathVariable Long id,
        @RequestBody UsuarioUpdateDTO dto  
    ) {
        log.info("📩 Datos recibidos para update: {}", dto);
        log.info("🛒 Campo carrito recibido: {}", dto.getCarrito());
        return usuarioService.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<Map<String,String>> delete(@PathVariable Long id) {
            usuarioService.delete(id);
            Map<String,String> resp = new HashMap<>();
            resp.put("message", "Usuario con ID " + id + " eliminado correctamente");
            return ResponseEntity
                    .ok()          // HTTP 200 OK
                    .body(resp);   // cuerpo { "message": "...eliminado correctamente" }
    }
}
