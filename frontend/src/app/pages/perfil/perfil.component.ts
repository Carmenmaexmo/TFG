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
  usuario: any = null;          // Objeto que contiene los datos del usuario
  editando = false;             // Controla si el formulario está en modo edición
  idUsuario: number = 0;        // ID del usuario actual

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    // Recuperamos el ID del usuario desde el almacenamiento local
    const id = localStorage.getItem('idUsuario');
    if (id) {
      this.idUsuario = +id;

      // Obtenemos los datos del usuario a través del servicio
      this.api.getUsuarioPorId(this.idUsuario).subscribe({
        next: (res) => this.usuario = res,
        error: (err) => console.error('Error cargando perfil:', err)
      });
    }
  }

  // Método que guarda los cambios realizados al perfil del usuario
  guardarCambios() {
    this.api.actualizarUsuario(this.idUsuario, this.usuario).subscribe({
      next: () => {
        this.editando = false; // Desactiva el modo edición
      },
      error: () => alert('Error al actualizar.')
    });
  }

  // Activa el modo edición del formulario
  activarEdicion() {
    this.editando = true;
  }

  // Cancela los cambios y recarga los datos originales desde el servidor
  cancelar() {
    this.editando = false;
    this.ngOnInit(); // Recarga los datos originales del usuario
  }
}
