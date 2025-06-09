import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Interceptor HTTP que añade el token de autenticación JWT a todas las peticiones salientes.
 * Si hay un token almacenado en localStorage, se incluye en la cabecera Authorization.
 */
@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  /**
   * Método intercept que se ejecuta en cada petición HTTP.
   * Añade el token (si existe) a la cabecera de autorización.
   */
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    console.log('TokenInterceptor activo. Token:', token);

    if (token) {
      // Clona la petición original y añade la cabecera Authorization
      const clone = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(clone);
    }

    // Si no hay token, continúa con la petición original sin modificar
    return next.handle(req);
  }
}
