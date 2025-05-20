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
  idUsuario = '';

  constructor(private api: ApiService, private router: Router) {}

  login() {
    this.api.login({ nombreUsuario: this.nombreUsuario, password: this.password })
      .subscribe({
        next: (res) => {
          this.idUsuario = res.idUsuario.toString();
          localStorage.setItem('token', res.token);
          localStorage.setItem('nombreUsuario', res.nombreUsuario);
          localStorage.setItem('roles', JSON.stringify(res.roles));
          localStorage.setItem('idUsuario', res.idUsuario.toString()); // ✅ usa lo que viene del backend
          this.idUsuario = res.idUsuario.toString();
          console.log('Token guardado:', res.token);
          console.log('Nombre de usuario guardado:', res.nombreUsuario);
          console.log('ID de usuario guardado:', this.idUsuario);
          console.log('Roles guardados:', res.roles);
  
          setTimeout(() => {
            this.router.navigate(['/catalogo']);
          }, 50);
        },
        error: () => this.error = 'Usuario o contraseña incorrectos'
      });
  }
  
  goToRegistro() {
    this.router.navigate(['/registro']);
  }

}
