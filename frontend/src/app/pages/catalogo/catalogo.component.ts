import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CarritoService } from '../../services/carrito.service';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {
  productos: any[] = [];
  productosOriginales: any[] = [];

  artistas: string[] = [];
  generos: string[] = [];

  selectedArtistas: string[] = [];
  selectedGeneros: string[] = [];
  precioMin: number | null = null;
  precioMax: number | null = null;

  mostrarFiltros = false;
  categoriaActiva: 'artista' | 'genero' | 'precio' | null = null;

  constructor(private api: ApiService, private carrito: CarritoService) {}

  ngOnInit(): void {
    this.api.getVinilos().subscribe({
      next: (res) => {
        this.productos = res;
        this.productosOriginales = [...res];

        this.artistas = [...new Set(res.map(p => p.artista))]
          .sort()
          .map(artista => {
            const count = res.filter(p => p.artista === artista).length;
            return `${artista} (${count})`;
          });

        this.generos = [...new Set(res.map(p => p.genero))]
          .sort()
          .map(genero => {
            const count = res.filter(p => p.genero === genero).length;
            return `${genero} (${count})`;
          });
      },
      error: (err) => console.error('Error cargando vinilos:', err)
    });

    const token = localStorage.getItem('token');
    if (token) {
      setTimeout(() => this.carrito.cargarCarritoDelServidor(), 50);
    }
  }

  aniadirAlCarrito(producto: any) {
    this.carrito.aniadir(producto);
  }

  aplicarFiltros() {
    this.productos = this.productosOriginales.filter(p => {
      const artistaFiltro = this.selectedArtistas.length === 0 || this.selectedArtistas.includes(`${p.artista} (${this.contar(p.artista, 'artista')})`);
      const generoFiltro = this.selectedGeneros.length === 0 || this.selectedGeneros.includes(`${p.genero} (${this.contar(p.genero, 'genero')})`);
      const precioFiltro =
        (!this.precioMin || p.precio >= this.precioMin) &&
        (!this.precioMax || p.precio <= this.precioMax);
  
      return artistaFiltro && generoFiltro && precioFiltro;
    });
  
    this.mostrarFiltros = false;
    this.categoriaActiva = null;
  }
  

  eliminarFiltros() {
    this.selectedArtistas = [];
    this.selectedGeneros = [];
    this.precioMin = null;
    this.precioMax = null;
    this.productos = [...this.productosOriginales];
  }

  toggleSeleccion(valor: string, tipo: 'artista' | 'genero') {
    const array = tipo === 'artista' ? this.selectedArtistas : this.selectedGeneros;
    const index = array.indexOf(valor);
  
    if (index === -1) {
      array.push(valor);
    } else {
      array.splice(index, 1);
    }
  }
  

  toggleFiltros() {
    this.mostrarFiltros = !this.mostrarFiltros;
    this.categoriaActiva = null;
  }

  private contar(valor: string, tipo: 'artista' | 'genero'): number {
    return this.productosOriginales.filter(p => p[tipo] === valor).length;
  }

  ordenarProductos(criterio: string) {
    switch (criterio) {
      case 'precio':
        this.productos.sort((a, b) => a.precio - b.precio);
        break;
      case 'titulo':
        this.productos.sort((a, b) => a.titulo.localeCompare(b.titulo));
        break;
      default:
        this.productos = [...this.productosOriginales];
        this.aplicarFiltros(); 
        break;
    }
  }
  
  
}
