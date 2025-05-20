import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  usuario: any = null;
  editando = false;
  idUsuario: number = 0;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    const id = localStorage.getItem('idUsuario');
    if (id) {
      this.idUsuario = +id;
      this.api.getUsuarioPorId(this.idUsuario).subscribe({
        next: (res) => this.usuario = res,
        error: (err) => console.error('Error cargando perfil:', err)
      });
    }
  }

  guardarCambios() {
    this.api.actualizarUsuario(this.idUsuario, this.usuario).subscribe({
      next: () => {
        this.editando = false;
        alert('Datos actualizados correctamente.');
      },
      error: () => alert('Error al actualizar.')
    });
  }

  activarEdicion() {
    this.editando = true;
  }

  cancelar() {
    this.editando = false;
    this.ngOnInit(); // recarga datos originales
  }
}
