// src/main/java/com/example/backend/service/impl/ComentarioForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.ComentarioForoCreateDTO;
import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.dto.ComentarioForoUpdateDTO;
import com.example.backend.model.ComentarioForo;
import com.example.backend.model.TemaForo;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.ComentarioForoMapper;
import com.example.backend.repository.ComentarioForoRepository;
import com.example.backend.repository.TemaForoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.ComentarioForoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioForoServiceImpl implements ComentarioForoService {

    private final ComentarioForoRepository comentarioRepo;
    private final UsuarioRepository usuarioRepo;
    private final TemaForoRepository temaRepo;
    private final ComentarioForoMapper comentarioMapper;

    @Override
    public List<ComentarioForoDTO> findAll() {
        return comentarioRepo.findAll()
                .stream()
                .map(comentarioMapper::toDTO)
                .toList();
    }

    @Override
    public ComentarioForoDTO findById(Long id) {
        ComentarioForo c = comentarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        return comentarioMapper.toDTO(c);
    }

    @Override
    public ComentarioForoDTO create(ComentarioForoCreateDTO dto) {
        // 1) Validar contenido
        if (dto.getContenido() == null || dto.getContenido().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El contenido es obligatorio"
            );
        }
        if (dto.getContenido().length() > 255) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El contenido no puede superar 255 caracteres"
            );
        }

        // 2) Validar fecha
        if (dto.getFechaComentario() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La fecha de comentario es obligatoria"
            );
        }

        // 3) Validar usuario
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        // 4) Validar tema
        TemaForo tema = temaRepo.findById(dto.getIdTema())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tema no encontrado"
                ));

        // 5) Validar comentario padre (opcional)
        ComentarioForo padre = null;
        if (dto.getIdComentarioPadre() != null) {
            padre = comentarioRepo.findById(dto.getIdComentarioPadre())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Comentario padre no encontrado"
                ));
            if (!padre.getTema().getId().equals(tema.getId())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El comentario padre debe pertenecer al mismo tema"
                );
            }
        }

        // 6) Crear y guardar
        ComentarioForo coment = comentarioMapper.fromCreateDTO(dto);
        coment.setUsuario(usuario);
        coment.setTema(tema);
        coment.setComentarioPadre(padre);

        return comentarioMapper.toDTO(
            comentarioRepo.save(coment)
        );
    }

    @Override
    public ComentarioForoDTO update(Long id, ComentarioForoUpdateDTO dto) {
        ComentarioForo c = comentarioRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Comentario no encontrado"
            ));

        // 1) Contenido
        if (dto.getContenido() != null) {
            if (dto.getContenido().isBlank()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El contenido no puede estar vacío"
                );
            }
            if (dto.getContenido().length() > 255) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El contenido no puede superar 255 caracteres"
                );
            }
            c.setContenido(dto.getContenido());
        }

        // 2) Fecha
        if (dto.getFechaComentario() != null) {
            c.setFechaComentario(dto.getFechaComentario());
        }

        // 3) Usuario
        if (dto.getIdUsuario() != null
            && !dto.getIdUsuario().equals(c.getUsuario().getIdUsuario())) {
            Usuario u = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            c.setUsuario(u);
        }

        // 4) Tema
        if (dto.getIdTema() != null
            && !dto.getIdTema().equals(c.getTema().getId())) {
            TemaForo t = temaRepo.findById(dto.getIdTema())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tema no encontrado"
                ));
            c.setTema(t);
        }

        // 5) Comentario padre (puede quedar nulo para "desenlazar")
        if (dto.getIdComentarioPadre() != null) {
            ComentarioForo padre = comentarioRepo.findById(dto.getIdComentarioPadre())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Comentario padre no encontrado"
                ));
            if (!padre.getTema().getId().equals(c.getTema().getId())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El comentario padre debe pertenecer al mismo tema"
                );
            }
            c.setComentarioPadre(padre);
        } else if (dto.getIdComentarioPadre() != null && dto.getIdComentarioPadre() == null) {
            // si explícitamente viene null, desenlazamos
            c.setComentarioPadre(null);
        }

        // 6) Guardar y devolver
        return comentarioMapper.toDTO(
            comentarioRepo.save(c)
        );
    }


    @Override
    public void delete(Long id) {
        ComentarioForo c = comentarioRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Comentario no encontrado"
            ));

        // buscamos hijos directos
        List<ComentarioForo> respuestas = comentarioRepo.findByComentarioPadre_Id(id);
        if (!respuestas.isEmpty()) {
            // —> SOFT‐MARK: sólo “marcamos” el comentario como eliminado
            c.setContenido("[comentario eliminado]");
            comentarioRepo.save(c);
        } else {
            // no tiene hijos: borramos físicamente
            comentarioRepo.delete(c);
        }
    }

}
