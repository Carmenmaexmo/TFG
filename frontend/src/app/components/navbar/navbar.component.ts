import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CarritoService } from '../../services/carrito.service';
import { UiService } from '../../services/ui.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  // Estado del menú desplegable del usuario
  dropdownOpen: boolean = false;

  // Estado del desplegable "Administración"
  adminDropdownOpen: boolean = false;

  // Visibilidad del menú en pantallas pequeñas
  navAbierto: boolean = false;
  menuAbierto: boolean = false;

  // Carrito
  carritoCantidad = 0;
  carritoContenido: any[] = [];
  mostrarCarritoSlide = false;

  // Búsqueda
  searchVisible: boolean = false;
  searchTerm: string = '';

  // Seguimiento del usuario actual
  lastUser = '';

  constructor(
    private router: Router,
    private carrito: CarritoService,
    private uiService: UiService
  ) {
    // Suscribirse al carrito y actualizar cantidad y contenido
    this.carrito.getCarritoObservable().subscribe(c => {
      this.carritoContenido = c;
      this.carritoCantidad = c.length;
    });

    // Mostrar/ocultar el slide del carrito desde UiService
    this.uiService.carritoVisible$.subscribe(visible => {
      this.mostrarCarritoSlide = visible;
    });
  }

  // Detectar cambios manualmente (p. ej., cambios en localStorage)
  ngDoCheck(): void {
    const currentUser = localStorage.getItem('nombreUsuario');
    if (this.lastUser !== currentUser) {
      this.dropdownOpen = false;
      this.lastUser = currentUser || '';
      this.carrito.cargarCarritoDelServidor();
    }
  }

  /** Si el usuario está logueado (hay token en localStorage) */
  isLogged(): boolean {
    return !!localStorage.getItem('token');
  }

  /** Nombre del usuario logueado */
  getUsername(): string {
    return localStorage.getItem('nombreUsuario') || '';
  }

  /** Logout con guardado del carrito */
  logout() {
    this.carrito.guardarCarritoEnServidor().subscribe({
      next: () => console.log('Carrito guardado al cerrar sesión'),
      error: err => console.error('Error guardando carrito al cerrar sesión', err),
      complete: () => {
        localStorage.clear();
        this.carrito.vaciar();
        this.router.navigate(['/login']);
      }
    });
  }

  /** Verifica si el usuario tiene alguno de los roles permitidos */
  hasRole(rolesPermitidos: string[]): boolean {
    const rolesStr = localStorage.getItem('roles');
    const roles = rolesStr ? JSON.parse(rolesStr) : [];
    return roles.some((role: string) => {
      const cleanRole = role.replace('ROLE_', '').toUpperCase();
      return rolesPermitidos.map(r => r.toUpperCase()).includes(cleanRole);
    });
  }

  /** Mostrar/ocultar buscador */
  toggleSearch() {
    this.searchVisible = !this.searchVisible;
  }

  /** Ejecutar búsqueda según ruta actual */
  onSearch() {
    const query = this.searchTerm.trim();
    const currentUrl = this.router.url.split('?')[0];

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
    } else if (currentUrl.includes('/temas')) {
      navegar(currentUrl);
    } else if (currentUrl.includes('/pedidos')) {
      navegar(currentUrl);
    } else {
      console.log('Buscador no soportado en esta ruta');
    }

    if (!query) {
      this.searchVisible = false;
    }
  }

  /** Mostrar/ocultar el slide del carrito */
  toggleCarrito() {
    this.mostrarCarritoSlide = !this.mostrarCarritoSlide;
  }

  abrirCarrito() {
    this.mostrarCarritoSlide = true;
  }

  /** Funciones de gestión de productos en el carrito */
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

  /** Navega a la pantalla de pago */
  irAPago() {
    this.mostrarCarritoSlide = false;
    this.router.navigate(['/pago']);
  }
}
