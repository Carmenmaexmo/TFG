import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class CatalogoComponent implements OnInit {
  productos: any[] = [];

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getVinilos().subscribe({
      next: (res) => {
        this.productos = res;
        console.log(' Vinilos cargados:', this.productos);
      },
      error: (err) => {
        console.error('Error cargando vinilos:', err);
      }
    });
  }
}
