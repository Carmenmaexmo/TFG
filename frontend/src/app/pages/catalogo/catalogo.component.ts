import { AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CarritoService } from '../../services/carrito.service';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';


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
  proveedores: any[] = [];

  selectedArtistas: string[] = [];
  selectedGeneros: string[] = [];
  precioMin: number | null = null;
  precioMax: number | null = null;
  
  paginaActual: number = 1; 
  vinilosPorPagina: number = 20;

  estaSobrePapelera = false;

  busquedaActiva: string | null = null;

  mostrarFiltros = false;
  categoriaActiva: 'artista' | 'genero' | 'precio' | null = null;

  mostrarFormularioVinilo = false;
  mostrarFormProveedor = false;
  mensajeErrorVinilo: string | null = null;
  mensajeErrorProveedor: string | null = null;

  busquedaProveedor: string = '';
  proveedoresFiltrados: any[] = [];

  proveedorEditando: any = null;
  generosFiltrados: string[] = [];

  viniloEditandoId: number | null = null;

  generosDisponibles: string[] = [
    'Rock', 'Pop', 'Jazz', 'Hip-Hop', 'Metal', 'Clásica', 'Reggae', 'Funk', 'Soul','Indie', 'Electrónica', 'Folk', 'Alternativo', 'Punk', 'Country', 'Grunge', 'Flamenco', 'Latino', 'R&B', 'Gospel', 'Blues', 'Bossa Nova', 'Salsa'
  ];

  nuevoVinilo = {
    titulo: '',
    artista: '',
    descripcion: '',
    genero: '',
    imagen: '',
    precio: null,
    stock: null,
    idProveedor: null
  };  

  nuevoProveedor = {
    nombre: '',
    direccion: '',
    email: '',
    telefono: ''
  };

  constructor(private api: ApiService, private carrito: CarritoService, private route: ActivatedRoute, private router: Router) {}
  @ViewChild('gridContainer') gridContainerRef!: ElementRef;
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
  
          this.route.queryParams.subscribe(params => {
            const termino = params['q']?.toLowerCase();
            if (termino) {
              this.busquedaActiva = termino;
              this.filtrarPorBusqueda(termino);
            } else {
              this.busquedaActiva = null;
              this.productos = [...this.productosOriginales];
            }
          });
          this.api.getProveedores().subscribe({
            next: (res) => this.proveedores = res,
            error: (err) => console.error('Error cargando proveedores', err)
          });         
           
      },
      error: (err) => console.error('Error cargando vinilos:', err)
    });
  
    const token = localStorage.getItem('token');
    if (token) {
      setTimeout(() => this.carrito.cargarCarritoDelServidor(), 50);
    }
  }

  rolAdminOEmpleado(): boolean {
    const roles = JSON.parse(localStorage.getItem('roles') || '[]');
    return roles.includes('ROLE_ADMINISTRADOR') || roles.includes('ROLE_EMPLEADO');
  }
  
  aniadirAlCarrito(producto: any) {
    const token = localStorage.getItem('token');
  if (!token) {
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

  filtrarPorBusqueda(termino: string) {
    this.productos = this.productosOriginales.filter(p =>
      p.titulo.toLowerCase().includes(termino) ||
      p.artista.toLowerCase().includes(termino) ||
      p.genero.toLowerCase().includes(termino)
    );
  }
  
  limpiarBusqueda() {
    this.router.navigate(['/catalogo']);  // Navega sin el parámetro ?q
  }
  
  crearVinilo() {
    if (!this.nuevoVinilo.titulo || !this.nuevoVinilo.artista || !this.nuevoVinilo.genero || !this.nuevoVinilo.precio) {
      this.mensajeErrorVinilo = '❌ Debe rellenar todos los campos para crear o actualizar el vinilo.';
      setTimeout(() => this.mensajeErrorVinilo = null, 4000);
      return;
    }
  
    if (this.viniloEditandoId) {
      // ACTUALIZAR
      const datosActualizar = { id: this.viniloEditandoId, ...this.nuevoVinilo };
      this.api.actualizarVinilo(this.viniloEditandoId, datosActualizar).subscribe({
        next: (res) => {
          const idx = this.productos.findIndex(v => v.id === res.id);
          if (idx !== -1) {
            this.productos[idx] = res;
            this.productosOriginales[idx] = res;
          }
          this.limpiarFormularioVinilo();
          alert('✅ Vinilo actualizado correctamente.');
        },
        error: () => {
          alert('❌ Error al actualizar el vinilo.');
        }
      });
    } else {
      // CREAR
      this.api.crearVinilo(this.nuevoVinilo).subscribe({
        next: (res) => {
          this.productos.push(res);
          this.productosOriginales.push(res);
          this.limpiarFormularioVinilo();
          alert('✅ Vinilo creado correctamente.');
        },
        error: (err) => console.error('Error creando vinilo:', err)
      });
    }
  }

  limpiarFormularioVinilo() {
    this.nuevoVinilo = {
      titulo: '',
      artista: '',
      descripcion: '',
      genero: '',
      imagen: '',
      precio: null,
      stock: null,
      idProveedor: null
    };
    this.viniloEditandoId = null;
    this.mostrarFormularioVinilo = false;
    this.generosFiltrados = [];
    this.mensajeErrorVinilo = null;
  }  

  cancelarFormularioVinilo() {
    this.limpiarFormularioVinilo();
  }  
  

  filtrarGeneros() {
    const texto = this.nuevoVinilo.genero.toLowerCase();
    this.generosFiltrados = this.generosDisponibles
      .filter(g => g.toLowerCase().includes(texto))
      .slice(0, 5);
  }
  
  seleccionarGenero(genero: string) {
    this.nuevoVinilo.genero = genero;
    this.generosFiltrados = [];
  }

  guardarProveedor() {
    const { nombre, direccion, email, telefono } = this.nuevoProveedor;
  
    // Validación básica
    if (!nombre || !direccion || !email || !telefono) {
      this.mensajeErrorProveedor = '❌ Todos los campos del proveedor son obligatorios.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }
  
    // Validación de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      this.mensajeErrorProveedor = '❌ El email no tiene un formato válido.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }
  
    // Validación de teléfono (ej. 9 dígitos numéricos, empezando por 6, 7 o 9)
    const telefonoRegex = /^[6789]\d{8}$/;
    if (!telefonoRegex.test(telefono)) {
      this.mensajeErrorProveedor = '❌ El teléfono debe tener 9 dígitos numéricos y empezar por 6, 7 o 9.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }
  
    // Si pasa todo, guardar
    this.api.crearProveedor(this.nuevoProveedor).subscribe({
      next: (res) => {
        this.proveedores.push(res);
        this.nuevoVinilo.idProveedor = res.id;
        this.nuevoProveedor = { nombre: '', direccion: '', email: '', telefono: '' };
        this.mensajeErrorProveedor = null;
        this.mostrarFormProveedor = false;
      },
      error: (err) => {
        console.error('Error creando proveedor:', err);
        this.mensajeErrorProveedor = '❌ Error al guardar el proveedor.';
      }
    });
  }
  
  eliminarProveedor(id: number) {
    this.api.eliminarProveedor(id).subscribe({
      next: () => {
        this.proveedores = this.proveedores.filter(p => p.id !== id);
        if (this.nuevoVinilo.idProveedor === id) {
          this.nuevoVinilo.idProveedor = null;
        }
      },
      error: (err) => {
        console.error('Error al eliminar proveedor:', err);
        this.mensajeErrorVinilo = '❌ No se pudo eliminar el proveedor ya que tiene vinilos vinculados a él.';
        setTimeout(() => this.mensajeErrorVinilo = null, 4000);
      }
    });
  }

  editarProveedor(proveedor: any) {
    this.mostrarFormProveedor = true;
    this.proveedorEditando = { ...proveedor }; // copia para editar
    this.nuevoProveedor = { ...proveedor };
  }
  
  actualizarProveedor() {
    const { id } = this.proveedorEditando;
  
    // Validación básica
    if (!this.nuevoProveedor.nombre || !this.nuevoProveedor.direccion || !this.nuevoProveedor.email || !this.nuevoProveedor.telefono) {
      this.mensajeErrorProveedor = '❌ Todos los campos del proveedor son obligatorios.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }
    // Validación de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.nuevoProveedor.email)) {
      this.mensajeErrorProveedor = '❌ El email no tiene un formato válido.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }
    // Validación de teléfono (ej. 9 dígitos numéricos, empezando por 6, 7 o 9)
    const telefonoRegex = /^[6789]\d{8}$/;
    if (!telefonoRegex.test(this.nuevoProveedor.telefono)) {
      this.mensajeErrorProveedor = '❌ El teléfono debe tener 9 dígitos numéricos y empezar por 6, 7 o 9.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }
    // Si pasa todo, actualizar
  
    this.api.actualizarProveedor(id, this.nuevoProveedor).subscribe({
      next: (res) => {
        this.api.getProveedores().subscribe({
          next: (proveedores) => {
            this.proveedores = proveedores;
            this.filtrarProveedores();  // ACTUALIZA también los filtrados
          },
          error: (err) => console.error('Error recargando proveedores', err)
        });
        this.cancelarEdicionProveedor();
      },
      error: (err) => {
        console.error('Error actualizando proveedor:', err);
        this.mensajeErrorProveedor = '❌ No se pudo actualizar el proveedor.';
        setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      }
    });       
  }
  
  cancelarEdicionProveedor() {
    this.mostrarFormProveedor = false;
    this.proveedorEditando = null;
    this.nuevoProveedor = { nombre: '', direccion: '', email: '', telefono: '' };
    this.mensajeErrorProveedor = null;
  }

  filtrarProveedores() {
    const termino = this.busquedaProveedor.toLowerCase();
    this.proveedoresFiltrados = this.proveedores
      .filter(p =>
        p.nombre.toLowerCase().includes(termino) ||
        p.email.toLowerCase().includes(termino)
      )
      .slice(0, 5); // limitar a 5 resultados
  }
  
  get vinilosPaginados() {
    const inicio = (this.paginaActual - 1) * this.vinilosPorPagina;
    const fin = inicio + this.vinilosPorPagina;
    return this.productos.slice(inicio, fin);
  }  
  
  get totalPaginas() {
    return Math.ceil(this.productos.length / this.vinilosPorPagina);
  }
  
  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina >= 1 && nuevaPagina <= this.totalPaginas) {
      this.paginaActual = nuevaPagina;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  ajustarVinilosPorPagina() {
    const anchoPantalla = window.innerWidth;
    const altoPantalla = window.innerHeight;
  
    const columnas = Math.floor(anchoPantalla / 220); // ancho aproximado de una card
    const filas = Math.floor(altoPantalla / 400);     // alto aproximado de una card + padding
  
    this.vinilosPorPagina = columnas * filas;
  }
  
  eliminarVinilo(vinilo: any) {
    const confirmacion = confirm(`¿Estás seguro de eliminar "${vinilo.titulo}"?`);
    if (!confirmacion) return;
  
    this.api.eliminarVinilo(vinilo.id).subscribe({
      next: () => {
        this.productos = this.productos.filter(v => v.id !== vinilo.id);
        this.productosOriginales = this.productosOriginales.filter(v => v.id !== vinilo.id);
        alert('✅ Vinilo eliminado correctamente.');
      },
      error: () => {
        alert('❌ Error al eliminar el vinilo.');
      }
    });
  }
  
  editarVinilo(vinilo: any) {
    this.viniloEditandoId = vinilo.id;
    this.mostrarFormularioVinilo = true;
    this.nuevoVinilo = {
      titulo: vinilo.titulo,
      artista: vinilo.artista,
      descripcion: vinilo.descripcion,
      genero: vinilo.genero,
      imagen: vinilo.imagen,
      precio: vinilo.precio,
      stock: vinilo.stock,
      idProveedor: vinilo.idProveedor
    };
  }
  
  
  

}
