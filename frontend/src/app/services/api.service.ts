import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface JwtResponse {
  token: string;
  nombreUsuario: string;
  idUsuario: number;
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
  getPedidosPorUsuario(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/pedidos/por-usuario/${localStorage.getItem('idUsuario')}`);
  }

  /**
   * Actualizar los datos de un usuario.
   */
  actualizarUsuario(id: number, datosParciales: any) {
    return this.http.put(`${this.base}/api/usuarios/${id}`, datosParciales);
  }

  /**
   * Obtener un usuario por su ID.
   */
  getUsuarioPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/usuarios/${id}`);
  }
  
  actualizarPedido(id: number, datosParciales: any) {
    return this.http.put(`${this.base}/api/pedidos/${id}`, datosParciales);
  }
  
}
