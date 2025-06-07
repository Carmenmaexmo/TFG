import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-pedidos',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './pedidos.component.html',
  styleUrls: ['./pedidos.component.css']
})
export class PedidosComponent implements OnInit {
  pedidos: any[] = [];
  pedidosOriginales: any[] = [];
  paginaActual: number = 1;
  pedidosPorPagina: number = 3;

  constructor(private api: ApiService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.api.getPedidosPorUsuario().subscribe({
      next: (data) => {
        // Ordenar por fecha descendente
        this.pedidosOriginales = data.sort((a, b) => 
          new Date(b.fechaPedido).getTime() - new Date(a.fechaPedido).getTime()
        );

        // Aplicar filtro inicial si hay búsqueda
        this.route.queryParams.subscribe(params => {
          const termino = (params['q'] || '').toLowerCase().trim();
          this.filtrarPedidos(termino);
        });
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

  get pedidosPaginados() {
  const inicio = (this.paginaActual - 1) * this.pedidosPorPagina;
  return this.pedidos.slice(inicio, inicio + this.pedidosPorPagina);
  }

  get totalPaginas(): number {
    return Math.ceil(this.pedidos.length / this.pedidosPorPagina);
  }

  cambiarPagina(p: number) {
    if (p >= 1 && p <= this.totalPaginas) {
      this.paginaActual = p;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  filtrarPedidos(termino: string) {
  if (!termino) {
    this.pedidos = [...this.pedidosOriginales];
    return;
  }

  this.pedidos = this.pedidosOriginales.filter(p =>
    p.id?.toString().includes(termino) ||
    p.estado?.toLowerCase().includes(termino) ||
    p.fechaPedido?.toLowerCase?.().includes(termino) ||
    p.direccionEnvio?.direccion?.toLowerCase().includes(termino) ||
    p.direccionEnvio?.ciudad?.toLowerCase().includes(termino) ||
    p.direccionEnvio?.telefono?.includes(termino) ||
    p.detalles?.some((detalle: any) =>
      detalle.vinilo?.titulo?.toLowerCase().includes(termino) ||
      detalle.vinilo?.artista?.toLowerCase().includes(termino) ||
      detalle.vinilo?.genero?.toLowerCase().includes(termino)
    )
  );

  // Reiniciar a la primera página
  this.paginaActual = 1;
  }

}
