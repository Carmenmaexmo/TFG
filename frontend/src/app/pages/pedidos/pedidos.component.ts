import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './pedidos.component.html',
  styleUrls: ['./pedidos.component.css']
})
export class PedidosComponent implements OnInit {
  pedidos: any[] = [];

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getPedidosPorUsuario().subscribe({
      next: (data) => {
        // Ordenar por fecha descendente
        this.pedidos = data.sort((a, b) => new Date(b.fechaPedido).getTime() - new Date(a.fechaPedido).getTime());
      },
      error: (err) => console.error('Error al cargar pedidos:', err)
    });
  }

  confirmarEntrega(pedido: any) {
    const actualizado = { ...pedido, estado: 'ENTREGADO' };

    this.api.actualizarPedido(pedido.id, actualizado).subscribe({
      next: (res) => {
        pedido.estado = 'ENTREGADO'; // actualizamos visualmente
      },
      error: (err) => console.error('Error al confirmar entrega:', err)
    });
  }

  cancelarPedido(pedido: any) {
      this.api.eliminarPedido(pedido.id).subscribe({
        next: () => {
          this.pedidos = this.pedidos.filter(p => p.id !== pedido.id);
        },
        error: (err) => console.error('Error al cancelar el pedido:', err)
      });
  }
  
}
