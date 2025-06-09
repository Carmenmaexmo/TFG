// Implementación del servicio de comentarios en temas de foros
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

@Service // Anotación que indica a Spring que esta clase es un servicio
@RequiredArgsConstructor // Genera constructor con inyección de dependencias para los campos final
public class ComentarioForoServiceImpl implements ComentarioForoService {

    private final ComentarioForoRepository comentarioRepo;
    private final UsuarioRepository usuarioRepo;
    private final TemaForoRepository temaRepo;
    private final ComentarioForoMapper comentarioMapper;

    /**
     * Devuelve todos los comentarios almacenados.
     */
    @Override
    public List<ComentarioForoDTO> findAll() {
        return comentarioRepo.findAll()
                .stream()
                .map(comentarioMapper::toDTO)
                .toList();
    }

    /**
     * Busca un comentario por su ID. Lanza excepción si no existe.
     */
    @Override
    public ComentarioForoDTO findById(Long id) {
        ComentarioForo c = comentarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        return comentarioMapper.toDTO(c);
    }

    /**
     * Devuelve todos los comentarios asociados a un tema específico.
     */
    @Override
    public List<ComentarioForoDTO> findByTema(Long idTema) {
        return comentarioRepo.findByTemaId(idTema).stream()
                .map(comentarioMapper::toDTO)
                .toList();
    }

    /**
     * Crea un nuevo comentario en un tema del foro.
     */
    @Override
    public ComentarioForoDTO create(ComentarioForoCreateDTO dto) {
        // 1) Validar que el contenido no sea nulo, vacío ni demasiado largo
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

        // 2) Validar que se haya enviado una fecha válida
        if (dto.getFechaComentario() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La fecha de comentario es obligatoria"
            );
        }

        // 3) Validar que el usuario exista
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        // 4) Validar que el tema exista
        TemaForo tema = temaRepo.findById(dto.getIdTema())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tema no encontrado"
                ));

        // 5) Validar comentario padre si se indica (para hilos o respuestas)
        ComentarioForo padre = null;
        if (dto.getIdComentarioPadre() != null) {
            padre = comentarioRepo.findById(dto.getIdComentarioPadre())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Comentario padre no encontrado"
                ));
            // Validamos que el padre pertenezca al mismo tema
            if (!padre.getTema().getId().equals(tema.getId())) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El comentario padre debe pertenecer al mismo tema"
                );
            }
        }

        // 6) Crear la entidad, establecer relaciones y guardar
        ComentarioForo coment = comentarioMapper.fromCreateDTO(dto);
        coment.setUsuario(usuario);
        coment.setTema(tema);
        coment.setComentarioPadre(padre);

        return comentarioMapper.toDTO(
            comentarioRepo.save(coment)
        );
    }

    /**
     * Actualiza un comentario ya existente. Permite modificaciones parciales.
     */
    @Override
    public ComentarioForoDTO update(Long id, ComentarioForoUpdateDTO dto) {
        ComentarioForo c = comentarioRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Comentario no encontrado"
            ));

        // 1) Validar y actualizar contenido
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

        // 2) Actualizar fecha si se proporciona
        if (dto.getFechaComentario() != null) {
            c.setFechaComentario(dto.getFechaComentario());
        }

        // 3) Cambiar usuario si es diferente
        if (dto.getIdUsuario() != null
            && !dto.getIdUsuario().equals(c.getUsuario().getIdUsuario())) {
            Usuario u = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            c.setUsuario(u);
        }

        // 4) Cambiar tema si se solicita
        if (dto.getIdTema() != null
            && !dto.getIdTema().equals(c.getTema().getId())) {
            TemaForo t = temaRepo.findById(dto.getIdTema())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Tema no encontrado"
                ));
            c.setTema(t);
        }

        // 5) Cambiar comentario padre (si se quiere enlazar o desenlazar)
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
            // Si se indica explícitamente null, se elimina el vínculo con el comentario padre
            c.setComentarioPadre(null);
        }

        // 6) Guardar los cambios y devolver el resultado actualizado
        return comentarioMapper.toDTO(
            comentarioRepo.save(c)
        );
    }

    /**
     * Elimina un comentario. Si tiene hijos, solo se marca como eliminado.
     */
    @Override
    public void delete(Long id) {
        ComentarioForo c = comentarioRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Comentario no encontrado"
            ));

        // Verificar si tiene respuestas (comentarios hijos)
        List<ComentarioForo> respuestas = comentarioRepo.findByComentarioPadre_Id(id);
        if (!respuestas.isEmpty()) {
            // Si tiene hijos: solo se marca como "[comentario eliminado]"
            c.setContenido("[comentario eliminado]");
            comentarioRepo.save(c);
        } else {
            // Si no tiene hijos: se elimina de la base de datos
            comentarioRepo.delete(c);
        }
    }
}
