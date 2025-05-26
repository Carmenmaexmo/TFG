import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CarritoService } from '../../services/carrito.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  dropdownOpen: boolean = false; 
  carritoCantidad = 0;
  carritoVisible: boolean = false;
  carritoContenido: any[] = [];
  mostrarCarritoSlide = false;


  constructor(private router: Router, private carrito: CarritoService) {
    this.carrito.getCarritoObservable().subscribe(c => {
      this.carritoContenido = c;             
      this.carritoCantidad = c.length;
    });
  }
  lastUser = '';


  ngDoCheck(): void {
    const currentUser = localStorage.getItem('nombreUsuario');
    if (this.lastUser !== currentUser) {
      // Cambió el usuario (ej: acaba de loguearse o desloguearse)
      this.dropdownOpen = false;
      this.lastUser = currentUser || '';
      this.carrito.cargarCarritoDelServidor();
    }

  }

  isLogged(): boolean {
    return !!localStorage.getItem('token');
  }

  getUsername(): string {
    return localStorage.getItem('nombreUsuario') || '';
  }

  logout() {
    this.carrito.guardarCarritoEnServidor();
    localStorage.clear();
    this.carrito.vaciar();
    this.router.navigate(['/login']);
  }

  searchVisible: boolean = false;
  searchTerm: string = '';

  toggleSearch() {
    this.searchVisible = !this.searchVisible;
  }

  onSearch() {
    const query = this.searchTerm.trim();
    const currentUrl = this.router.url.split('?')[0];  // ignora query params
  
    // Define a helper para navegar con o sin término de búsqueda
    const navegar = (ruta: string) => {
      this.router.navigate([ruta], {
        queryParams: query ? { q: query } : {},
        queryParamsHandling: query ? 'merge' : undefined
      });
    };
  
    if (currentUrl.includes('/catalogo')) {
      navegar('/catalogo');
    } else if (currentUrl.includes('/eventos')) {
      navegar('/eventos');
    } else if (currentUrl.includes('/foros')) {
      navegar('/foros');
    } else {
      console.log('🔍 Buscador no soportado en esta ruta');
    }
  
    this.searchVisible = false;
  }
  
  

  toggleCarrito() {
    this.mostrarCarritoSlide = !this.mostrarCarritoSlide;
  }  
  
  aumentarCantidad(id: number) {
    const producto = this.carritoContenido.find(p => p.id === id);
    if (producto) {
      this.carrito.aniadir(producto);
    }
  }
  
  reducirCantidad(id: number) {
    this.carrito.quitar(id);
  }
  
  eliminarProducto(id: number) {
    this.carrito.eliminar(id);
  }
  
  getTotalCarrito(): number {
    return this.carrito.getTotal();
  }

  irAPago() {
    this.mostrarCarritoSlide = false; // cierra el carrito
    this.router.navigate(['/pago']);  // navega a /pago
  }
  

}
