import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-eventos',
  templateUrl: './eventos.component.html',
  styleUrls: ['./eventos.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class EventosComponent implements OnInit {
  
  // Lista de eventos mostrados actualmente
  eventos: any[] = [];

  // Copia original de todos los eventos para restaurar o reordenar
  eventosOriginales: any[] = [];

  // Criterio de orden actual: ascendente, descendente, próximos o sin orden
  sentidoOrden: 'asc' | 'desc' | 'proximos' | '' = '';

  // Control de paginación
  paginaActual: number = 1;
  eventosPorPagina: number = 6;

  constructor(
    private api: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  // Se ejecuta al iniciar el componente
  ngOnInit(): void {
    this.api.getEventos().subscribe({
      next: (res) => {
        this.eventosOriginales = res;
        this.eventos = [...res];

        // Escucha cambios en los parámetros de la URL (ej: ?q=)
        this.route.queryParams.subscribe(params => {
          const termino = params['q']?.toLowerCase();
          if (termino && termino.trim() !== '') {
            this.filtrarEventos(termino);
          } else {
            this.eventos = [...this.eventosOriginales];
          }
        });
      },
      error: (err) => console.error('Error cargando eventos:', err)
    });
  }

  // Devuelve true si la fecha del evento es anterior a la actual
  esPasado(fecha: string): boolean {
    return new Date(fecha).getTime() < Date.now();
  }

  // Filtra los eventos que coincidan con el término de búsqueda
  filtrarEventos(termino: string) {
    this.eventos = this.eventosOriginales.filter(e =>
      e.titulo?.toLowerCase().includes(termino) ||
      e.lugar?.toLowerCase().includes(termino) ||
      e.descripcion?.toLowerCase().includes(termino) ||
      e.fechaInicio?.toLowerCase().includes(termino) ||
      e.fecha_fin?.toLowerCase().includes(termino)
    );
  }

  // Elimina el parámetro de búsqueda y recarga todos los eventos
  limpiarBusqueda() {
    this.router.navigate(['/eventos']);
  }

  // Ordena los eventos según el valor actual de `sentidoOrden`
  ordenarEventos(): void {
    const hoy = new Date().getTime();
    
    if (this.sentidoOrden === 'asc') {
      this.eventos = [...this.eventosOriginales].sort((a, b) =>
        new Date(a.fechaInicio).getTime() - new Date(b.fechaInicio).getTime()
      );
    } else if (this.sentidoOrden === 'desc') {
      this.eventos = [...this.eventosOriginales].sort((a, b) =>
        new Date(b.fechaInicio).getTime() - new Date(a.fechaInicio).getTime()
      );
    } else if (this.sentidoOrden === 'proximos') {
      this.eventos = this.eventosOriginales
        .filter(e => new Date(e.fecha_fin).getTime() >= hoy)
        .sort((a, b) =>
          new Date(a.fechaInicio).getTime() - new Date(b.fechaInicio).getTime()
        );
    } else {
      // Sin orden: se restauran los eventos originales
      this.eventos = [...this.eventosOriginales];
    }
  }

  // Getter: obtiene los eventos correspondientes a la página actual
  get eventosPaginados(): any[] {
    const inicio = (this.paginaActual - 1) * this.eventosPorPagina;
    const fin = inicio + this.eventosPorPagina;
    return this.eventos.slice(inicio, fin);
  }

  // Getter: calcula el número total de páginas disponibles
  get totalPaginas(): number {
    return Math.ceil(this.eventos.length / this.eventosPorPagina);
  }
}
