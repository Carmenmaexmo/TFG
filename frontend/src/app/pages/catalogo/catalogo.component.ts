import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CarritoService } from '../../services/carrito.service';
import { UiService } from '../../services/ui.service';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {
  // Listado de productos y copia original para restaurar
  productos: any[] = [];
  productosOriginales: any[] = [];

  // Listas de artistas, géneros y proveedores para los filtros
  artistas: string[] = [];
  generos: string[] = [];
  proveedores: any[] = [];

  // Filtros seleccionados por el usuario
  selectedArtistas: string[] = [];
  selectedGeneros: string[] = [];
  selectedProveedores: number[] = [];
  precioMin: number | null = null;
  precioMax: number | null = null;

  // Control de paginación
  paginaActual: number = 1;
  vinilosPorPagina: number = 20;

  // Variables para control visual
  estaSobrePapelera = false;
  busquedaActiva: string | null = null;
  mostrarFiltros = false;
  categoriaActiva: 'artista' | 'genero' | 'precio' | 'proveedor' | null = null;
  mostrarFormularioVinilo = false;
  mostrarFormProveedor = false;
  mensajeErrorVinilo: string | null = null;
  mensajeErrorProveedor: string | null = null;

  // Búsqueda y filtrado de proveedores
  busquedaProveedor: string = '';
  proveedoresFiltrados: any[] = [];

  // Estado de edición
  proveedorEditando: any = null;
  viniloEditandoId: number | null = null;

  // Géneros disponibles para autocompletar
  generosFiltrados: string[] = [];
  generosDisponibles: string[] = [
    'Rock', 'Pop', 'Jazz', 'Hip-Hop', 'Metal', 'Clásica', 'Reggae', 'Funk', 'Soul', 'Indie', 'Electrónica', 'Folk', 'Alternativo', 'Punk', 'Country', 'Grunge', 'Flamenco', 'Latino', 'R&B', 'Gospel', 'Blues', 'Bossa Nova', 'Salsa'
  ];

  // Modelo del nuevo vinilo
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

  // Modelo del nuevo proveedor
  nuevoProveedor = {
    nombre: '',
    direccion: '',
    email: '',
    telefono: ''
  };

  constructor(
    private api: ApiService,
    private carrito: CarritoService,
    private route: ActivatedRoute,
    private router: Router,
    private ui: UiService
  ) {}

  @ViewChild('gridContainer') gridContainerRef!: ElementRef;

  // Carga inicial de vinilos, filtros y proveedores
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

    // Si hay token, cargar carrito del servidor
    const token = localStorage.getItem('token');
    if (token) {
      setTimeout(() => this.carrito.cargarCarritoDelServidor(), 50);
    }
  }

  // Comprueba si el usuario tiene rol de administrador o empleado
  rolAdminOEmpleado(): boolean {
    const roles = JSON.parse(localStorage.getItem('roles') || '[]');
    return roles.includes('ROLE_ADMINISTRADOR') || roles.includes('ROLE_EMPLEADO');
  }

  // Añade un vinilo al carrito
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
    this.ui.mostrarCarrito();
  }

  // Aplica filtros de artista, género, precio y proveedor
  aplicarFiltros() {
    this.productos = this.productosOriginales.filter(p => {
      const artistaFiltro = this.selectedArtistas.length === 0 || this.selectedArtistas.includes(`${p.artista} (${this.contar(p.artista, 'artista')})`);
      const generoFiltro = this.selectedGeneros.length === 0 || this.selectedGeneros.includes(`${p.genero} (${this.contar(p.genero, 'genero')})`);
      const precioFiltro = (!this.precioMin || p.precio >= this.precioMin) && (!this.precioMax || p.precio <= this.precioMax);
      const proveedorFiltro = this.selectedProveedores.length === 0 || this.selectedProveedores.includes(p.proveedor?.id);
      return artistaFiltro && generoFiltro && precioFiltro && proveedorFiltro;
    });
    this.mostrarFiltros = false;
    this.categoriaActiva = null;
  }

  // Elimina todos los filtros seleccionados y restaura el listado original
  eliminarFiltros() {
    this.selectedArtistas = [];
    this.selectedGeneros = [];
    this.precioMin = null;
    this.precioMax = null;
    this.productos = [...this.productosOriginales];
  }

  // Añade o quita un filtro de artista o género según el tipo indicado
  toggleSeleccion(valor: string, tipo: 'artista' | 'genero') {
    const array = tipo === 'artista' ? this.selectedArtistas : this.selectedGeneros;
    const index = array.indexOf(valor);

    if (index === -1) {
      array.push(valor);
    } else {
      array.splice(index, 1);
    }
  }

  // Alterna la visibilidad del panel de filtros
  toggleFiltros() {
    this.mostrarFiltros = !this.mostrarFiltros;
    this.categoriaActiva = null;
  }

  // Cuenta cuántos productos tienen el valor indicado según tipo
  private contar(valor: string, tipo: 'artista' | 'genero'): number {
    return this.productosOriginales.filter(p => p[tipo] === valor).length;
  }

  // Ordena los productos por precio, título, o los restaura
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

  // Filtra los productos por término de búsqueda (título, artista o género)
  filtrarPorBusqueda(termino: string) {
    this.productos = this.productosOriginales.filter(p =>
      p.titulo.toLowerCase().includes(termino) ||
      p.artista.toLowerCase().includes(termino) ||
      p.genero.toLowerCase().includes(termino)
    );
  }

  // Alterna la selección de un proveedor en los filtros
  toggleSeleccionProveedor(id: number) {
    const index = this.selectedProveedores.indexOf(id);
    if (index === -1) {
      this.selectedProveedores.push(id);
    } else {
      this.selectedProveedores.splice(index, 1);
    }
  }

  // Elimina los parámetros de búsqueda navegando a la ruta limpia
  limpiarBusqueda() {
    this.router.navigate(['/catalogo']);
  }

  // Crea o actualiza un vinilo dependiendo de si hay un ID en edición
  crearVinilo() {
    if (!this.nuevoVinilo.titulo || !this.nuevoVinilo.artista || !this.nuevoVinilo.genero || !this.nuevoVinilo.precio) {
      this.mensajeErrorVinilo = 'Debe rellenar todos los campos para crear o actualizar el vinilo.';
      setTimeout(() => this.mensajeErrorVinilo = null, 4000);
      return;
    }

    if (this.viniloEditandoId) {
      // Actualiza el vinilo existente
      const datosActualizar = { id: this.viniloEditandoId, ...this.nuevoVinilo };
      this.api.actualizarVinilo(this.viniloEditandoId, datosActualizar).subscribe({
        next: (res) => {
          const idx = this.productos.findIndex(v => v.id === res.id);
          if (idx !== -1) {
            this.productos[idx] = res;
            this.productosOriginales[idx] = res;
          }
          this.limpiarFormularioVinilo();
        },
        error: () => {
          alert('Error al actualizar el vinilo.');
        }
      });
    } else {
      // Crea un nuevo vinilo
      this.api.crearVinilo(this.nuevoVinilo).subscribe({
        next: (res) => {
          this.productos.push(res);
          this.productosOriginales.push(res);
          this.limpiarFormularioVinilo();
        },
        error: (err) => console.error('Error creando vinilo:', err)
      });
    }
  }

  // Limpia los campos del formulario de vinilo
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

  // Cancela la edición del vinilo y limpia el formulario
  cancelarFormularioVinilo() {
    this.limpiarFormularioVinilo();
  }

  // Filtra los géneros disponibles para autocompletar el campo de género
  filtrarGeneros() {
    const texto = this.nuevoVinilo.genero.toLowerCase();
    this.generosFiltrados = this.generosDisponibles
      .filter(g => g.toLowerCase().includes(texto))
      .slice(0, 5);
  }

  // Asigna un género seleccionado desde el autocompletado
  seleccionarGenero(genero: string) {
    this.nuevoVinilo.genero = genero;
    this.generosFiltrados = [];
  }

  // Guarda un nuevo proveedor después de validar los campos del formulario
  guardarProveedor() {
    const { nombre, direccion, email, telefono } = this.nuevoProveedor;

    // Validación de campos obligatorios
    if (!nombre || !direccion || !email || !telefono) {
      this.mensajeErrorProveedor = 'Todos los campos del proveedor son obligatorios.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }

    // Validación del formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      this.mensajeErrorProveedor = 'El email no tiene un formato válido.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }

    // Validación del formato de teléfono (9 dígitos empezando por 6, 7 o 9)
    const telefonoRegex = /^[6789]\d{8}$/;
    if (!telefonoRegex.test(telefono)) {
      this.mensajeErrorProveedor = 'El teléfono debe tener 9 dígitos y comenzar por 6, 7 o 9.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }

    // Llamada al backend para guardar el proveedor
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
        this.mensajeErrorProveedor = 'Error al guardar el proveedor.';
      }
    });
  }

  // Elimina un proveedor si no tiene vinilos asociados
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
        this.mensajeErrorVinilo = 'No se pudo eliminar el proveedor porque tiene vinilos asociados.';
        setTimeout(() => this.mensajeErrorVinilo = null, 4000);
      }
    });
  }

  // Activa el formulario para editar un proveedor existente
  editarProveedor(proveedor: any) {
    this.mostrarFormProveedor = true;
    this.proveedorEditando = { ...proveedor }; // Copia profunda para edición
    this.nuevoProveedor = { ...proveedor };
  }

  // Actualiza los datos de un proveedor después de validarlos
  actualizarProveedor() {
    const { id } = this.proveedorEditando;

    // Validación de campos obligatorios
    if (!this.nuevoProveedor.nombre || !this.nuevoProveedor.direccion || !this.nuevoProveedor.email || !this.nuevoProveedor.telefono) {
      this.mensajeErrorProveedor = 'Todos los campos del proveedor son obligatorios.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }

    // Validación del email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.nuevoProveedor.email)) {
      this.mensajeErrorProveedor = 'El email no tiene un formato válido.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }

    // Validación del teléfono
    const telefonoRegex = /^[6789]\d{8}$/;
    if (!telefonoRegex.test(this.nuevoProveedor.telefono)) {
      this.mensajeErrorProveedor = 'El teléfono debe tener 9 dígitos y comenzar por 6, 7 o 9.';
      setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      return;
    }

    // Enviar actualización al backend
    this.api.actualizarProveedor(id, this.nuevoProveedor).subscribe({
      next: () => {
        // Refresca la lista completa de proveedores tras la edición
        this.api.getProveedores().subscribe({
          next: (proveedores) => {
            this.proveedores = proveedores;
            this.filtrarProveedores(); // También actualiza los proveedores filtrados
          },
          error: (err) => console.error('Error recargando proveedores', err)
        });
        this.cancelarEdicionProveedor();
      },
      error: (err) => {
        console.error('Error actualizando proveedor:', err);
        this.mensajeErrorProveedor = 'No se pudo actualizar el proveedor.';
        setTimeout(() => this.mensajeErrorProveedor = null, 4000);
      }
    });
  }

  // Cancela la edición del proveedor y reinicia los formularios
  cancelarEdicionProveedor() {
    this.mostrarFormProveedor = false;
    this.proveedorEditando = null;
    this.nuevoProveedor = { nombre: '', direccion: '', email: '', telefono: '' };
    this.mensajeErrorProveedor = null;
  }

  // Filtra proveedores por nombre o email en tiempo real
  filtrarProveedores() {
    console.log('Aplicando filtros con proveedores:', this.selectedProveedores);
    const termino = this.busquedaProveedor.toLowerCase();
    this.proveedoresFiltrados = this.proveedores
      .filter(p =>
        p.nombre.toLowerCase().includes(termino) ||
        p.email.toLowerCase().includes(termino)
      )
      .slice(0, 5); // Limita a los primeros 5 resultados
  }

  // Calcula los vinilos que se deben mostrar según la página actual
  get vinilosPaginados() {
    const inicio = (this.paginaActual - 1) * this.vinilosPorPagina;
    const fin = inicio + this.vinilosPorPagina;
    return this.productos.slice(inicio, fin);
  }

  // Calcula el número total de páginas necesarias para paginar los productos
  get totalPaginas() {
    return Math.ceil(this.productos.length / this.vinilosPorPagina);
  }

  // Cambia de página en el catálogo si está dentro del rango válido
  cambiarPagina(nuevaPagina: number) {
    if (nuevaPagina >= 1 && nuevaPagina <= this.totalPaginas) {
      this.paginaActual = nuevaPagina;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  // Ajusta cuántos vinilos se deben mostrar por pantalla según resolución
  ajustarVinilosPorPagina() {
    const anchoPantalla = window.innerWidth;
    const altoPantalla = window.innerHeight;

    const columnas = Math.floor(anchoPantalla / 220); // Aproximación del ancho de card
    const filas = Math.floor(altoPantalla / 400);     // Aproximación del alto de card

    this.vinilosPorPagina = columnas * filas;
  }

  // Elimina un vinilo tras confirmar con el usuario
  eliminarVinilo(vinilo: any) {

    this.api.eliminarVinilo(vinilo.id).subscribe({
      next: () => {
        this.productos = this.productos.filter(v => v.id !== vinilo.id);
        this.productosOriginales = this.productosOriginales.filter(v => v.id !== vinilo.id);
      },
      error: () => {
        alert('Error al eliminar el vinilo.');
      }
    });
  }

  // Prepara el formulario con los datos del vinilo para editar
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

  // Navega al detalle de un vinilo seleccionado
  verDetalleVinilo(id: number) {
    this.router.navigate(['/vinilo', id]);
  }
  }