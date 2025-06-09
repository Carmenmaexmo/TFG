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
  pedidos: any[] = []; // Lista de pedidos mostrados en pantalla (filtrados)
  pedidosOriginales: any[] = []; // Lista original sin filtros, para conservar los datos
  paginaActual: number = 1; // Página actual en la paginación
  pedidosPorPagina: number = 3; // Número de pedidos mostrados por página

  constructor(private api: ApiService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Al iniciar el componente, obtenemos todos los pedidos del usuario
    this.api.getPedidosPorUsuario().subscribe({
      next: (data) => {
        // Ordenamos los pedidos por fecha descendente (más reciente primero)
        this.pedidosOriginales = data.sort((a, b) =>
          new Date(b.fechaPedido).getTime() - new Date(a.fechaPedido).getTime()
        );

        // Aplicamos un filtro inicial si hay un parámetro de búsqueda en la URL
        this.route.queryParams.subscribe(params => {
          const termino = (params['q'] || '').toLowerCase().trim();
          this.filtrarPedidos(termino);
        });
      },
      error: (err) => console.error('Error al cargar pedidos:', err)
    });
  }

  // Permite confirmar la entrega de un pedido, cambiando su estado a "ENTREGADO"
  confirmarEntrega(pedido: any) {
    const actualizado = { ...pedido, estado: 'ENTREGADO' };

    this.api.actualizarPedido(pedido.id, actualizado).subscribe({
      next: () => {
        pedido.estado = 'ENTREGADO'; // Se actualiza visualmente el estado
      },
      error: (err) => console.error('Error al confirmar entrega:', err)
    });
  }

  // Permite cancelar (eliminar) un pedido
  cancelarPedido(pedido: any) {
    this.api.eliminarPedido(pedido.id).subscribe({
      next: () => {
        // Eliminamos el pedido cancelado de la lista
        this.pedidos = this.pedidos.filter(p => p.id !== pedido.id);
      },
      error: (err) => console.error('Error al cancelar el pedido:', err)
    });
  }

  // Devuelve la lista de pedidos correspondientes a la página actual
  get pedidosPaginados() {
    const inicio = (this.paginaActual - 1) * this.pedidosPorPagina;
    return this.pedidos.slice(inicio, inicio + this.pedidosPorPagina);
  }

  // Calcula el número total de páginas
  get totalPaginas(): number {
    return Math.ceil(this.pedidos.length / this.pedidosPorPagina);
  }

  // Cambia de página si el número es válido
  cambiarPagina(p: number) {
    if (p >= 1 && p <= this.totalPaginas) {
      this.paginaActual = p;
      window.scrollTo({ top: 0, behavior: 'smooth' }); // Desplazamiento visual
    }
  }

  // Aplica un filtro sobre la lista de pedidos en función del término de búsqueda
  filtrarPedidos(termino: string) {
    if (!termino) {
      this.pedidos = [...this.pedidosOriginales]; // Restaurar lista original
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

    // Reiniciamos a la primera página tras aplicar el filtro
    this.paginaActual = 1;
  }
}
