import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-foros',
    templateUrl: './foros.component.html',
    imports: [CommonModule, FormsModule, RouterModule],
    styleUrls: ['./foros.component.css']
})
export class ForosComponent implements OnInit {
    foro: any = null;
    temas: any[] = [];
    mostrarFormulario: boolean = false;
    temasOriginal: any[] = [];
    terminoBusqueda: string = '';
    editarTemaActivoId: number | null = null;
    bloqueoUsuario: any = null;
    mostrarModalBloqueado: boolean = false;
    mostrarModalEliminar = false;
    temaAEliminar: any = null;
    paginaActual: number = 1;
    temasPorPagina: number = 6; 
    modoEdicionForo: boolean = false;

    constructor(private api: ApiService, private router: Router, private route: ActivatedRoute) {}

    ngOnInit(): void {
      const idUsuario = Number(localStorage.getItem('idUsuario'));

      this.api.getBloqueosForo().subscribe({
        next: bloqueos => {
          const bloqueo = bloqueos.find((b: any) => b.usuario?.idUsuario === idUsuario);
          if (bloqueo) {
            this.bloqueoUsuario = bloqueo;
            this.mostrarModalBloqueado = true;
          } else {
            this.cargarForoYTemas();
          }
        },
        error: err => console.error('Error verificando bloqueos:', err)
      });

      // Escuchar cambios en ?q y actualizar lista de temas
      this.route.queryParams.subscribe(params => {
        this.terminoBusqueda = (params['q'] || '').toLowerCase();

        //Si ya tengo los temas cargados, aplicar filtro directamente
        if (this.temasOriginal.length > 0) {
          this.temas = this.filtrarTemas(this.temasOriginal, this.terminoBusqueda);
        }
      });
    }

    volverAlInicio() {
      this.router.navigate(['/']);
    }

    verComentarios(idTema: number) {
        this.router.navigate(['/temas', idTema]);
    }

    nuevoTema = {
        titulo: '',
        contenido: ''
    };

    crearTema() {
        const foroId = this.foro?.id;
        const idUsuario = Number(localStorage.getItem('idUsuario'));

        if (!foroId || !this.nuevoTema.titulo.trim() || !this.nuevoTema.contenido.trim()) {
            alert('Completa todos los campos');
            return;
        }

        const tema = {
            titulo: this.nuevoTema.titulo.trim(),
            contenido: this.nuevoTema.contenido.trim(),
            idForo: foroId,
            idUsuario: idUsuario
        };

        this.api.crearTemaEnForo(foroId, tema).subscribe({
            next: (nuevo) => {
                this.temas.push(nuevo);
                this.nuevoTema = { titulo: '', contenido: '' };
                this.mostrarFormulario = false;
            },
            error: err => console.error('Error creando tema:', err)
        });
    }

    cargarForoYTemas() {
        this.api.getForo().subscribe({
            next: (res) => {
                this.foro = res[0];
                if (this.foro?.id) {
                    this.api.getTemas(this.foro.id).subscribe({
                        next: temas => {
                            console.log('Temas cargados:', temas);
                            this.temasOriginal = temas;
                            this.temas = this.filtrarTemas(this.temasOriginal, this.terminoBusqueda);
                        },
                        error: err => console.error('Error cargando temas:', err)
                    });
                }
            },
            error: err => console.error('Error cargando foro:', err)
        });
    }

    filtrarTemas(lista: any[], termino: string): any[] {
        if (!termino) return lista;
        return lista.filter(t =>
            t.titulo.toLowerCase().includes(termino) ||
            t.contenido.toLowerCase().includes(termino)
        );
    }

    activarEdicionTema(id: number) {
        this.editarTemaActivoId = id;
    }

    guardarEdicionTema(tema: any) {
        if (!tema.titulo.trim() || !tema.contenido.trim()) {
            alert('Los campos no pueden estar vacíos.');
            return;
        }
        this.api.actualizarTema(tema.id, tema).subscribe({
            next: actualizado => {
                Object.assign(tema, actualizado);
                this.editarTemaActivoId = null;
                alert('Tema actualizado.');
            },
            error: err => console.error('Error al actualizar tema:', err)
        });
    }

    cancelarEdicion() {
        this.editarTemaActivoId = null;
    }

    abrirModalEliminar(tema: any) {
        this.temaAEliminar = tema;
        this.mostrarModalEliminar = true;
    }

    confirmarEliminar() {
      if (this.temaAEliminar) {
        this.api.eliminarTema(this.temaAEliminar.id).subscribe({
          next: () => {
            this.temas = this.temas.filter(t => t.id !== this.temaAEliminar.id);
            this.cerrarModalEliminar();
          },
          error: err => {
          this.cerrarModalEliminar();

          console.error('Error al eliminar tema:', err);

          const mensaje = err.error?.message?.toLowerCase?.() || '';
          
          if (
            (err.status === 401 || err.status === 403 || err.status === 409) &&
            (mensaje.includes('comentarios') || mensaje === '' || typeof err.error === 'string')
          ) {
          Swal.fire({
            icon: 'error',
            title: '<span style="font-family:\'Segoe UI\', \'Roboto\', sans-serif; font-weight:600;">No se puede eliminar</span>',
            html: '<p style="font-family:\'Segoe UI\', \'Roboto\', sans-serif;">Este tema tiene comentarios y no puede ser eliminado.</p>',
            confirmButtonColor: '#fcd34d',
            customClass: {
              popup: 'rounded-3xl shadow-lg',
              confirmButton: 'text-black font-medium px-4 py-2'
            }
          });
          } else {
            Swal.fire({
              icon: 'error',
              title: 'Error inesperado',
              text: 'Ocurrió un error al intentar eliminar el tema.',
              confirmButtonColor: '#fcd34d'
            });
          }
        }
        });
      }
    }

    cerrarModalEliminar() {
        this.temaAEliminar = null;
        this.mostrarModalEliminar = false;
    }

    hasRole(rolesPermitidos: string[]): boolean {
        const rolesStr = localStorage.getItem('roles');
        const roles = rolesStr ? JSON.parse(rolesStr) : [];
        return roles.some((role: string) => {
            const cleanRole = role.replace('ROLE_', '').toUpperCase();
            return rolesPermitidos.map(r => r.toUpperCase()).includes(cleanRole);
        });
    }

    esCreadorTema(tema: any): boolean {
        const idUsuario = Number(localStorage.getItem('idUsuario'));
        return tema.usuario?.idUsuario === idUsuario;
    }

    puedeEditarTema(tema: any): boolean {
        return this.hasRole(['ADMINISTRADOR', 'EMPLEADO']) || this.esCreadorTema(tema);
    }

    puedeEliminarTema(tema: any): boolean {
        return this.hasRole(['ADMINISTRADOR', 'EMPLEADO']) || this.esCreadorTema(tema);
    }

    get temasPaginados(): any[] {
    const inicio = (this.paginaActual - 1) * this.temasPorPagina;
    const fin = inicio + this.temasPorPagina;
    return this.temas.slice(inicio, fin);
    }

    get totalPaginas(): number {
      return Math.ceil(this.temas.length / this.temasPorPagina);
    }

    puedeEditarForo(): boolean {
    return this.hasRole(['ADMINISTRADOR', 'EMPLEADO']);
    }

   activarEdicionForo() {
  if (this.puedeEditarForo()) {
    this.modoEdicionForo = true;
  }
}

guardarForoEditado() {
  if (!this.foro.nombre.trim() || !this.foro.descripcion.trim()) {
    this.modoEdicionForo = false;
    return;
  }

  this.api.actualizarForo(this.foro.id, {
    nombre: this.foro.nombre,
    descripcion: this.foro.descripcion
  }).subscribe({
    next: () => {
      this.modoEdicionForo = false;
    },
    error: err => {
      console.error('Error actualizando foro:', err);
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'No se pudo actualizar el foro.',
        confirmButtonColor: '#fcd34d'
      });
    }
  });
  }


}
