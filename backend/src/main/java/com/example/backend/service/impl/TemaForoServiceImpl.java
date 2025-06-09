// Servicio de gestión de temas en foros.
// Se encarga de crear, consultar, actualizar y eliminar temas,
// validando integridad de datos y relaciones con usuario y foro.
package com.example.backend.service.impl;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.mapper.TemaForoMapper;
import com.example.backend.repository.*;
import com.example.backend.service.TemaForoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // Marca la clase como un servicio de Spring
@RequiredArgsConstructor // Genera constructor con los campos final para inyección automática
public class TemaForoServiceImpl implements TemaForoService {

    private final TemaForoRepository temaRepo;
    private final UsuarioRepository usuarioRepo;
    private final ForoRepository foroRepo;
    private final TemaForoMapper temaMapper;

    /**
     * Devuelve todos los temas de foro existentes.
     */
    @Override
    public List<TemaForoDTO> findAll() {
        return temaRepo.findAll()
                .stream()
                .map(temaMapper::toDTO)
                .toList();
    }

    /**
     * Busca un tema por su ID. Lanza excepción si no se encuentra.
     */
    @Override
    public TemaForoDTO findById(Long id) {
        TemaForo tema = temaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        return temaMapper.toDTO(tema);
    }

    /**
     * Crea un nuevo tema dentro de un foro, validando título único por foro,
     * existencia de foro y usuario creador.
     */
    @Override
    public TemaForoDTO create(TemaForoCreateDTO dto) {
        // 1) Validar título
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El título es obligatorio"
            );
        }
        String tituloNorm = dto.getTitulo().trim();

        // 2) Validar foro y evitar títulos duplicados dentro del mismo foro
        Foro foro = foroRepo.findById(dto.getIdForo())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Foro no encontrado"
            ));
        if (temaRepo.existsByTituloAndForoId(tituloNorm, foro.getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Ya existe un tema con ese título en este foro"
            );
        }

        // 3) Validar existencia del usuario creador
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // 4) Mapear, asignar relaciones y guardar
        TemaForo tema = temaMapper.fromCreateDTO(dto);
        tema.setTitulo(tituloNorm);
        tema.setForo(foro);
        tema.setUsuario(usuario);

        return temaMapper.toDTO(temaRepo.save(tema));
    }

    /**
     * Actualiza un tema existente de forma parcial.
     * Permite cambiar título, contenido, usuario y foro.
     */
    @Override
    public TemaForoDTO update(Long id, TemaForoUpdateDTO dto) {
        TemaForo tema = temaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tema no encontrado"
            ));

        // 1) Actualizar título
        if (dto.getTitulo() != null) {
            if (dto.getTitulo().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El título no puede estar vacío"
                );
            }
            tema.setTitulo(dto.getTitulo());
        }

        // 2) Actualizar contenido
        if (dto.getContenido() != null) {
            if (dto.getContenido().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El contenido no puede estar vacío"
                );
            }
            tema.setContenido(dto.getContenido());
        }

        // 3) Reasignar autor del tema si cambia
        if (dto.getIdUsuario() != null && 
            !dto.getIdUsuario().equals(tema.getUsuario().getIdUsuario())) {
            Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            tema.setUsuario(usuario);
        }

        // 4) Reasignar a otro foro si cambia
        if (dto.getIdForo() != null &&
            !dto.getIdForo().equals(tema.getForo().getId())) {
            Foro foro = foroRepo.findById(dto.getIdForo())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Foro no encontrado"
                ));
            tema.setForo(foro);
        }

        TemaForo actualizado = temaRepo.save(tema);
        return temaMapper.toDTO(actualizado);
    }

    /**
     * Elimina un tema por su ID.
     */
    @Override
    public void delete(Long id) {
        temaRepo.deleteById(id);
    }
}
