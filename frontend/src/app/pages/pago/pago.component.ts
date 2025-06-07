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
  direccionSeleccionada: number | null = null;
  direcciones: any[] = [];
  mostrarFormularioDireccion: boolean = false;
  ciudadesFiltradas: string[] = [];
  mensajeError: string | null = null;
  direccionEditandoId: number | null = null;


  ciudadesEspaña: string[] = [
    "Álava", "Albacete", "Alicante", "Almería", "Asturias", "Ávila", "Badajoz",
    "Barcelona", "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellón", "Ciudad Real",
    "Córdoba", "Cuenca", "Girona", "Granada", "Guadalajara", "Guipúzcoa", "Huelva",
    "Huesca", "Islas Baleares", "Jaén", "La Coruña", "La Rioja", "Las Palmas", "León",
    "Lérida", "Lugo", "Madrid", "Málaga", "Murcia", "Navarra", "Orense", "Palencia",
    "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria",
    "Tarragona", "Teruel", "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora",
    "Zaragoza"
  ]
  ;

  metodoPago: string = '';
  nuevaDireccionObj = {
    ciudad: '',
    codigoPostal: '',
    direccion: '',
    telefono: ''
  };

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


  tarjeta = {
    nombre: '',
    numero: '',
    fecha: '',
    cvv: ''
  };

  constructor(private api: ApiService, public carrito: CarritoService, private router: Router) {}

  ngOnInit() {
    const idUsuario = Number(localStorage.getItem('idUsuario'));
    this.api.getDireccionesPorUsuario().subscribe({
      next: (direcciones: any[]) => {
        this.direcciones = direcciones;
      },
      error: (err: any) => console.error('Error cargando direcciones', err)
    });
  }

  datosTarjetaCompletos() {
    const { nombre, numero, fecha, cvv } = this.tarjeta;
    return nombre && numero && fecha && cvv;
  }

  guardarNuevaDireccion() {
    if (this.direcciones.length >= 5) {
      alert('Máximo 5 direcciones permitidas');
      return;
    }    
    
    const idUsuario = Number(localStorage.getItem('idUsuario'));
    const { ciudad, codigoPostal, direccion, telefono } = this.nuevaDireccionObj;

    if (!ciudad || !codigoPostal || !direccion || !telefono) {
      alert('Todos los campos son obligatorios');
      return;
    }

    const nueva = {
      ciudad,
      codigoPostal,
      direccion,
      telefono,
      idUsuario
    };

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
          this.mensajeError = '❌ No se puede eliminar la dirección porque está vinculada a un pedido.';
  
          // Ocultar el mensaje después de 4 segundos
          setTimeout(() => {
            this.mensajeError = null;
          }, 4000);
        } else {
          this.mensajeError = '❌ Error inesperado al eliminar la dirección.';
          setTimeout(() => {
            this.mensajeError = null;
          }, 4000);
        }
      }
    });
  }
  
  formatearNumeroTarjeta() {
    // Quita espacios y no numéricos, luego agrupa en bloques de 4
    this.tarjeta.numero = this.tarjeta.numero
      .replace(/\D/g, '')
      .replace(/(.{4})/g, '$1 ')
      .trim();
  }
  
  formatearFechaTarjeta() {
    this.tarjeta.fecha = this.tarjeta.fecha
      .replace(/\D/g, '')
      .replace(/^(\d{2})(\d{1,2})/, '$1/$2')
      .substr(0, 5);
  }
  
  soloNumeros(event: Event) {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/\D/g, '');
    this.tarjeta.cvv = input.value;
  }

  onInputCiudad() {
    const texto = this.nuevaDireccionObj.ciudad.toLowerCase();
    this.ciudadesFiltradas = this.ciudadesEspaña
      .filter(ciudad => ciudad.toLowerCase().startsWith(texto))
      .slice(0, 5);
  }
  
  seleccionarCiudad(ciudad: string) {
    this.nuevaDireccionObj.ciudad = ciudad;
    this.ciudadesFiltradas = [];
  }

  esCodigoPostalValido(): boolean {
    return /^\d{5}$/.test(this.nuevaDireccionObj.codigoPostal);
  }
  
  esTelefonoValido(): boolean {
    return /^[6789]\d{8}$/.test(this.nuevaDireccionObj.telefono);
  }
  
  esNombreTitularValido(): boolean {
    return /^[A-Za-zÁÉÍÓÚáéíóúñÑ\s]+$/.test(this.tarjeta.nombre.trim());
  }  

  confirmarPago() {
    const idUsuario = Number(localStorage.getItem('idUsuario'));
    const idDireccion = this.direccionSeleccionada;
    const total = this.carrito.getTotal();
    const productos = this.carrito.getCarrito(); // <-- obtén los items
  
    if (total <= 0 || productos.length === 0) {
      alert('El carrito está vacío o el total no es válido');
      return;
    }
  
    const detalles = productos.map(p => ({
      idVinilo: p.id,
      cantidad: p.cantidad,
      precio: p.precio
    }));

    console.log('Detalles del pedido:', detalles);
  
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
      this.carrito.vaciar();  // 🔁 ya vacía localmente
      this.carrito.guardarCarritoEnServidor().subscribe({
        next: () => this.router.navigate(['/pedidos']),
        error: err => {
          console.error('Error vaciando carrito en servidor', err);
          this.router.navigate(['/pedidos']);  // redirige igual aunque falle
        }
      });
    },
    error: err => console.error('Error creando pedido', err)
  });

  }  

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

  cancelarFormularioDireccion() {
  this.mostrarFormularioDireccion = false;
  this.direccionEditandoId = null;
  this.nuevaDireccionObj = { ciudad: '', codigoPostal: '', direccion: '', telefono: '' };
  }



  
}
