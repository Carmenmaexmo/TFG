import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pedidos-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pedidos-admin.component.html',
  styleUrls: ['./pedidos-admin.component.css']
})
export class PedidosAdminComponent implements OnInit {

  // Lista de todos los pedidos cargados desde el backend
  pedidos: any[] = [];

  // Filtro por texto (id o nombre de usuario)
  filtro: string = '';

  // Filtro específico por estado del pedido
  filtroEstado: string = '';

  // Pedido actualmente seleccionado para ver su detalle
  pedidoSeleccionado: any = null;

  // Estados disponibles para actualizar un pedido
  estadosDisponibles = ['PENDIENTE', 'ENVIADO', 'ENTREGADO', 'CANCELADO'];

  // ID del pedido en modo edición (para cambiar su estado)
  editandoId: number | null = null;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.cargarPedidos();
  }

  // Carga todos los pedidos desde el backend
  cargarPedidos() {
    this.api.getPedidos().subscribe(data => {
      this.pedidos = data;
    });
  }

  // Retorna los pedidos filtrados por texto (id o nombre) y por estado
  get pedidosFiltrados() {
    return this.pedidos.filter(p =>
      (!this.filtro || p.id.toString().includes(this.filtro) ||
       p.usuario.nombre.toLowerCase().includes(this.filtro.toLowerCase())) &&
      (!this.filtroEstado || p.estado === this.filtroEstado)
    );
  }

  // Calcula la cantidad total de pedidos por estado
  get resumenPedidos() {
    const resumen = [
      { estado: 'PENDIENTE', cantidad: 0 },
      { estado: 'ENVIADO', cantidad: 0 },
      { estado: 'ENTREGADO', cantidad: 0 },
      { estado: 'CANCELADO', cantidad: 0 }
    ];
    this.pedidos.forEach(p => {
      const r = resumen.find(r => r.estado === p.estado);
      if (r) r.cantidad++;
    });
    return resumen;
  }

  // Activa el modo edición para cambiar el estado del pedido
  activarEdicion(pedido: any) {
    this.editandoId = pedido.id;
  }

  // Guarda el nuevo estado del pedido
  guardarEstado(pedido: any) {
    this.api.actualizarPedido(pedido.id, { estado: pedido.estado }).subscribe(() => {
      this.editandoId = null;
    }, error => {
      console.error(`Error actualizando estado del pedido #${pedido.id}`, error);
    });
  }

  // Carga y muestra los detalles de un pedido concreto
  verDetalle(pedido: any) {
    this.api.getPedidoPorId(pedido.id).subscribe(data => {
      this.pedidoSeleccionado = data;
    }, error => {
      console.error('Error cargando detalles del pedido', error);
    });
  }

  // Cierra el panel de detalles
  cerrarDetalle() {
    this.pedidoSeleccionado = null;
  }

  // Elimina un pedido tras confirmación del usuario
  eliminarPedido(id: number) {
    if (confirm('¿Seguro que deseas eliminar este pedido?')) {
      this.api.eliminarPedido(id).subscribe(() => {
        this.pedidos = this.pedidos.filter(p => p.id !== id);
      });
    }
  }
}
