import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {
  nombreUsuario = '';
  email = '';
  password = '';
  nombre = '';
  apellidos = '';
  telefono = '';
  dni = '';
  confirmPassword = '';

  error = '';
  success = '';

  submitted = false;

  constructor(private api: ApiService, private router: Router) {}

  registrar() {
    this.submitted = true;
    this.error = '';
    this.success = '';

    // Validaciones locales
    if (!this.nombreUsuario || !this.email || !this.nombre || !this.apellidos || !this.telefono || !this.dni || !this.password || !this.confirmPassword) {
      this.error = 'Por favor, completa todos los campos.';
      return;
    }

    if (!this.validEmail(this.email)) {
      this.error = 'El email no tiene un formato válido.';
      return;
    }

    if (!this.validTelefono(this.telefono)) {
      this.error = 'El teléfono debe tener 9 dígitos y empezar por 6, 7 o 9.';
      return;
    }

    if (!this.validDNI(this.dni)) {
      this.error = 'El DNI no es válido.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = 'Las contraseñas no coinciden.';
      return;
    }

    const nuevoUsuario = {
      nombreUsuario: this.nombreUsuario,
      email: this.email,
      password: this.password,
      nombre: this.nombre,
      apellidos: this.apellidos,
      telefono: this.telefono,
      dni: this.dni
    };

    // Validación por API para comprobar duplicados
    this.api.registrar(nuevoUsuario).subscribe({
      next: (res: any) => {
        console.log('✅ Registro exitoso:', res);
        this.success = res.message || '¡Registro exitoso! Ya puedes iniciar sesión.';
        setTimeout(() => this.router.navigate(['/login']), 1500);
      },
      error: (err) => {
        console.error(err);
        if (err.status === 409) {
          this.error = 'Ese usuario o email ya está registrado.';
        } else {
          this.error = 'Error al registrar. Intenta nuevamente.';
        }
      }
    });
  }

  validEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  validTelefono(tel: string): boolean {
    return /^[679]\d{8}$/.test(tel); // Formato típico español
  }

  validDNI(dni: string): boolean {
    const dniRegex = /^\d{8}[A-HJ-NP-TV-Z]$/i;
    if (!dniRegex.test(dni)) return false;

    const letras = 'TRWAGMYFPDXBNJZSQVHLCKE';
    const numero = parseInt(dni.slice(0, 8));
    const letraEsperada = letras[numero % 23];
    return letraEsperada === dni.slice(-1).toUpperCase();
  }

}
