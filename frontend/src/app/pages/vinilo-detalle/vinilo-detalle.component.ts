import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CarritoService } from '../../services/carrito.service';
import { UiService } from '../../services/ui.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Location } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-vinilo-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './vinilo-detalle.component.html',
  styleUrls: ['./vinilo-detalle.component.css']
})
export class ViniloDetalleComponent implements OnInit {
  vinilo: any;                   // Vinilo actual que se está visualizando
  proveedor: any;                // Información del proveedor del vinilo
  roles: string[] = [];          // Roles del usuario actual
  vinilosSugeridos: any[] = []; // Lista de vinilos sugeridos por género

  constructor(
    private route: ActivatedRoute,       // Para acceder a los parámetros de la ruta
    private api: ApiService,             // Servicio para acceder a la API
    private carrito: CarritoService,     // Servicio para gestionar el carrito de compras
    private ui: UiService,               // Servicio para manejar el UI (como mostrar u ocultar el slide del carrito)
    private location: Location,          // Para permitir volver atrás
    private router: Router               // Para navegación manual entre rutas
  ) {}

  ngOnInit(): void {
    // Obtener ID del vinilo desde los parámetros de la URL
    this.route.paramMap.subscribe(params => {
      const id = +params.get('id')!;
      
      // Cargar datos del vinilo
      this.api.getViniloPorId(id).subscribe({
        next: (v) => {
          this.vinilo = v;

          // Cargar proveedor asociado
          this.api.getProveedorPorId(v.proveedor?.id).subscribe(p => this.proveedor = p);

          // Cargar vinilos del mismo género (excepto el actual)
          this.cargarVinilosSugeridos(v.genero, v.id);

          // Subir al inicio de la página al cargar
          window.scrollTo({ top: 0, behavior: 'smooth' }); 
        },
        error: () => console.error('Error cargando vinilo')
      });
    });

    // Cargar roles desde localStorage
    const rolesStr = localStorage.getItem('roles');
    this.roles = rolesStr ? JSON.parse(rolesStr) : [];
  }

  /**
   * Determina si el usuario actual tiene rol de administrador o empleado
   */
  esAdminOEmpleado(): boolean {
    return this.roles.includes('ROLE_ADMINISTRADOR') || this.roles.includes('ROLE_EMPLEADO');
  }

  /**
   * Añade el vinilo al carrito y muestra el carrito deslizable si esta logueado.
   */
  aniadirAlCarrito() {
    const token = localStorage.getItem('token');

    if (!token) {
      // Mostrar aviso si no está logueado
      Swal.fire({
        title: 'Para añadir al carrito debes iniciar sesión',
        icon: 'warning',
        confirmButtonText: 'Ir al login',
        confirmButtonColor: '#fcd34d',
        background: '#1c1c1e',
        color: '#f8f8f8',
        customClass: {
          popup: 'rounded-3xl shadow-lg',
          title: 'text-lg font-semibold',
          htmlContainer: 'text-sm',
          confirmButton: 'text-black font-medium px-4 py-2'
        }
      }).then(() => {
        this.router.navigate(['/login']);
      });

      return;
    }

    this.carrito.aniadir(this.vinilo);
    this.ui.mostrarCarrito();
  }

  /**
   * Navega a la página anterior utilizando el historial del navegador
   */
  volver() {
    this.location.back();
  }

  /**
   * Carga una lista de vinilos sugeridos que comparten el mismo género
   * y excluye el vinilo actual
   */
  cargarVinilosSugeridos(genero: string, idActual: number) {
    this.api.getVinilos().subscribe((todos: any[]) => {
      this.vinilosSugeridos = todos
        .filter(v => v.genero === genero && v.id !== idActual)
        .slice(0, 5); // Se limita a mostrar máximo 5 sugerencias
    });
  }

  /**
   * Cambia a la vista detallada de otro vinilo, cerrando antes el slide del carrito
   */
  verDetalleVinilo(id: number) {
    this.ui.ocultarCarrito(); // Oculta el slide del carrito si está abierto
    this.router.navigate(['/vinilo', id]);
  }

}
