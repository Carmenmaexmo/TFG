import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-tema-detalle',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tema-detalle.component.html',
  styleUrls: ['./tema-detalle.component.css'],
  providers: [DatePipe]
})
export class TemaDetalleComponent implements OnInit  {
  comentarios: any[] = [];
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

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.temaId = Number(this.route.snapshot.paramMap.get('id'));
    document.addEventListener('click', () => this.cerrarContextMenu());

    if (this.temaId) {
      this.cargarComentarios();
    }
  }

  ngOnDestroy(): void {
    document.removeEventListener('click', () => this.cerrarContextMenu());
  }

  /**
   * Construye árbol de comentarios agrupando respuestas por comentario padre
   */
  construirJerarquia(comentarios: any[]): any[] {
    const mapa = new Map<number, any>();
    const jerarquia: any[] = [];

    comentarios.forEach(c => mapa.set(c.id, { ...c, respuestas: [] }));

    comentarios.forEach(c => {
      if (c.comentarioPadre?.id) {
        const padre = mapa.get(c.comentarioPadre.id);
        if (padre) {
          padre.respuestas.push(mapa.get(c.id));
        }
      } else {
        jerarquia.push(mapa.get(c.id));
      }
    });

    return jerarquia;
  }

  
  volverAForo() {
    this.router.navigate(['/foros']); // ajusta si tu ruta de foros es distinta
  }

  enviarComentario() {
    if (!this.nuevoComentario.trim()) return;
  
    const comentario = {
      contenido: this.nuevoComentario.trim(),
      fechaComentario: new Date().toISOString(), // 👈 FECHA ACTUAL obligatoria
      idTema: this.temaId,
      idComentarioPadre: this.comentarioPadreId || null,
      idUsuario: Number(localStorage.getItem('idUsuario'))
    };

    console.log('Comentario a enviar:', comentario);
  
    this.api.crearComentario(this.temaId, comentario).subscribe({
      next: () => {
        this.nuevoComentario = '';
        this.comentarioPadreId = null;
        this.cargarComentarios(); // recarga los comentarios después de enviar
      },
      error: (err) => console.error('Error al crear comentario', err)
    });
  }
  
  
  cancelarRespuesta() {
    this.comentarioPadreId = null;
  }

  cargarComentarios(): void {
    this.api.getComentarios(this.temaId).subscribe({
      next: (res) => {
        this.comentarios = res;
        this.comentariosJerarquicos = this.construirJerarquia(res); // si usas estructura anidada
      },
      error: (err) => console.error('Error al cargar comentarios:', err)
    });
  }

  onRightClick(event: MouseEvent, comentario: any) {
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
  
  responderComentario() {
    this.comentarioPadreId = this.comentarioContextual?.id;
    this.respuestaAUsuario = this.comentarioContextual?.usuario?.nombreUsuario || 'Usuario';
    this.cerrarContextMenu();
  }

  borrarComentario() {
    if (!this.comentarioContextual?.id) return;

    this.api.BorrarComentario(this.comentarioContextual.id).subscribe({
      next: () => {
        this.cerrarContextMenu();
        this.cargarComentarios(); // Recarga tras eliminar
      },
      error: (err) => console.error('Error eliminando comentario', err)
    });
  }

  scrollAbajo() {
    setTimeout(() => {
      const el = document.getElementById('scroll-final');
      if (el) el.scrollIntoView({ behavior: 'smooth' });
    }, 50);
  }

  editarComentario() {
    if (this.comentarioContextual?.usuario?.idUsuario !== this.idUsuarioActual) return;
  
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
  
  cancelarEdicion() {
    this.comentarioEditandoId = null;
    this.comentarioEditadoTexto = '';
  }
}
