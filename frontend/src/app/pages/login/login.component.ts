import { Component } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // Campos del formulario de login
  nombreUsuario = '';
  password = '';
  error = '';
  idUsuario = '';

  constructor(private api: ApiService, private router: Router) {}

  /**
   * Método para iniciar sesión. Envía las credenciales al backend,
   * guarda el token y los datos del usuario si son válidos y redirige al catálogo.
   */
  login() {
    this.api.login({ nombreUsuario: this.nombreUsuario, password: this.password })
      .subscribe({
        next: (res) => {
          // Guardar datos importantes en localStorage
          this.idUsuario = res.idUsuario.toString();
          localStorage.setItem('token', res.token);
          localStorage.setItem('nombreUsuario', res.nombreUsuario);
          localStorage.setItem('roles', JSON.stringify(res.roles));
          localStorage.setItem('idUsuario', this.idUsuario);

          // Logs para depuración
          console.log('Token guardado:', res.token);
          console.log('Nombre de usuario guardado:', res.nombreUsuario);
          console.log('ID de usuario guardado:', this.idUsuario);
          console.log('Roles guardados:', res.roles);
  
          // Redirige al catálogo tras iniciar sesión
          setTimeout(() => {
            this.router.navigate(['/catalogo']);
          }, 50);
        },
        // Si hay error en el login, muestra mensaje
        error: () => this.error = 'Usuario o contraseña incorrectos'
      });
  }

  /**
   * Navega a la vista de registro de usuario
   */
  goToRegistro() {
    this.router.navigate(['/registro']);
  }
}
