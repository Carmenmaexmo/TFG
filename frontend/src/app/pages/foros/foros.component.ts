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
    // Datos del foro y lista de temas
    foro: any = null;
    temas: any[] = [];
    temasOriginal: any[] = [];

    // Estados del componente
    mostrarFormulario: boolean = false;
    terminoBusqueda: string = '';
    editarTemaActivoId: number | null = null;
    bloqueoUsuario: any = null;
    mostrarModalBloqueado: boolean = false;
    mostrarModalEliminar = false;
    temaAEliminar: any = null;
    paginaActual: number = 1;
    temasPorPagina: number = 6;
    modoEdicionForo: boolean = false;

    mensajeError: string = ''; // Mensaje de error

    constructor(private api: ApiService, private router: Router, private route: ActivatedRoute) {}

    ngOnInit(): void {
        // Obtener ID de usuario desde el localStorage
        const idUsuario = Number(localStorage.getItem('idUsuario'));

        // Comprobar si el usuario está bloqueado del foro
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

        // Escuchar cambios en los parámetros de búsqueda (?q)
        this.route.queryParams.subscribe(params => {
            this.terminoBusqueda = (params['q'] || '').toLowerCase();
            if (this.temasOriginal.length > 0) {
                this.temas = this.filtrarTemas(this.temasOriginal, this.terminoBusqueda);
            }
        });
    }

    // Navegar al inicio
    volverAlInicio() {
        this.router.navigate(['/']);
    }

    // Ir a los comentarios de un tema específico
    verComentarios(idTema: number) {
        this.router.navigate(['/temas', idTema]);
    }

    // Modelo de datos para la creación de un nuevo tema
    nuevoTema = {
        titulo: '',
        contenido: ''
    };

    // Crear un nuevo tema en el foro
    crearTema() {
        const foroId = this.foro?.id;
        const idUsuario = Number(localStorage.getItem('idUsuario'));

         // Validación: foro cargado y campos obligatorios
        if (!foroId || !this.nuevoTema.titulo.trim() || !this.nuevoTema.contenido.trim()) {
          this.mensajeError = 'Debes rellenar el título y el contenido del tema.';
          setTimeout(() => this.mensajeError = '', 4000);
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

    // Cargar el foro y sus temas asociados
    cargarForoYTemas() {
        this.api.getForo().subscribe({
            next: (res) => {
                this.foro = res[0];
                if (this.foro?.id) {
                    this.api.getTemas(this.foro.id).subscribe({
                        next: temas => {
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

    // Filtrar lista de temas por término de búsqueda
    filtrarTemas(lista: any[], termino: string): any[] {
        if (!termino) return lista;
        return lista.filter(t =>
            t.titulo.toLowerCase().includes(termino) ||
            t.contenido.toLowerCase().includes(termino)
        );
    }

    // Activar modo edición para un tema
    activarEdicionTema(id: number) {
        this.editarTemaActivoId = id;
    }

    // Guardar cambios en un tema editado
    guardarEdicionTema(tema: any) {
          if (!tema.titulo.trim() || !tema.contenido.trim()) {
          this.mensajeError = 'El título y el contenido no pueden estar vacíos.';
          setTimeout(() => this.mensajeError= '', 4000);
          return;
        }
        this.api.actualizarTema(tema.id, tema).subscribe({
            next: actualizado => {
                Object.assign(tema, actualizado);
                this.editarTemaActivoId = null;
            },
            error: err => console.error('Error al actualizar tema:', err)
        });
    }

    // Cancelar edición del tema
    cancelarEdicion() {
        this.editarTemaActivoId = null;
    }

    // Mostrar modal de confirmación para eliminar tema
    abrirModalEliminar(tema: any) {
        this.temaAEliminar = tema;
        this.mostrarModalEliminar = true;
    }

    // Confirmar eliminación del tema
   confirmarEliminar() {
    if (!this.temaAEliminar) return;

    this.api.eliminarTema(this.temaAEliminar.id).subscribe({
      next: () => {
        this.temas = this.temas.filter(t => t.id !== this.temaAEliminar.id);
        this.cerrarModalEliminar();
      },
      error: err => {
        this.cerrarModalEliminar();

        Swal.fire({
          icon: 'error',
          title: 'No se puede eliminar',
          html: 'Este tema tiene comentarios y no puede ser eliminado.',
          confirmButtonColor: '#fcd34d',
          customClass: {
            popup: 'rounded-3xl shadow-lg',
            confirmButton: 'text-black font-medium px-4 py-2'
          }
        });
      }
    });
  }

    // Cierra el modal de eliminación
    cerrarModalEliminar() {
        this.temaAEliminar = null;
        this.mostrarModalEliminar = false;
    }

    // Comprueba si el usuario tiene alguno de los roles permitidos
    hasRole(rolesPermitidos: string[]): boolean {
        const rolesStr = localStorage.getItem('roles');
        const roles = rolesStr ? JSON.parse(rolesStr) : [];
        return roles.some((role: string) => {
            const cleanRole = role.replace('ROLE_', '').toUpperCase();
            return rolesPermitidos.map(r => r.toUpperCase()).includes(cleanRole);
        });
    }

    // Verifica si el usuario actual es el creador del tema
    esCreadorTema(tema: any): boolean {
        const idUsuario = Number(localStorage.getItem('idUsuario'));
        return tema.usuario?.idUsuario === idUsuario;
    }

    // Verifica si el usuario puede editar un tema
    puedeEditarTema(tema: any): boolean {
        return this.hasRole(['ADMINISTRADOR', 'EMPLEADO']) || this.esCreadorTema(tema);
    }

    // Verifica si el usuario puede eliminar un tema
    puedeEliminarTema(tema: any): boolean {
        return this.hasRole(['ADMINISTRADOR', 'EMPLEADO']) || this.esCreadorTema(tema);
    }

    // Devuelve los temas visibles en la página actual
    get temasPaginados(): any[] {
        const inicio = (this.paginaActual - 1) * this.temasPorPagina;
        const fin = inicio + this.temasPorPagina;
        return this.temas.slice(inicio, fin);
    }

    // Calcula el número total de páginas de temas
    get totalPaginas(): number {
        return Math.ceil(this.temas.length / this.temasPorPagina);
    }

    // Verifica si el usuario puede editar los datos del foro
    puedeEditarForo(): boolean {
        return this.hasRole(['ADMINISTRADOR', 'EMPLEADO']);
    }

    // Activa el modo edición del foro si el usuario tiene permiso
    activarEdicionForo() {
        if (this.puedeEditarForo()) {
            this.modoEdicionForo = true;
        }
    }

    // Guarda los cambios realizados en los datos del foro
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
