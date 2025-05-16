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
  nombreUsuario = '';
  password = '';
  error = '';

  constructor(private api: ApiService, private router: Router) {}

  login() {
    this.api.login({ nombreUsuario: this.nombreUsuario, password: this.password })
      .subscribe({
        next: (res) => {
          localStorage.setItem('token', res.token);
          localStorage.setItem('nombreUsuario', res.nombreUsuario);
          localStorage.setItem('roles', JSON.stringify(res.roles));
          console.log('Token guardado:', res.token);
          console.log('Nombre de usuario guardado:', res.nombreUsuario);
          console.log('Roles guardados:', res.roles);
  
          // ✅ Redirige a /catalogo directamente
          this.router.navigate(['/']);
        },
        error: () => this.error = 'Usuario o contraseña incorrectos'
      });
  }
  
  goToRegistro() {
    this.router.navigate(['/registro']);
  }
  
}
