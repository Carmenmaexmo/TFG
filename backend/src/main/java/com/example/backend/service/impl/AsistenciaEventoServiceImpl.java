// Implementación del servicio de gestión de asistencias a eventos.
package com.example.backend.service.impl;

import com.example.backend.dto.AsistenciaEventoCreateDTO;
import com.example.backend.dto.AsistenciaEventoDTO;
import com.example.backend.dto.AsistenciaEventoUpdateDTO;
import com.example.backend.model.AsistenciaEvento;
import com.example.backend.model.Evento;
import com.example.backend.model.Usuario;
import com.example.backend.mapper.AsistenciaEventoMapper;
import com.example.backend.repository.AsistenciaEventoRepository;
import com.example.backend.repository.EventoRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.AsistenciaEventoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // Marca esta clase como componente de servicio para Spring.
@Transactional // Asegura que los métodos públicos se ejecuten dentro de una transacción.
@RequiredArgsConstructor // Genera constructor con todos los campos final (inyección automática).
public class AsistenciaEventoServiceImpl implements AsistenciaEventoService {

    // Repositorio de asistencias a eventos
    private final AsistenciaEventoRepository asistenciaRepo;

    // Repositorio de usuarios
    private final UsuarioRepository usuarioRepo;

    // Repositorio de eventos
    private final EventoRepository eventoRepo;

    // Mapper para convertir entre entidades y DTOs
    private final AsistenciaEventoMapper asistenciaMapper;

    /**
     * Obtiene todas las asistencias registradas en la base de datos.
     */
    @Override
    public List<AsistenciaEventoDTO> findAll() {
        return asistenciaRepo.findAll()
            .stream()
            .map(asistenciaMapper::toDTO)
            .toList();
    }

    /**
     * Busca una asistencia por su ID. Lanza 404 si no existe.
     */
    @Override
    public AsistenciaEventoDTO findById(Long id) {
        AsistenciaEvento ae = asistenciaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no encontrada"
            ));
        return asistenciaMapper.toDTO(ae);
    }

    /**
     * Devuelve todas las asistencias asociadas a un evento específico.
     */
    @Override
    public List<AsistenciaEventoDTO> findByEvento(Long idEvento) {
        return asistenciaRepo.findByEventoId(idEvento).stream()
            .map(asistenciaMapper::toDTO)
            .toList();
    }

    /**
     * Crea una nueva asistencia, validando que el usuario y el evento existan
     * y que no exista ya una asistencia duplicada.
     */
    @Override
    public AsistenciaEventoDTO create(AsistenciaEventoCreateDTO dto) {
        // Validación de usuario
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"
            ));

        // Validación de evento
        Evento evento = eventoRepo.findById(dto.getIdEvento())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Evento no encontrado"
            ));

        // Verificación de duplicidad
        boolean existe = asistenciaRepo
            .existsByUsuarioAndEvento(usuario, evento);
        if (existe) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El usuario ya está apuntado a este evento"
            );
        }

        // Creación de la entidad AsistenciaEvento
        AsistenciaEvento ae = AsistenciaEvento.builder()
            .usuario(usuario)
            .evento(evento)
            .confirmado(dto.getConfirmado() != null && dto.getConfirmado())
            .build();

        // Guardar en base de datos y devolver el DTO correspondiente
        return asistenciaMapper.toDTO(
            asistenciaRepo.save(ae)
        );
    }

    /**
     * Actualiza parcialmente una asistencia existente. Se pueden modificar usuario,
     * evento y estado de confirmación si se proporcionan en el DTO.
     */
    @Override
    public AsistenciaEventoDTO update(Long id, AsistenciaEventoUpdateDTO dto) {
        // Buscar asistencia por ID
        AsistenciaEvento ae = asistenciaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no encontrada"
            ));

        // Actualizar usuario si se proporciona
        if (dto.getIdUsuario() != null &&
            !dto.getIdUsuario().equals(ae.getUsuario().getIdUsuario())) {
            Usuario u = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));
            ae.setUsuario(u);
        }

        // Actualizar evento si se proporciona
        if (dto.getIdEvento() != null &&
            !dto.getIdEvento().equals(ae.getEvento().getId())) {
            Evento e = eventoRepo.findById(dto.getIdEvento())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Evento no encontrado"
                ));
            ae.setEvento(e);
        }

        // Actualizar confirmación si se proporciona
        if (dto.getConfirmado() != null) {
            ae.setConfirmado(dto.getConfirmado());
        }

        // Guardar cambios y devolver el DTO actualizado
        return asistenciaMapper.toDTO(
            asistenciaRepo.save(ae)
        );
    }

    /**
     * Elimina una asistencia si existe. Lanza 404 si no se encuentra.
     */
    @Override
    public void delete(Long id) {
        if (!asistenciaRepo.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no existe"
            );
        }
        asistenciaRepo.deleteById(id);
    }

    /**
     * Marca una asistencia como confirmada.
     */
    @Override
    public AsistenciaEventoDTO confirmar(Long id) {
        AsistenciaEvento ae = asistenciaRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Asistencia no encontrada"
            ));
        ae.setConfirmado(true);
        return asistenciaMapper.toDTO(asistenciaRepo.save(ae));
    }
}
