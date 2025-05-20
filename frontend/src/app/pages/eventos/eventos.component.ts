import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-eventos',
  templateUrl: './eventos.component.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./eventos.component.css']
})
export class EventosComponent implements OnInit {
  eventos: any[] = [];
  eventosOriginales: any[] = [];
  sentidoOrden: 'asc' | 'desc' = 'asc';

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.api.getEventos().subscribe({
      next: (res) => {
        this.eventosOriginales = res;
        this.eventos = [...res];

        // Escucha parámetro de búsqueda
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
  
  esPasado(fecha: string): boolean {
    return new Date(fecha).getTime() < Date.now();
  }
  
  filtrarEventos(termino: string) {
    this.eventos = this.eventosOriginales.filter(e =>
      e.titulo?.toLowerCase().includes(termino) ||
      e.lugar?.toLowerCase().includes(termino) ||
      e.descripcion?.toLowerCase().includes(termino) ||
      e.fechaInicio?.toLowerCase().includes(termino) ||
      e.fecha_fin?.toLowerCase().includes(termino)
    );
  }

  limpiarBusqueda() {
    this.router.navigate(['/eventos']);  // Navega sin el parámetro ?q
  }

  ordenarEventos(): void {
    this.eventos.sort((a, b) => {
      const fechaA = new Date(a.fechaInicio).getTime();
      const fechaB = new Date(b.fechaInicio).getTime();
      return this.sentidoOrden === 'asc' ? fechaA - fechaB : fechaB - fechaA;
    });
  }
}
