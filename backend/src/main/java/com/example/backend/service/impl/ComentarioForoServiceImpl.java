// src/main/java/com/example/backend/service/impl/ComentarioForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.ComentarioForoCreateDTO;
import com.example.backend.dto.ComentarioForoDTO;
import com.example.backend.model.ComentarioForo;
import com.example.backend.model.TemaForo;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.ComentarioForoMapper;
import com.example.backend.repository.ComentarioForoRepository;
import com.example.backend.repository.TemaForoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.ComentarioForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TemaForo tema = temaRepo.findById(dto.getIdTema())
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        ComentarioForo comentario = comentarioMapper.fromCreateDTO(dto);
        comentario.setUsuario(usuario);
        comentario.setTema(tema);

        return comentarioMapper.toDTO(comentarioRepo.save(comentario));
    }

    @Override
    public ComentarioForoDTO update(Long id, ComentarioForoCreateDTO dto) {
        ComentarioForo c = comentarioRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        c.setContenido(dto.getContenido());
        return comentarioMapper.toDTO(comentarioRepo.save(c));
    }

    @Override
    public void delete(Long id) {
        comentarioRepo.deleteById(id);
    }
}
