import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CarritoService } from '../../services/carrito.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pago',
  imports: [CommonModule, FormsModule],
  templateUrl: './pago.component.html',
  styleUrl: './pago.component.css'
})
export class PagoComponent {

  // Dirección seleccionada para el envío
  direccionSeleccionada: number | null = null;

  // Lista de direcciones del usuario
  direcciones: any[] = [];

  // Controla la visibilidad del formulario de nueva/edición de dirección
  mostrarFormularioDireccion: boolean = false;

  // Lista de ciudades sugeridas según el texto introducido
  ciudadesFiltradas: string[] = [];

  // Mensaje de error para mostrar al usuario
  mensajeError: string | null = null;

  // ID de la dirección que se está editando (si aplica)
  direccionEditandoId: number | null = null;

  // Listado completo de ciudades disponibles en España
  ciudadesEspaña: string[] = [ /* listado completo */ ];

  // Método de pago seleccionado (a implementar si se requiere)
  metodoPago: string = '';

  // Objeto que representa la nueva dirección a guardar o editar
  nuevaDireccionObj = {
    ciudad: '',
    codigoPostal: '',
    direccion: '',
    telefono: ''
  };

  // Objeto que representa los datos de la tarjeta del usuario
  tarjeta = {
    nombre: '',
    numero: '',
    fecha: '',
    cvv: ''
  };

  // Constructor con inyección de servicios: API, Carrito y Router
  constructor(private api: ApiService, public carrito: CarritoService, private router: Router) {}

  // Al inicializar el componente, se cargan las direcciones del usuario actual
  ngOnInit() {
    const idUsuario = Number(localStorage.getItem('idUsuario'));
    this.api.getDireccionesPorUsuario().subscribe({
      next: (direcciones: any[]) => {
        this.direcciones = direcciones;
      },
      error: (err: any) => console.error('Error cargando direcciones', err)
    });
  }

  // Verifica que todos los campos de la tarjeta están completos
  datosTarjetaCompletos() {
    const { nombre, numero, fecha, cvv } = this.tarjeta;

    const numeroLimpio = numero.replace(/\s/g, '');
    const cvvValido = /^\d{3}$/.test(cvv);
    const fechaValida = /^\d{2}\/\d{2}$/.test(fecha);

    return (
      nombre.trim() &&
      numeroLimpio.length >= 13 && numeroLimpio.length <= 19 &&
      fechaValida &&
      cvvValido
    );
  }

  // Guarda una nueva dirección para el usuario, si se cumplen los requisitos
  guardarNuevaDireccion() {
    if (this.direcciones.length >= 5) {
      this.mensajeError = 'No se pueden guardar más de 5 direcciones.';
      setTimeout(() => this.mensajeError = null, 4000);
      return;
    }
    
    const idUsuario = Number(localStorage.getItem('idUsuario'));
    const { ciudad, codigoPostal, direccion, telefono } = this.nuevaDireccionObj;

    if (!ciudad || !codigoPostal || !direccion || !telefono) {
      this.mensajeError = 'Todos los campos son obligatorios.';
      return;
    }

    const nueva = { ciudad, codigoPostal, direccion, telefono, idUsuario };

    this.api.guardarDireccion(nueva).subscribe({
      next: (res: any) => {
        this.direcciones.push(res);
        this.direccionSeleccionada = res.id;
        this.nuevaDireccionObj = { ciudad: '', codigoPostal: '', direccion: '', telefono: '' };
        this.mostrarFormularioDireccion = false;
      },
      error: (err: any) => console.error('Error guardando dirección', err)
    });
  }

  // Elimina una dirección si no está vinculada a pedidos
  eliminarDireccion(id: number) {
    this.api.eliminarDireccion(id).subscribe({
      next: () => {
        this.direcciones = this.direcciones.filter(dir => dir.id !== id);
        if (this.direccionSeleccionada === id) {
          this.direccionSeleccionada = null;
        }
      },
      error: err => {
        console.error('Error eliminando dirección', err);

        if (err.status === 401 || err.error?.message?.includes("vinculada")) {
          this.mensajeError = 'No se puede eliminar la dirección porque está vinculada a un pedido.';
        } else {
          this.mensajeError = 'Error inesperado al eliminar la dirección.';
        }

        setTimeout(() => {
          this.mensajeError = null;
        }, 4000);
      }
    });
  }

  // Prepara el formulario para editar una dirección ya existente
  editarDireccion(dir: any) {
    this.mostrarFormularioDireccion = true;
    this.direccionEditandoId = dir.id;
    this.nuevaDireccionObj = {
      ciudad: dir.ciudad,
      codigoPostal: dir.codigoPostal,
      direccion: dir.direccion,
      telefono: dir.telefono
    };
  }

  // Da formato con espacios a los números de la tarjeta
  formatearNumeroTarjeta() {
    this.tarjeta.numero = this.tarjeta.numero
      .replace(/\D/g, '')
      .replace(/(.{4})/g, '$1 ')
      .trim();
  }

  // Da formato tipo MM/AA a la fecha de la tarjeta
  formatearFechaTarjeta() {
    this.tarjeta.fecha = this.tarjeta.fecha
      .replace(/\D/g, '')
      .replace(/^(\d{2})(\d{1,2})/, '$1/$2')
      .substr(0, 5);
  }

  // Asegura que el CVV sean solo números
  soloNumeros(event: Event) {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/\D/g, '');
    this.tarjeta.cvv = input.value;
  }

  // Filtro de autocompletado de ciudades mientras se escribe
  onInputCiudad() {
    const texto = this.nuevaDireccionObj.ciudad.toLowerCase();
    this.ciudadesFiltradas = this.ciudadesEspaña
      .filter(ciudad => ciudad.toLowerCase().startsWith(texto))
      .slice(0, 5);
  }

  // Selecciona una ciudad desde la sugerencia
  seleccionarCiudad(ciudad: string) {
    this.nuevaDireccionObj.ciudad = ciudad;
    this.ciudadesFiltradas = [];
  }

  // Validaciones para campos individuales
  esCodigoPostalValido(): boolean {
    return /^\d{5}$/.test(this.nuevaDireccionObj.codigoPostal);
  }

  esTelefonoValido(): boolean {
    return /^[6789]\d{8}$/.test(this.nuevaDireccionObj.telefono);
  }

  esNombreTitularValido(): boolean {
    return /^[A-Za-zÁÉÍÓÚáéíóúñÑ\s]+$/.test(this.tarjeta.nombre.trim());
  }

  // Confirma el pago, valida datos, genera pedido y vacía el carrito
  confirmarPago() {
    const idUsuario = Number(localStorage.getItem('idUsuario'));
    const idDireccion = this.direccionSeleccionada;
    const total = this.carrito.getTotal();
    const productos = this.carrito.getCarrito();

    if (total <= 0 || productos.length === 0) {
      alert('El carrito está vacío o el total no es válido');
      return;
    }

    const detalles = productos.map(p => ({
      idVinilo: p.id,
      cantidad: p.cantidad,
      precio: p.precio
    }));

    const nuevoPedido = {
      estado: 'PENDIENTE',
      fechaPedido: new Date().toISOString(),
      total,
      idDireccionEnvio: idDireccion,
      idUsuario,
      detalles
    };

    this.api.crearPedido(nuevoPedido).subscribe({
      next: () => {
        this.carrito.vaciar();
        this.carrito.guardarCarritoEnServidor().subscribe({
          next: () => this.router.navigate(['/pedidos']),
          error: err => {
            console.error('Error vaciando carrito en servidor', err);
            this.router.navigate(['/pedidos']); // Redirige igual aunque falle
          }
        });
      },
      error: err => console.error('Error creando pedido', err)
    });
  }

  // Actualiza una dirección existente con los nuevos datos del formulario
  actualizarDireccion() {
    const idUsuario = Number(localStorage.getItem('idUsuario'));
    const { ciudad, codigoPostal, direccion, telefono } = this.nuevaDireccionObj;

    const actualizada = {
      id: this.direccionEditandoId!,
      ciudad,
      codigoPostal,
      direccion,
      telefono,
      idUsuario
    };

    this.api.actualizarDireccion(actualizada.id!, actualizada).subscribe({
      next: (res: any) => {
        const index = this.direcciones.findIndex(d => d.id === actualizada.id);
        if (index !== -1) {
          this.direcciones[index] = res;
        }

        this.direccionEditandoId = null;
        this.nuevaDireccionObj = { ciudad: '', codigoPostal: '', direccion: '', telefono: '' };
        this.mostrarFormularioDireccion = false;
      },
      error: (err: any) => {
        console.error('Error actualizando dirección', err);
      }
    });
  }

  // Cancela la edición/creación de una dirección y limpia el formulario
  cancelarFormularioDireccion() {
    this.mostrarFormularioDireccion = false;
    this.direccionEditandoId = null;
    this.nuevaDireccionObj = { ciudad: '', codigoPostal: '', direccion: '', telefono: '' };
  }

  // Verifica si se puede confirmar el pago
  get puedeConfirmarPago() {
  return this.direccionSeleccionada &&
         this.metodoPago &&
         (this.metodoPago !== 'tarjeta' ||
          (this.datosTarjetaCompletos() && this.esNombreTitularValido()));
  }

}
