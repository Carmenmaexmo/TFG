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
  // Lista de comentarios organizados jerárquicamente
  comentariosJerarquicos: any[] = [];

  // ID del tema actual
  temaId: number = 0;

  // Contenido del nuevo comentario a enviar
  nuevoComentario: string = '';

  // ID del comentario al que se está respondiendo, si aplica
  comentarioPadreId: number | null = null;

  // Variables para el menú contextual de acciones
  contextMenuVisible: boolean = false;
  contextMenuX: number = 0;
  contextMenuY: number = 0;
  comentarioContextual: any = null;

  // Nombre del usuario al que se responde, para mostrar en UI
  respuestaAUsuario: string = '';

  // ID del usuario actual (extraído del localStorage)
  idUsuarioActual: number = Number(localStorage.getItem('idUsuario'));

  // ID del comentario que se está editando y su nuevo contenido
  comentarioEditandoId: number | null = null;
  comentarioEditadoTexto: string = '';

  // Término de búsqueda para filtrar comentarios
  terminoBusqueda: string = '';

  constructor(private route: ActivatedRoute, private api: ApiService, private router: Router) {}

  // Carga inicial del componente
  ngOnInit(): void {
    this.temaId = Number(this.route.snapshot.paramMap.get('id'));
    this.route.queryParams.subscribe(params => {
      this.terminoBusqueda = (params['q'] || '').toLowerCase();
      this.cargarComentarios();
    });

    // Escuchar clics globales para cerrar el menú contextual
    document.addEventListener('click', () => this.cerrarContextMenu());
  }

  // Eliminación de listeners para evitar fugas de memoria
  ngOnDestroy(): void {
    document.removeEventListener('click', () => this.cerrarContextMenu());
  }

  // Cargar los comentarios del tema desde la API y procesarlos
  cargarComentarios(): void {
    this.api.getComentarios(this.temaId).subscribe({
      next: (res) => {
        const jerarquicos = this.construirJerarquia(res);
        const marcados = this.marcarColapsos(jerarquicos);
        this.comentariosJerarquicos = this.filtrarComentariosRecursivo(marcados, this.terminoBusqueda);
      },
      error: (err) => console.error('Error al cargar comentarios:', err)
    });
  }

  // Construir jerarquía padre-hijo a partir de los comentarios planos
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

  // Añadir propiedad de colapso por defecto a cada comentario
  marcarColapsos(lista: any[]): any[] {
    return lista.map(c => ({
      ...c,
      mostrarRespuestas: false,
      respuestas: c.respuestas ? this.marcarColapsos(c.respuestas) : []
    }));
  }

  // Alternar visibilidad de respuestas de un comentario
  toggleRespuestas(comentario: any) {
    comentario.mostrarRespuestas = !comentario.mostrarRespuestas;
  }

  // Redirigir a la vista general del foro
  volverAForo() {
    this.router.navigate(['/foros']);
  }

  // Enviar un nuevo comentario (respuesta o raíz)
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

  // Desplazar vista hacia el formulario de comentarios
  scrollAlFormulario() {
    setTimeout(() => {
      const el = document.getElementById('formulario-comentario');
      if (el) el.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, 50);
  }

  // Preparar estado para responder a un comentario concreto
  responderComentario() {
    this.comentarioPadreId = this.comentarioContextual?.id;
    this.respuestaAUsuario = this.comentarioContextual?.usuario?.nombreUsuario || 'Usuario';
    this.cerrarContextMenu();

    setTimeout(() => {
      const textarea = document.getElementById('nuevo-comentario-textarea');
      if (textarea) {
        textarea.scrollIntoView({ behavior: 'smooth', block: 'center' });
        (textarea as HTMLTextAreaElement).focus();
      }
    }, 100);
  }

  // Cancelar el modo respuesta
  cancelarRespuesta() {
    this.comentarioPadreId = null;
    this.respuestaAUsuario = '';
  }

  // Mostrar menú contextual en clic derecho sobre comentario
  onRightClick(event: MouseEvent, comentario: any) {
    event.stopPropagation();
    event.preventDefault();
    this.contextMenuVisible = true;
    this.contextMenuX = event.clientX;
    this.contextMenuY = event.clientY;
    this.comentarioContextual = comentario;
  }

  // Ocultar menú contextual
  cerrarContextMenu() {
    this.contextMenuVisible = false;
    this.comentarioContextual = null;
  }

  // Borrar el comentario seleccionado en el menú contextual
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

  // Preparar comentario para su edición
  editarComentario() {
    if (this.comentarioContextual?.usuario?.idUsuario !== this.idUsuarioActual && !this.hasRole(['ADMINISTRADOR', 'EMPLEADO'])) return;
    this.comentarioEditandoId = this.comentarioContextual.id;
    this.comentarioEditadoTexto = this.comentarioContextual.contenido;
    this.cerrarContextMenu();
  }

  // Guardar cambios al editar un comentario
  guardarEdicionComentario() {
    const id = this.comentarioEditandoId;
    const contenido = this.comentarioEditadoTexto.trim();
    if (!id || !contenido) return;
    this.api.EditarComentario(id, { contenido }).subscribe({
      next: () => {
        this.comentarioEditandoId = null;
        this.comentarioEditadoTexto = '';
        this.cargarComentarios();
      },
      error: (err) => console.error('Error editando comentario', err)
    });
  }

  // Verificar si el usuario actual tiene uno de los roles indicados
  hasRole(rolesPermitidos: string[]): boolean {
    const rolesStr = localStorage.getItem('roles');
    const roles = rolesStr ? JSON.parse(rolesStr) : [];
    return roles.some((role: string) => {
      const cleanRole = role.replace('ROLE_', '').toUpperCase();
      return rolesPermitidos.map(r => r.toUpperCase()).includes(cleanRole);
    });
  }

  // Validar si un comentario puede ser editado por el usuario actual
  puedeEditarComentario(comentario: any): boolean {
    return comentario?.usuario?.idUsuario === this.idUsuarioActual || this.hasRole(['ADMINISTRADOR', 'EMPLEADO']);
  }

  // Desplazar vista hacia la parte inferior
  scrollAbajo() {
    setTimeout(() => {
      const el = document.getElementById('scroll-final');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    }, 50);
  }

  // Cancelar edición de comentario
  cancelarEdicion() {
    this.comentarioEditandoId = null;
    this.comentarioEditadoTexto = '';
    this.cargarComentarios();
  }

  // Validar si un comentario puede ser borrado por el usuario actual
  puedeBorrarComentario(comentario: any): boolean {
    return (
      comentario?.usuario?.idUsuario === this.idUsuarioActual ||
      this.hasRole(['ADMINISTRADOR', 'EMPLEADO', 'MODERADOR'])
    );
  }

  // Filtro recursivo de comentarios y sus respuestas por término de búsqueda
  filtrarComentariosRecursivo(comentarios: any[], termino: string): any[] {
    if (!termino.trim()) return comentarios;

    const buscar = termino.toLowerCase();

    return comentarios.reduce((acumulador: any[], comentario: any) => {
      const contenido = comentario.contenido?.toLowerCase?.() || '';
      const usuario = comentario.usuario?.nombreUsuario?.toLowerCase?.() || '';
      const fecha = comentario.fechaComentario?.toLowerCase?.() || '';

      const coincide =
        contenido.includes(buscar) ||
        usuario.includes(buscar) ||
        fecha.includes(buscar);

      const respuestasFiltradas = this.filtrarComentariosRecursivo(comentario.respuestas || [], termino);

      if (coincide || respuestasFiltradas.length > 0) {
        acumulador.push({
          ...comentario,
          respuestas: respuestasFiltradas
        });
      }

      return acumulador;
    }, []);
  }

}
