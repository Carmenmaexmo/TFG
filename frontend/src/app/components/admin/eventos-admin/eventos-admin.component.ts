import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-eventos-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './eventos-admin.component.html',
  styleUrls: ['./eventos-admin.component.css']
})
export class EventosAdminComponent implements OnInit {
  eventos: any[] = [];
  asistentesEvento: any[] = [];
  eventoSeleccionado: any = null;
  filtro = '';
  filtroUsuario = '';
  creandoEvento = false;
  editandoId: number | null = null;

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

  cargarEventos() {
    this.api.getEventos().subscribe(data => {
      this.eventos = data;
    });
  }

  get eventosFiltrados() {
    return this.eventos.filter(e =>
      (!this.filtro || e.titulo.toLowerCase().includes(this.filtro.toLowerCase()) || e.lugar.toLowerCase().includes(this.filtro.toLowerCase()))
    );
  }

  abrirCrear() {
    this.creandoEvento = true;
  }

  cerrarCrear() {
    this.creandoEvento = false;
    this.nuevoEvento = { titulo: '', descripcion: '', fechaInicio: '', fecha_fin: '', lugar: '', descuento: 0 };
  }

  guardarNuevoEvento() {
    const nuevo = {
      ...this.nuevoEvento,
      fechaInicio: new Date(this.nuevoEvento.fechaInicio).toISOString().slice(0, 19),
      fecha_fin: new Date(this.nuevoEvento.fecha_fin).toISOString().slice(0, 19),
      descuento: this.nuevoEvento.descuento
    };

    console.log('Crear evento:', nuevo);

    this.api.crearEvento(nuevo).subscribe(data => {
      this.eventos.push(data);
      this.cerrarCrear();
    }, error => {
      console.error('❌ Error al crear evento', error);
    });
  }

  activarEdicion(evento: any) {
    this.editandoId = evento.id;
  }

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
      console.error('❌ Error al actualizar evento', error);
    });
  }

  eliminarEvento(id: number) {
    if (confirm('¿Seguro que deseas eliminar este evento?')) {
      this.api.eliminarEvento(id).subscribe(() => {
        this.eventos = this.eventos.filter(e => e.id !== id);
      });
    }
  }

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

  cerrarAsistentes() {
    this.eventoSeleccionado = null;
    this.asistentesEvento = [];
    this.filtroUsuario = '';
  }

  get asistentesFiltrados() {
    return this.asistentesEvento.filter(a =>
      (!this.filtroUsuario || a.usuario.nombre.toLowerCase().includes(this.filtroUsuario.toLowerCase()) ||
       a.usuario.email.toLowerCase().includes(this.filtroUsuario.toLowerCase()))
    );
  }

  marcarAsistencia(asistencia: any, confirmado: boolean) {
    if (asistencia.id == null) {
      const nuevo = {
        idUsuario: asistencia.usuario.idUsuario,
        idEvento: this.eventoSeleccionado.id,
        confirmado: confirmado
      };

      console.log('👉 Crear asistente (POST):', nuevo);
      this.api.crearAsistenciaEvento(nuevo).subscribe((data) => {
        asistencia.id = data.id;
        asistencia.confirmado = data.confirmado;
        console.log('✅ Asistencia creada y guardada en BD:', data);
      }, error => {
        console.error('❌ Error al crear asistencia', error);
      });

    } else {
      const update = { confirmado: confirmado };
      console.log('👉 Actualizar asistente (PUT):', asistencia.id, update);

      this.api.actualizarAsistenciaEvento(asistencia.id, update).subscribe((data) => {
        asistencia.confirmado = data.confirmado;
        console.log('✅ Asistencia actualizada y guardada en BD:', data);
      }, error => {
        console.error('❌ Error al actualizar asistencia', error);
      });
    }
  }
}
