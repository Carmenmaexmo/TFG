import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-tema-detalle',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tema-detalle.component.html',
  styleUrls: ['./tema-detalle.component.css']
})
export class TemaDetalleComponent implements OnInit, OnDestroy {
  comentariosJerarquicos: any[] = [];
  temaId: number = 0;
  nuevoComentario: string = '';
  comentarioPadreId: number | null = null;
  contextMenuVisible: boolean = false;
  contextMenuX: number = 0;
  contextMenuY: number = 0;
  comentarioContextual: any = null;
  respuestaAUsuario: string = '';
  idUsuarioActual: number = Number(localStorage.getItem('idUsuario'));
  comentarioEditandoId: number | null = null;
  comentarioEditadoTexto: string = '';

  constructor(private route: ActivatedRoute, private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.temaId = Number(this.route.snapshot.paramMap.get('id'));
    document.addEventListener('click', () => this.cerrarContextMenu());
    if (this.temaId) this.cargarComentarios();
  }

  ngOnDestroy(): void {
    document.removeEventListener('click', () => this.cerrarContextMenu());
  }

  cargarComentarios(): void {
    this.api.getComentarios(this.temaId).subscribe({
      next: (res) => {
        const jerarquicos = this.construirJerarquia(res);
        this.comentariosJerarquicos = this.marcarColapsos(jerarquicos);
      },
      error: (err) => console.error('Error al cargar comentarios:', err)
    });
  }

  construirJerarquia(comentarios: any[]): any[] {
    const mapa = new Map<number, any>();
    comentarios.forEach(c => mapa.set(c.id, { ...c, respuestas: [], mostrarRespuestas: false }));

    const jerarquia: any[] = [];
    comentarios.forEach(c => {
      if (c.comentarioPadre?.id) {
        const padre = mapa.get(c.comentarioPadre.id);
        if (padre) padre.respuestas.push(mapa.get(c.id));
      } else {
        jerarquia.push(mapa.get(c.id));
      }
    });

    return jerarquia;
  }

  marcarColapsos(lista: any[]): any[] {
    return lista.map(c => ({
      ...c,
      mostrarRespuestas: false,
      respuestas: c.respuestas ? this.marcarColapsos(c.respuestas) : []
    }));
  }

  toggleRespuestas(comentario: any) {
    comentario.mostrarRespuestas = !comentario.mostrarRespuestas;
  }

  volverAForo() {
    this.router.navigate(['/foros']);
  }

  enviarComentario() {
    if (!this.nuevoComentario.trim()) return;
    const comentario = {
      contenido: this.nuevoComentario.trim(),
      fechaComentario: new Date().toISOString(),
      idTema: this.temaId,
      idComentarioPadre: this.comentarioPadreId || null,
      idUsuario: this.idUsuarioActual
    };
    this.api.crearComentario(this.temaId, comentario).subscribe({
      next: () => {
        this.nuevoComentario = '';
        this.comentarioPadreId = null;
        this.scrollAlFormulario();
        this.cargarComentarios();
      },
      error: (err) => console.error('Error al crear comentario', err)
    });
  }

  scrollAlFormulario() {
    setTimeout(() => {
      const el = document.getElementById('formulario-comentario');
      if (el) el.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, 50);
  }

responderComentario() {
  this.comentarioPadreId = this.comentarioContextual?.id;
  this.respuestaAUsuario = this.comentarioContextual?.usuario?.nombreUsuario || 'Usuario';
  this.cerrarContextMenu();

  // ⚠ NUEVO: scroll al textarea al fondo
  setTimeout(() => {
    const textarea = document.getElementById('nuevo-comentario-textarea');
    if (textarea) {
      textarea.scrollIntoView({ behavior: 'smooth', block: 'center' });
      (textarea as HTMLTextAreaElement).focus();
    }
  }, 100);
}


  cancelarRespuesta() {
    this.comentarioPadreId = null;
    this.respuestaAUsuario = '';
  }

  onRightClick(event: MouseEvent, comentario: any) {
    event.stopPropagation();
    event.preventDefault();
    this.contextMenuVisible = true;
    this.contextMenuX = event.clientX;
    this.contextMenuY = event.clientY;
    this.comentarioContextual = comentario;
  }

  cerrarContextMenu() {
    this.contextMenuVisible = false;
    this.comentarioContextual = null;
  }

  borrarComentario() {
    if (!this.comentarioContextual?.id) return;
    this.api.BorrarComentario(this.comentarioContextual.id).subscribe({
      next: () => {
        this.cerrarContextMenu();
        this.cargarComentarios();
      },
      error: (err) => console.error('Error eliminando comentario', err)
    });
  }

  editarComentario() {
    if (this.comentarioContextual?.usuario?.idUsuario !== this.idUsuarioActual && !this.hasRole(['ADMINISTRADOR', 'EMPLEADO'])) return;
    this.comentarioEditandoId = this.comentarioContextual.id;
    this.comentarioEditadoTexto = this.comentarioContextual.contenido;
    this.cerrarContextMenu();
  }

  guardarEdicionComentario() {
    const id = this.comentarioEditandoId;
    const contenido = this.comentarioEditadoTexto.trim();
    if (!id || !contenido) return;
    const datos = { contenido };
    this.api.EditarComentario(id, datos).subscribe({
      next: () => {
        this.comentarioEditandoId = null;
        this.comentarioEditadoTexto = '';
        this.cargarComentarios();
      },
      error: (err) => console.error('Error editando comentario', err)
    });
  }

  hasRole(rolesPermitidos: string[]): boolean {
    const rolesStr = localStorage.getItem('roles');
    const roles = rolesStr ? JSON.parse(rolesStr) : [];
    return roles.some((role: string) => {
      const cleanRole = role.replace('ROLE_', '').toUpperCase();
      return rolesPermitidos.map(r => r.toUpperCase()).includes(cleanRole);
    });
  }

  puedeEditarComentario(comentario: any): boolean {
    return comentario?.usuario?.idUsuario === this.idUsuarioActual || this.hasRole(['ADMINISTRADOR', 'EMPLEADO']);
  }

  scrollAbajo() {
    setTimeout(() => {
      const el = document.getElementById('scroll-final');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    }, 50);
  }

  cancelarEdicion() {
    this.comentarioEditandoId = null;
    this.comentarioEditadoTexto = '';
    this.cargarComentarios();
  }

  puedeBorrarComentario(comentario: any): boolean {
  return (
    comentario?.usuario?.idUsuario === this.idUsuarioActual ||
    this.hasRole(['ADMINISTRADOR', 'EMPLEADO', 'MODERADOR'])
  );
}


}