// src/main/java/com/example/backend/service/impl/TemaForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.TemaForoCreateDTO;
import com.example.backend.dto.TemaForoDTO;
import com.example.backend.dto.TemaForoUpdateDTO;
import com.example.backend.model.Foro;
import com.example.backend.model.TemaForo;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.TemaForoMapper;
import com.example.backend.repository.ForoRepository;
import com.example.backend.repository.TemaForoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.TemaForoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemaForoServiceImpl implements TemaForoService {

    private final TemaForoRepository temaRepo;
    private final UsuarioRepository usuarioRepo;
    private final ForoRepository foroRepo;
    private final TemaForoMapper temaMapper;

    @Override
    public List<TemaForoDTO> findAll() {
        return temaRepo.findAll()
                .stream()
                .map(temaMapper::toDTO)
                .toList();
    }

    @Override
    public TemaForoDTO findById(Long id) {
        TemaForo tema = temaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        return temaMapper.toDTO(tema);
    }

     @Override
     public TemaForoDTO create(TemaForoCreateDTO dto) {
         // 1) Validar título
         if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
             throw new ResponseStatusException(
                 HttpStatus.BAD_REQUEST, "El título es obligatorio"
             );
         }
         String tituloNorm = dto.getTitulo().trim();
         // 2) Validar foro y duplicados
         Foro foro = foroRepo.findById(dto.getIdForo())
             .orElseThrow(() -> new ResponseStatusException(
                 HttpStatus.NOT_FOUND, "Foro no encontrado"
             ));
         if (temaRepo.existsByTituloAndForoId(tituloNorm, foro.getId())) {
             throw new ResponseStatusException(
                 HttpStatus.BAD_REQUEST,
                 "Ya existe un tema con ese título en este foro"
             );
         }
         // 3) Validar usuario
         Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
             .orElseThrow(() -> new ResponseStatusException(
                 HttpStatus.NOT_FOUND, "Usuario no encontrado"
             ));
     
         // 4) Crear y guardar
         TemaForo tema = temaMapper.fromCreateDTO(dto);
         tema.setTitulo(tituloNorm);
         tema.setForo(foro);
         tema.setUsuario(usuario);
     
         return temaMapper.toDTO(temaRepo.save(tema));
     }
     

    @Override
    public TemaForoDTO update(Long id, TemaForoUpdateDTO dto) {
        TemaForo tema = temaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tema no encontrado"
            ));

        // 1) Título
        if (dto.getTitulo() != null) {
            if (dto.getTitulo().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El título no puede estar vacío"
                );
            }
            tema.setTitulo(dto.getTitulo());
        }

        // 2) Contenido
        if (dto.getContenido() != null) {
            if (dto.getContenido().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El contenido no puede estar vacío"
                );
            }
            tema.setContenido(dto.getContenido());
        }

        // 3) Reasignar usuario (autor) si viene
        if (dto.getIdUsuario() != null && 
            !dto.getIdUsuario().equals(tema.getUsuario().getIdUsuario())) {
            Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            tema.setUsuario(usuario);
        }

        // 4) Reasignar foro si viene
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

    @Override
    public void delete(Long id) {
        temaRepo.deleteById(id);
    }
}
