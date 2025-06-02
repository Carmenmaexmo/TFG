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
  
  
   // ---------- USUARIOS ----------
   getUsuarioPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/usuarios/${id}`);
  }

  actualizarUsuario(id: number, datosParciales: any) {
    return this.http.put(`${this.base}/api/usuarios/${id}`, datosParciales);
  }

  getUsuarios(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/usuarios`);
  }

  eliminarUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/usuarios/${id}`);
  }

  // ---------- VINILOS ----------
  getVinilos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/vinilos`);
  }

  // ---------- PEDIDOS ----------
  getPedidosPorUsuario(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/pedidos/por-usuario/${localStorage.getItem('idUsuario')}`);
  }

  actualizarPedido(id: number, datosParciales: any) {
    return this.http.put(`${this.base}/api/pedidos/${id}`, datosParciales);
  }

  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/pedidos`);
  }

  getPedidoPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/pedidos/${id}`);
  }

  // ---------- EVENTOS ----------
  getEventos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/eventos`);
  }

  crearEvento(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/eventos`, datos);
  }

  eliminarEvento(eventoId: number): Observable<any> {
    return this.http.delete(`${this.base}/api/eventos/${eventoId}`);
  }

  actualizarEvento(eventoId: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/eventos/${eventoId}`, datos);
  }

  // ---------- ASISTENCIAS EVENTO ----------

  getAsistenciasPorEvento(eventoId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/asistencias-evento/por-evento/${eventoId}`);
  }
  
  crearAsistenciaEvento(data: any): Observable<any> {
    return this.http.post(`${this.base}/api/asistencias-evento`, data);
  }
  
  actualizarAsistenciaEvento(id: number, data: any): Observable<any> {
    return this.http.put(`${this.base}/api/asistencias-evento/${id}`, data);
  }
  

  // ---------- FORO ----------
  /**
   * Obtiene el foro principal (asumes que solo hay uno).
   */
  getForo(): Observable<any> {
    return this.http.get<any>(`${this.base}/api/foros`);
  }

  /**
   * Obtiene los temas de un foro.
   */
  getTemas(foroId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/temas-foro`);
  }

  /**
   * Obtiene los comentarios de un tema del foro.
   */
  getComentarios(temaId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/comentarios-foro/por-tema/${temaId}`);
  }

  /**
   * Obtiene un tema concreto por su ID (si lo necesitas en la vista de tema).
   */
  getTemaPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.base}/api/foro/temas/${id}`);
  }

  /**
   * Crea un nuevo comentario (o respuesta si se indica `idComentarioPadre`).
   */
  crearComentario(temaId: number, comentario: any): Observable<any> {
    return this.http.post(`${this.base}/api/comentarios-foro`, comentario);
  }

  /**
   * Crear un nuevo tema en el foro.
   */
  crearTemaEnForo(foroId: number, tema: any): Observable<any> {
    return this.http.post(`${this.base}/api/temas-foro`, tema);
  }

  /**
   * Eliminar un comentario existente.
   */
  BorrarComentario(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/comentarios-foro/${id}`);
  }
  
  /**
   * Editar un comentario existente.
   */
  EditarComentario(id: number, comentario: any): Observable<any> {
    return this.http.put(`${this.base}/api/comentarios-foro/${id}`, comentario);
  }

  // ---------- BLOQUEOS FORO ----------
  getBloqueosForo(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/bloqueos-foro`);
  }

  crearBloqueoForo(bloqueo: any): Observable<any> {
    return this.http.post(`${this.base}/api/bloqueos-foro`, bloqueo);
  }

  eliminarBloqueoForo(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/bloqueos-foro/${id}`);
  }

  // ---------- TEMAS Y COMENTARIOS FORO ----------
  actualizarTema(id: number, data: any): Observable<any> {
    return this.http.put(`${this.base}/api/temas-foro/${id}`, data);
  }

  borrarTema(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/temas-foro/${id}`);
  }

  borrarComentario(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/comentarios-foro/${id}`);
  }

  /**
   * Direcciones del usuario.
   */
  getDireccionesPorUsuario(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/direcciones-envio/por-usuario/${localStorage.getItem('idUsuario')}`);
  }

  /**
   * Guardar dirección.
   */
  guardarDireccion(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/direcciones-envio`, datos);
  }

  /**
   * Eliminar dirección.
   */
  eliminarDireccion(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/direcciones-envio/${id}`);
  }

  /**
   * Guardar un nuevo pedido.
   */
  crearPedido(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/pedidos`, datos);
  }

  /**
   * Eliminar un pedido.
   */
  eliminarPedido(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/pedidos/${id}`);
  }

  /**
   * Crear un nuevo vinilo.
   */
  crearVinilo(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/vinilos`, datos);
  }

  /**
   * Obtener proveedores.
   */
  getProveedores(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/api/proveedores`);
  }

  /**
   * Crear un nuevo proveedor.
   */
  crearProveedor(datos: any): Observable<any> {
    return this.http.post(`${this.base}/api/proveedores`, datos);
  }

  /**
   * Eliminar un proveedor.
   */
  eliminarProveedor(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/proveedores/${id}`);
  }

  /**
   * Actualizar un proveedor.
   */
  actualizarProveedor(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/proveedores/${id}`, datos);
  }

  /**
   * Eliminar un vinilo.
   */
  eliminarVinilo(id: number): Observable<any> {
    return this.http.delete(`${this.base}/api/vinilos/${id}`);
  }

  /**
   * Actualizar un vinilo.
   */
  actualizarVinilo(id: number, datos: any): Observable<any> {
    return this.http.put(`${this.base}/api/vinilos/${id}`, datos);
  }

  /**
   * Actualizar un usuario con un body completo carrito.
   */
  actualizarUsuarioConRawBody(id: number, rawBody: string): Observable<any> {
    return this.http.put(`${this.base}/api/usuarios/${id}`, rawBody, {
      headers: { 'Content-Type': 'application/json' },
      responseType: 'json'
    });

  }

}
