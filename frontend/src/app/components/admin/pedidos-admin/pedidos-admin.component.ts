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
  pedidos: any[] = [];
  filtro: string = '';
  filtroEstado: string = '';
  pedidoSeleccionado: any = null;
  estadosDisponibles = ['PENDIENTE', 'ENVIADO', 'ENTREGADO', 'CANCELADO'];
  editandoId: number | null = null;

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.cargarPedidos();
  }

  cargarPedidos() {
    this.api.getPedidos().subscribe(data => {
      this.pedidos = data;
    });
  }

  get pedidosFiltrados() {
    return this.pedidos.filter(p =>
      (!this.filtro || p.id.toString().includes(this.filtro) || p.usuario.nombre.toLowerCase().includes(this.filtro.toLowerCase())) &&
      (!this.filtroEstado || p.estado === this.filtroEstado)
    );
  }

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

  activarEdicion(pedido: any) {
    this.editandoId = pedido.id;
  }

  guardarEstado(pedido: any) {
    this.api.actualizarPedido(pedido.id, { estado: pedido.estado }).subscribe(() => {
      console.log(`✅ Estado del pedido #${pedido.id} actualizado a ${pedido.estado}`);
      this.editandoId = null;
    }, error => {
      console.error(`❌ Error actualizando estado del pedido #${pedido.id}`, error);
    });
  }

  verDetalle(pedido: any) {
    this.api.getPedidoPorId(pedido.id).subscribe(data => {
      this.pedidoSeleccionado = data;
    }, error => {
      console.error('❌ Error cargando detalles del pedido', error);
    });
  }  

  cerrarDetalle() {
    this.pedidoSeleccionado = null;
  }

  eliminarPedido(id: number) {
    if (confirm('¿Seguro que deseas eliminar este pedido?')) {
      this.api.eliminarPedido(id).subscribe(() => {
        this.pedidos = this.pedidos.filter(p => p.id !== id);
        console.log(`✅ Pedido #${id} eliminado`);
      });
    }
  }
}
