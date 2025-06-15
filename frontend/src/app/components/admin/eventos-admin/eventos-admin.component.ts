import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-eventos-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './eventos-admin.component.html',
  styleUrls: ['./eventos-admin.component.css']
})
export class EventosAdminComponent implements OnInit {
  // Lista de eventos disponibles
  eventos: any[] = [];

  // Lista de asistentes para el evento seleccionado
  asistentesEvento: any[] = [];

  // Evento actualmente seleccionado (para ver asistentes)
  eventoSeleccionado: any = null;

  // Filtro de búsqueda general por título o lugar
  filtro = '';

  // Filtro para buscar entre los asistentes
  filtroUsuario = '';

  // Bandera para controlar la visualización del formulario de creación
  creandoEvento = false;

  // Identificador del evento que se está editando (modo edición)
  editandoId: number | null = null;
  eventoEditando: any = null;

  //Error de validación para el formulario de creación y edición
  erroresEventoCrear: { [key: string]: string } = {};
  erroresEventoEdicion: { [eventoId: number]: { [key: string]: string } } = {};

  // Modelo del nuevo evento que se está creando
  nuevoEvento = {
    titulo: '',
    descripcion: '',
    fechaInicio: '',
    fecha_fin: '',
    lugar: '',
    descuento: 0
  };

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.cargarEventos();
  }

  // Carga todos los eventos desde la API
  cargarEventos() {
    this.api.getEventos().subscribe(data => {
      this.eventos = data;
    });
  }

  // Devuelve los eventos filtrados por título o lugar
  get eventosFiltrados() {
    return this.eventos.filter(e =>
      (!this.filtro || e.titulo.toLowerCase().includes(this.filtro.toLowerCase()) ||
       e.lugar.toLowerCase().includes(this.filtro.toLowerCase()))
    );
  }

  // Abre el formulario para crear un nuevo evento
  abrirCrear() {
    this.creandoEvento = true;
  }

  // Cierra el formulario de creación y limpia el modelo
  cerrarCrear() {
    this.creandoEvento = false;
    this.nuevoEvento = { titulo: '', descripcion: '', fechaInicio: '', fecha_fin: '', lugar: '', descuento: 0 };
  }

  // Guarda el nuevo evento en el servidor y lo añade a la lista
  guardarNuevoEvento() {
    this.erroresEventoCrear = {};

    const { titulo, descripcion, fechaInicio, fecha_fin, lugar, descuento } = this.nuevoEvento;

    if (!titulo || !descripcion || !fechaInicio || !fecha_fin || !lugar || descuento === null) {
      this.erroresEventoCrear['general'] = 'Todos los campos son obligatorios.';
      return;
    }

    const inicio = new Date(fechaInicio);
    const fin = new Date(fecha_fin);
    const ahora = new Date();

    if (inicio <= ahora) {
      this.erroresEventoCrear['fechaInicio'] = 'La fecha de inicio debe ser futura.';
      return;
    }

    if (fin <= inicio) {
      this.erroresEventoCrear['fechaFin'] = 'La fecha de fin debe ser posterior a la de inicio.';
      return;
    }

    const nuevo = {
      ...this.nuevoEvento,
      fechaInicio: inicio.toISOString().slice(0, 19),
      fecha_fin: fin.toISOString().slice(0, 19),
      descuento
    };

    this.api.crearEvento(nuevo).subscribe({
      next: data => {
        this.eventos.push(data);
        this.cerrarCrear();
      },
      error: error => {
        console.error('Error al crear evento', error);
        this.erroresEventoCrear['general'] = 'No se pudo crear el evento.';
      }
    });
  }


  // Activa el modo edición para un evento concreto
  activarEdicion(evento: any) {
    this.editandoId = evento.id;
    this.erroresEventoEdicion[evento.id] = {};

    this.eventoEditando = {
      ...evento,
      fechaInicio: this.convertirAFechaLocal(evento.fechaInicio),
      fecha_fin: this.convertirAFechaLocal(evento.fecha_fin)
    };
  }

  // Convierte una fecha ISO (2022-10-12T12:00) a una fecha local (2022-10-12T12:00-03:00)
  convertirAFechaLocal(fechaISO: string): string {
    const date = new Date(fechaISO);
    const offset = date.getTimezoneOffset();
    const local = new Date(date.getTime() - offset * 60 * 1000);
    return local.toISOString().slice(0, 16); // yyyy-MM-ddTHH:mm
  }



  // Guarda los cambios realizados en el evento en edición
  guardarEdicion(evento: any) {
    const errores: { [key: string]: string } = {};
    const inicio = new Date(this.eventoEditando.fechaInicio);
    const fin = new Date(this.eventoEditando.fecha_fin);
    const ahora = new Date();

    if (!this.eventoEditando.titulo || !this.eventoEditando.lugar || !this.eventoEditando.descripcion || !this.eventoEditando.fechaInicio || !this.eventoEditando.fecha_fin) {
      errores['general'] = 'Todos los campos son obligatorios.';
    } else {
      if (inicio <= ahora) errores['fechaInicio'] = 'La fecha de inicio debe ser futura.';
      if (fin <= inicio) errores['fechaFin'] = 'La fecha de fin debe ser posterior a la de inicio.';
    }

    if (Object.keys(errores).length > 0) {
      this.erroresEventoEdicion[evento.id] = errores;
      return;
    }

    const actualizado = {
      ...this.eventoEditando,
      fechaInicio: inicio.toISOString().slice(0, 19),
      fecha_fin: fin.toISOString().slice(0, 19)
    };

    this.api.actualizarEvento(evento.id, actualizado).subscribe({
      next: () => {
        Object.assign(evento, actualizado);
        this.editandoId = null;
        this.eventoEditando = null;
        delete this.erroresEventoEdicion[evento.id];
      },
      error: error => {
        console.error('Error al actualizar evento', error);
        this.erroresEventoEdicion[evento.id] = { general: 'No se pudo actualizar el evento.' };
      }
    });
  }

  // Cancela la edición de un evento y limpia el modelo
  cancelarEdicion() {
    this.editandoId = null;
    this.eventoEditando = null;
  }

  // Elimina un evento si se confirma la acción
  eliminarEvento(id: number) {
    this.api.eliminarEvento(id).subscribe({
      next: () => {
        this.eventos = this.eventos.filter(e => e.id !== id);
      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'No se puede eliminar',
          text: 'Este evento tiene asistentes y no puede ser eliminado.',
          confirmButtonColor: '#fcd34d',
          customClass: {
            popup: 'rounded-3xl shadow-lg',
            confirmButton: 'text-black font-medium px-4 py-2'
          }
        });
      }
    });
  }

  // Muestra los asistentes de un evento determinado
  verAsistentes(evento: any) {
    this.eventoSeleccionado = evento;

    this.api.getUsuarios().subscribe(usuarios => {
      this.api.getAsistenciasPorEvento(evento.id).subscribe(asistencias => {
        this.asistentesEvento = usuarios.map((usuario: any) => {
          const asistenciaExistente = asistencias.find((a: any) => a.usuario.idUsuario === usuario.idUsuario);
          return {
            id: asistenciaExistente ? asistenciaExistente.id : null,
            usuario: usuario,
            confirmado: asistenciaExistente ? asistenciaExistente.confirmado : false
          };
        });
      });
    });
  }

  // Cierra el panel de asistentes
  cerrarAsistentes() {
    this.eventoSeleccionado = null;
    this.asistentesEvento = [];
    this.filtroUsuario = '';
  }

  // Devuelve los asistentes filtrados por nombre o email
  get asistentesFiltrados() {
    return this.asistentesEvento.filter(a =>
      (!this.filtroUsuario || a.usuario.nombre.toLowerCase().includes(this.filtroUsuario.toLowerCase()) ||
       a.usuario.email.toLowerCase().includes(this.filtroUsuario.toLowerCase()))
    );
  }

  // Marca o actualiza la asistencia de un usuario a un evento
  marcarAsistencia(asistencia: any, confirmado: boolean) {
    if (asistencia.id == null) {
      // Crear nueva asistencia
      const nuevo = {
        idUsuario: asistencia.usuario.idUsuario,
        idEvento: this.eventoSeleccionado.id,
        confirmado: confirmado
      };

      this.api.crearAsistenciaEvento(nuevo).subscribe((data) => {
        asistencia.id = data.id;
        asistencia.confirmado = data.confirmado;
      }, error => {
        console.error('Error al crear asistencia', error);
      });

    } else {
      // Actualizar asistencia existente
      const update = { confirmado: confirmado };
      this.api.actualizarAsistenciaEvento(asistencia.id, update).subscribe((data) => {
        asistencia.confirmado = data.confirmado;
      }, error => {
        console.error('Error al actualizar asistencia', error);
      });
    }
  }
}
