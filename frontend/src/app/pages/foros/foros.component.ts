import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

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

    mostrarModalEliminar = false;
    temaAEliminar: any = null;

    constructor(private api: ApiService, private router: Router, private route: ActivatedRoute) {}

    ngOnInit(): void {
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

        this.route.queryParams.subscribe(params => {
            this.terminoBusqueda = (params['q'] || '').toLowerCase();
            this.cargarForoYTemas();
        });
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
                error: err => console.error('Error al eliminar tema:', err)
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
}
