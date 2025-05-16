import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface JwtResponse {
  token: string;
  nombreUsuario: string;
  roles: string[];
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private base = 'http://localhost:8080'; 

  constructor(private http: HttpClient) {}

  /**
   * Login del usuario con nombreUsuario y password.
   * Devuelve un objeto con token JWT y datos del usuario.
   */
  login(data: { nombreUsuario: string; password: string }): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.base}/api/auth/login`, data);
  }

  /**
   * Registro de un nuevo usuario.
   * Devuelve un objeto con el usuario creado.
   */
  registrar(usuario: {
    nombreUsuario: string;
    password: string;
    email: string;
    nombre: string;
    apellidos: string;
    telefono: string;
    dni: string;
  }) {
    return this.http.post(`${this.base}/api/auth/signup`, usuario);
  }
  
  
  /**
   * Obtiene el listado de vinilos (requiere token si el backend lo pide).
   */
  getVinilos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/vinilos`);
  }

  /**
   * Obtiene los pedidos del usuario autenticado.
   */
  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/pedidos`);
  }
}
