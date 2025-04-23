package com.example.backend.service.impl;

import com.example.backend.dto.PedidoCreateDTO;
import com.example.backend.dto.PedidoDTO;
import com.example.backend.dto.DetallePedidoCreateDTO;
import com.example.backend.model.*;
import com.example.backend.mapper.PedidoMapper;
import com.example.backend.repository.*;
import com.example.backend.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DireccionEnvioRepository direccionEnvioRepository;
    private final ViniloRepository viniloRepository;
    private final PedidoMapper pedidoMapper;
    private final DetallePedidoRepository detallePedidoRepository;

    @Override
    public List<PedidoDTO> findAll() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toDTO)
                .toList();
    }

    @Override
    public PedidoDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public PedidoDTO create(PedidoCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DireccionEnvio direccion = direccionEnvioRepository.findById(dto.getIdDireccionEnvio())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(direccion);
        pedido.setEstado(dto.getEstado());
        pedido.setFechaPedido(dto.getFechaPedido());
        pedido.setTotal(dto.getTotal());

        List<DetallePedido> detalles = new ArrayList<>();

        for (DetallePedidoCreateDTO item : dto.getDetalles()) {
            Vinilo vinilo = viniloRepository.findById(item.getIdVinilo())
                    .orElseThrow(() -> new RuntimeException("Vinilo no encontrado"));

            DetallePedido detalle = new DetallePedido();
            detalle.setVinilo(vinilo);
            detalle.setCantidad(item.getCantidad());
            detalle.setPedido(pedido);
            detalles.add(detalle);
        }

        pedido.setDetalles(detalles);
        pedidoRepository.save(pedido);
        detallePedidoRepository.saveAll(detalles);

        return pedidoMapper.toDTO(pedido);
    }

    @Override
    public void delete(Long id) {
        pedidoRepository.deleteById(id);
    }
}
