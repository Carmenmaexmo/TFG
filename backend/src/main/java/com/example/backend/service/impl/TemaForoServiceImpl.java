// src/main/java/com/example/backend/service/impl/TemaForoServiceImpl.java
package com.example.backend.service.impl;

import com.example.backend.dto.TemaForoCreateDTO;
import com.example.backend.dto.TemaForoDTO;
import com.example.backend.model.Foro;
import com.example.backend.model.TemaForo;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.TemaForoMapper;
import com.example.backend.repository.ForoRepository;
import com.example.backend.repository.TemaForoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.TemaForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Foro foro = foroRepo.findById(dto.getIdForo())
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));

        TemaForo tema = temaMapper.fromCreateDTO(dto);
        tema.setUsuario(usuario);
        tema.setForo(foro);

        return temaMapper.toDTO(temaRepo.save(tema));
    }

    @Override
    public TemaForoDTO update(Long id, TemaForoCreateDTO dto) {
        TemaForo tema = temaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        tema.setTitulo(dto.getTitulo());
        tema.setContenido(dto.getContenido());
        return temaMapper.toDTO(temaRepo.save(tema));
    }

    @Override
    public void delete(Long id) {
        temaRepo.deleteById(id);
    }
}
