import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import Swal from 'sweetalert2';

/**
 * Guard de autenticación que impide el acceso a rutas si el usuario no está logueado.
 * Si no hay token, muestra una alerta personalizada con SweetAlert2 y redirige al login.
 */
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  /**
   * Método que determina si se puede activar una ruta protegida.
   * Retorna true si hay token en localStorage, o false (con redirección) si no lo hay.
   */
  canActivate(): boolean | UrlTree {
    const token = localStorage.getItem('token');

    if (!token) {
      // Usuario no logueado: mostrar modal y redirigir al login
      Swal.fire({
        title: '<span style="font-family:\'Segoe UI\', Roboto, sans-serif;">Acceso restringido</span>',
        html: `<div style="font-family:'Segoe UI', Roboto, sans-serif;">
                 <strong>Debes iniciar sesión para continuar</strong><br>
                 <small>Inicia sesión para comprar vinilos y acceder al contenido</small>
               </div>`,
        icon: 'warning',
        confirmButtonText: 'Ir al login',
        confirmButtonColor: '#fcd34d',
        background: '#1c1c1e',
        color: '#f8f8f8',
        customClass: {
          popup: 'rounded-3xl shadow-lg',
          confirmButton: 'text-black font-medium px-4 py-2'
        }
      }).then(() => {
        // Redirección al login una vez se cierra el modal
        this.router.navigate(['/login']);
      });

      return false;
    }

    // Usuario logueado: permitir acceso
    return true;
  }
}
