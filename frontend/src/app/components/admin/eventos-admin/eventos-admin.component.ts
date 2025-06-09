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
    const nuevo = {
      ...this.nuevoEvento,
      fechaInicio: new Date(this.nuevoEvento.fechaInicio).toISOString().slice(0, 19),
      fecha_fin: new Date(this.nuevoEvento.fecha_fin).toISOString().slice(0, 19),
      descuento: this.nuevoEvento.descuento
    };

    this.api.crearEvento(nuevo).subscribe(data => {
      this.eventos.push(data);
      this.cerrarCrear();
    }, error => {
      console.error('Error al crear evento', error);
    });
  }

  // Activa el modo edición para un evento concreto
  activarEdicion(evento: any) {
    this.editandoId = evento.id;
  }

  // Guarda los cambios realizados en el evento en edición
  guardarEdicion(evento: any) {
    const actualizado = {
      titulo: evento.titulo,
      lugar: evento.lugar,
      fechaInicio: evento.fechaInicio,
      fecha_fin: evento.fecha_fin,
      descripcion: evento.descripcion,
      descuento: evento.descuento
    };

    this.api.actualizarEvento(evento.id, actualizado).subscribe(() => {
      this.editandoId = null;
    }, error => {
      console.error('Error al actualizar evento', error);
    });
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
