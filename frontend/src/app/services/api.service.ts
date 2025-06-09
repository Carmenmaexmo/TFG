import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interfaz para la respuesta del login con JWT
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
  private base = 'http://localhost:8080'; // URL base del backend

  constructor(private http: HttpClient) {}

  // ===========================
  // AUTENTICACIÓN Y REGISTRO
  // ===========================

  /**
   * Realiza el login de un usuario.
   */
  login(data: { nombreUsuario: string; password: string }): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.base}/api/auth/login`, data);
  }

  /**
   * Registra un nuevo usuario.
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

  // ===========================
  // USUARIOS
  // ===========================

  getUsuarioPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/usuarios/${id}`);
  }

  actualizarUsuario(id: number, datosParciales: any) {
    return this.http.put(`${this.base}/api/usuarios/${id}`, datosParciales);
  }

  actualizarUsuarioConRawBody(id: number, rawBody: string): Observable<any> {
    return this.http.put(`${this.base}/api/usuarios/${id}`, rawBody, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });

  }

  getUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/usuarios`);
  }

  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/usuarios/${id}`);
  }

  // ===========================
  // DIRECCIONES DE ENVÍO
  // ===========================

  getDireccionesPorUsuario(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/direcciones-envio/por-usuario/${localStorage.getItem('idUsuario')}`);
  }

  guardarDireccion(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/direcciones-envio`, datos);
  }

  eliminarDireccion(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/direcciones-envio/${id}`);
  }

  actualizarDireccion(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/direcciones-envio/${id}`, datos);
  }

  // ===========================
  // VINILOS
  // ===========================

  getVinilos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/vinilos`);
  }

  getViniloPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/vinilos/${id}`);
  }

  crearVinilo(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/vinilos`, datos);
  }

  actualizarVinilo(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/vinilos/${id}`, datos);
  }

  eliminarVinilo(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/vinilos/${id}`);
  }

  // ===========================
  // PROVEEDORES
  // ===========================

  getProveedores(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/proveedores`);
  }

  getProveedorPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/proveedores/${id}`);
  }

  crearProveedor(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/proveedores`, datos);
  }

  actualizarProveedor(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/proveedores/${id}`, datos);
  }

  eliminarProveedor(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/proveedores/${id}`);
  }

  // ===========================
  // PEDIDOS
  // ===========================

  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/pedidos`);
  }

  getPedidosPorUsuario(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/pedidos/por-usuario/${localStorage.getItem('idUsuario')}`);
  }

  getPedidoPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/pedidos/${id}`);
  }

  crearPedido(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/pedidos`, datos);
  }

  actualizarPedido(id: number, datosParciales: any): Observable<any> {
    return this.http.put(`${this.base}/api/pedidos/${id}`, datosParciales);
  }

  eliminarPedido(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/pedidos/${id}`);
  }

  // ===========================
  // EVENTOS
  // ===========================

  getEventos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/eventos`);
  }

  crearEvento(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/eventos`, datos);
  }

  actualizarEvento(eventoId: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/eventos/${eventoId}`, datos);
  }

  eliminarEvento(eventoId: number): Observable<any> {
    return this.http.delete(`${this.base}/api/eventos/${eventoId}`);
  }

  // ===========================
  // ASISTENCIA A EVENTOS
  // ===========================

  getAsistenciasPorEvento(eventoId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/asistencias-evento/por-evento/${eventoId}`);
  }

  crearAsistenciaEvento(data: any): Observable<any> {
    return this.http.post(`${this.base}/api/asistencias-evento`, data);
  }

  actualizarAsistenciaEvento(id: number, data: any): Observable<any> {
    return this.http.put(`${this.base}/api/asistencias-evento/${id}`, data);
  }

  // ===========================
  // FORO
  // ===========================

  getForo(): Observable<any> {
    return this.http.get<any>(`${this.base}/api/foros`);
  }

  actualizarForo(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/foros/${id}`, datos);
  }

  getTemas(foroId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/temas-foro`);
  }

  getTemaPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/foro/temas/${id}`);
  }

  crearTemaEnForo(foroId: number, tema: any): Observable<any> {
    return this.http.post(`${this.base}/api/temas-foro`, tema);
  }

  actualizarTema(id: number, data: any): Observable<any> {
    return this.http.put(`${this.base}/api/temas-foro/${id}`, data);
  }

  eliminarTema(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/temas-foro/${id}`);
  }

  getComentarios(temaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/comentarios-foro/por-tema/${temaId}`);
  }

  crearComentario(temaId: number, comentario: any): Observable<any> {
    return this.http.post(`${this.base}/api/comentarios-foro`, comentario);
  }

  EditarComentario(id: number, comentario: any): Observable<any> {
    return this.http.put(`${this.base}/api/comentarios-foro/${id}`, comentario);
  }

  BorrarComentario(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/comentarios-foro/${id}`);
  }

  // ===========================
  // BLOQUEOS EN EL FORO
  // ===========================

  getBloqueosForo(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/bloqueos-foro`);
  }

  crearBloqueo(bloqueo: any): Observable<any> {
    return this.http.post(`${this.base}/api/bloqueos-foro`, bloqueo);
  }

  eliminarBloqueo(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/bloqueos-foro/${id}`);
  }
}
