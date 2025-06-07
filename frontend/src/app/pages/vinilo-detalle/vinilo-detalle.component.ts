import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CarritoService } from '../../services/carrito.service';
import { UiService } from '../../services/ui.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-vinilo-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './vinilo-detalle.component.html',
  styleUrls: ['./vinilo-detalle.component.css']
})
export class ViniloDetalleComponent implements OnInit {
  vinilo: any;
  proveedor: any;
  roles: string[] = [];
  vinilosSugeridos: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private carrito: CarritoService,
    private ui: UiService,
    private location: Location,
    private router: Router
  ) {}

 ngOnInit(): void {
 this.route.paramMap.subscribe(params => {
  const id = +params.get('id')!;
  this.api.getViniloPorId(id).subscribe({
    next: (v) => {
      this.vinilo = v;
      this.api.getProveedorPorId(v.proveedor?.id).subscribe(p => this.proveedor = p);
      this.cargarVinilosSugeridos(v.genero, v.id); // si usas sugerencias
      window.scrollTo({ top: 0, behavior: 'smooth' }); 
    },
    error: () => console.error('Error cargando vinilo')
  });
});
  const rolesStr = localStorage.getItem('roles');
  this.roles = rolesStr ? JSON.parse(rolesStr) : [];
}

  esAdminOEmpleado(): boolean {
    return this.roles.includes('ROLE_ADMINISTRADOR') || this.roles.includes('ROLE_EMPLEADO');
  }

  aniadirAlCarrito() {
    this.carrito.aniadir(this.vinilo);
    this.ui.mostrarCarrito();
  }

  volver() {
  this.location.back();
  }

  cargarVinilosSugeridos(genero: string, idActual: number) {
  this.api.getVinilos().subscribe((todos: any[]) => {
    this.vinilosSugeridos = todos
      .filter(v => v.genero === genero && v.id !== idActual)
      .slice(0, 5);
  });
  }

  verDetalleVinilo(id: number) {
    this.ui.ocultarCarrito(); // si quieres cerrar el slide del carrito
    this.router.navigate(['/vinilo', id]);
  }

}
