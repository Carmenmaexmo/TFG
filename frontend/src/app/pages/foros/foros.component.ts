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


  constructor(private api: ApiService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.api.getForo().subscribe({
      next: (res) => {
        // Suponiendo que solo hay un foro
        this.foro = res[0];
        if (this.foro?.id) {
          this.api.getTemas(this.foro.id).subscribe({
            next: temas => this.temas = temas,
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
    this.router.navigate(['/foros', idTema]); // Asegúrate de tener esta ruta configurada
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

    console.log('Creando tema:', tema);
  
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
  

}
