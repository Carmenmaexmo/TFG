// Servicio encargado de la gestión de pedidos, incluyendo validaciones,
// asociación con usuario, dirección y vinilos, así como lógica de stock.
package com.example.backend.service.impl;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.mapper.PedidoMapper;
import com.example.backend.repository.*;
import com.example.backend.service.PedidoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service // Define este componente como servicio de Spring
@RequiredArgsConstructor // Inyección automática de dependencias vía constructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionEnvioRepository direccionEnvioRepository;
    private final ViniloRepository viniloRepository;
    private final PedidoMapper pedidoMapper;
    private final DetallePedidoRepository detallePedidoRepository;

    // Conjunto de estados válidos que puede tener un pedido
    private static final Set<String> ESTADOS_VALIDOS = Set.of(
        "PENDIENTE", "ENVIADO", "ENTREGADO", "CANCELADO"
    );

    /**
     * Crea un nuevo pedido, validando datos de usuario, dirección,
     * estado, fecha, total y vinilos del detalle.
     */
    @Override
    public PedidoDTO create(PedidoCreateDTO dto) {
        // Validación del estado del pedido
        if (dto.getEstado() == null || dto.getEstado().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El estado del pedido es obligatorio"
            );
        }
        String estadoNorm = dto.getEstado().trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoNorm)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Estado inválido. Debe ser uno de " + ESTADOS_VALIDOS
            );
        }

        // Validación de fecha y total
        if (dto.getFechaPedido() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La fecha de pedido es obligatoria"
            );
        }
        if (dto.getTotal() == null || dto.getTotal() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "El total debe ser un número mayor que 0"
            );
        }

        // Validación de relaciones
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        DireccionEnvio direccion = direccionEnvioRepository.findById(dto.getIdDireccionEnvio())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección de envío no encontrada"));

        // Crear y guardar pedido (cabecera)
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(direccion);
        pedido.setEstado(estadoNorm);
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setTotal(dto.getTotal());
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Crear y guardar los detalles del pedido
        List<DetallePedido> detalles = new ArrayList<>();
        for (DetallePedidoCreateDTO item : dto.getDetalles()) {
            if (item.getCantidad() == null || item.getCantidad() < 1) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cada detalle debe tener cantidad ≥ 1"
                );
            }
            Vinilo vinilo = viniloRepository.findById(item.getIdVinilo())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Vinilo con id " + item.getIdVinilo() + " no encontrado"
                ));

            DetallePedido det = new DetallePedido();
            det.setPedido(pedidoGuardado);
            det.setVinilo(vinilo);
            det.setPrecio(vinilo.getPrecio());
            det.setCantidad(item.getCantidad());
            detalles.add(det);
        }
        detallePedidoRepository.saveAll(detalles);
        pedidoGuardado.setDetalles(detalles);

        return pedidoMapper.toDTO(pedidoGuardado);
    }

    /**
     * Actualiza datos del pedido, incluyendo estado (con lógica de stock),
     * fecha, total, usuario, dirección y detalles.
     */
    @Override
    public PedidoDTO update(Long id, PedidoUpdateDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Pedido no encontrado"
            ));

        // Actualizar estado y gestionar stock si cambia a ENVIADO o ENTREGADO
        if (dto.getEstado() != null) {
            String estadoNorm = dto.getEstado().trim().toUpperCase();
            if (!ESTADOS_VALIDOS.contains(estadoNorm)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Estado inválido. Debe ser uno de " + ESTADOS_VALIDOS
                );
            }

            boolean actualizarStock = 
                (estadoNorm.equals("ENVIADO") || estadoNorm.equals("ENTREGADO")) &&
                !(pedido.getEstado().equals("ENVIADO") || pedido.getEstado().equals("ENTREGADO"));

            pedido.setEstado(estadoNorm);

            if (actualizarStock) {
                for (DetallePedido detalle : pedido.getDetalles()) {
                    Vinilo vinilo = detalle.getVinilo();
                    int nuevaCantidad = vinilo.getStock() - detalle.getCantidad();
                    vinilo.setStock(Math.max(nuevaCantidad, 0)); // Evita stock negativo
                    viniloRepository.save(vinilo);
                }
            }
        }

        // Actualizar fecha
        if (dto.getFechaPedido() != null) {
            pedido.setFechaPedido(dto.getFechaPedido());
        }

        // Actualizar total
        if (dto.getTotal() != null) {
            if (dto.getTotal() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El total debe ser mayor que 0");
            }
            pedido.setTotal(dto.getTotal());
        }

        // Actualizar usuario (si se cambia)
        if (dto.getIdUsuario() != null &&
            !dto.getIdUsuario().equals(pedido.getUsuario().getIdUsuario())) {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            pedido.setUsuario(usuario);
        }

        // Actualizar dirección de envío
        if (dto.getIdDireccionEnvio() != null &&
            !dto.getIdDireccionEnvio().equals(pedido.getDireccionEnvio().getId())) {
            DireccionEnvio direccion = direccionEnvioRepository.findById(dto.getIdDireccionEnvio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección de envío no encontrada"));
            pedido.setDireccionEnvio(direccion);
        }

        // Actualizar detalles (si vienen)
        if (dto.getDetalles() != null) {
            Map<Long, DetallePedido> existentes = pedido.getDetalles().stream()
                .collect(Collectors.toMap(DetallePedido::getId, Function.identity()));

            List<DetallePedido> fusionados = new ArrayList<>();
            for (DetallePedidoUpsertDTO d : dto.getDetalles()) {
                if (d.getId() != null && existentes.containsKey(d.getId())) {
                    // Actualizar cantidad de detalle existente
                    DetallePedido vieja = existentes.get(d.getId());
                    if (d.getCantidad() < 1) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Cantidad de detalle debe ser ≥ 1");
                    }
                    vieja.setCantidad(d.getCantidad());
                    fusionados.add(vieja);
                } else {
                    // Crear nuevo detalle
                    Vinilo vinilo = viniloRepository.findById(d.getIdVinilo())
                        .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Vinilo con id " + d.getIdVinilo() + " no encontrado"));
                    if (d.getCantidad() < 1) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Cantidad de detalle debe ser ≥ 1");
                    }
                    DetallePedido nueva = new DetallePedido();
                    nueva.setPedido(pedido);
                    nueva.setVinilo(vinilo);
                    nueva.setCantidad(d.getCantidad());
                    fusionados.add(nueva);
                }
            }

            pedido.setDetalles(fusionados);
            detallePedidoRepository.saveAll(fusionados);
        }

        // Guardar cabecera
        pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedido);
    }

    /**
     * Elimina un pedido si existe.
     */
    @Override
    public void delete(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no existe");
        }
        pedidoRepository.deleteById(id);
    }

    /**
     * Devuelve todos los pedidos existentes.
     */
    @Override
    public List<PedidoDTO> findAll() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDTO)
                .toList();
    }

    /**
     * Busca un pedido por su ID.
     */
    @Override
    public PedidoDTO findById(Long id) {
        Pedido p = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Pedido no encontrado"
                ));
        return pedidoMapper.toDTO(p);
    }

    /**
     * Devuelve todos los pedidos realizados por un usuario.
     */
    @Override
    public List<PedidoDTO> findByUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    
        return pedidoRepository.findByUsuario(usuario).stream()
            .map(pedidoMapper::toDTO)
            .collect(Collectors.toList());
    }
}
