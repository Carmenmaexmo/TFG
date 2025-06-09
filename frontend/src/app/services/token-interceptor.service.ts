import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Interceptor HTTP para añadir el token JWT a las solicitudes salientes hacia el backend.
 */
@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  
  /**
   * Intercepta las peticiones HTTP salientes.
   * Si existe un token en localStorage y la URL es del backend, añade el header Authorization.
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    console.log('TokenInterceptor activo. Token:', token);

    // Añadir el token solo si existe y la petición es hacia el backend
    if (token && req.url.startsWith('http://localhost:8080/api')) {
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(cloned);
    }

    // Si no hay token o no es una URL del backend, continuar sin modificar la petición
    return next.handle(req);
  }
}
