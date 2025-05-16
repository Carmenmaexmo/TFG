import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import Swal from 'sweetalert2';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean | UrlTree {
    const token = localStorage.getItem('token');

    if (!token) {
      // Mostrar modal si NO está logueado
      Swal.fire({
        title: '🔐 Acceso restringido',
        html: '<b>Debes iniciar sesión para continuar</b><br><small>Inicia sesión para comprar vinilos y acceder al contenido</small>',
        icon: 'warning',
        confirmButtonText: 'Ir al login',
        confirmButtonColor: '#fcd34d',
        background: '#1c1c1e',
        color: '#f8f8f8',
        customClass: {
          popup: 'rounded-3xl shadow-lg',
          title: 'text-lg font-semibold',
          htmlContainer: 'text-sm',
          confirmButton: 'text-black font-medium px-4 py-2'
        }
      }).then(() => {
        this.router.navigate(['/login']);
      });

      return false;
    }

    return true;
  }
}
