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
  error: string = ''; // Mensaje de error en caso de fallo
  success: string = ''; // Mensaje de éxito al guardar cambios

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
    this.error = '';
    this.success = '';

    // Validación de campos obligatorios
    if (!this.usuario.nombre || !this.usuario.apellidos || !this.usuario.email || !this.usuario.telefono || !this.usuario.dni) {
      this.error = 'Todos los campos son obligatorios.';
      setTimeout(() => this.error = '', 4000);
      return;
    }

    // Validación de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.usuario.email)) {
      this.error = 'El email no tiene un formato válido.';
      setTimeout(() => this.error = '', 4000);
      return;
    }

    // Validación de teléfono español (9 dígitos y empieza por 6, 7 o 9)
    const telefonoRegex = /^[679]\d{8}$/;
    if (!telefonoRegex.test(this.usuario.telefono)) {
      this.error = 'El teléfono debe tener 9 dígitos y empezar por 6, 7 o 9.';
      setTimeout(() => this.error = '', 4000);
      return;
    }

    // Validación de DNI
    const dniRegex = /^\d{8}[A-HJ-NP-TV-Z]$/i;
    if (!dniRegex.test(this.usuario.dni)) {
      this.error = 'El DNI no tiene un formato válido.';
      setTimeout(() => this.error = '', 4000);
      return;
    }
    const letras = 'TRWAGMYFPDXBNJZSQVHLCKE';
    const numero = parseInt(this.usuario.dni.slice(0, 8));
    const letraEsperada = letras[numero % 23];
    if (letraEsperada !== this.usuario.dni.slice(-1).toUpperCase()) {
      this.error = 'La letra del DNI no es correcta.';
      setTimeout(() => this.error = '', 4000);
      return;
    }

    // Actualización exitosa
    this.api.actualizarUsuario(this.idUsuario, this.usuario).subscribe({
      next: () => {
        this.success = 'Perfil actualizado correctamente.';
        this.editando = false;
        setTimeout(() => this.success = '', 4000);
      },
      error: () => {
        this.error = 'Error al actualizar el perfil.';
        setTimeout(() => this.error = '', 4000);
      }
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
