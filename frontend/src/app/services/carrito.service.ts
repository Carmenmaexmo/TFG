import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class CarritoService {
  private base = 'http://localhost:8080'; // URL base para llamadas directas (caso cargarCarritoDelServidor)
  private carrito: any[] = []; // Lista local de productos en el carrito
  private carrito$ = new BehaviorSubject<any[]>([]); // Observable para reaccionar a cambios en el carrito
  mostrarSlide = false; // Controla si se muestra el slide del carrito

  constructor(private http: HttpClient, private apiService: ApiService) {
    // Si hay un carrito en localStorage, cargarlo
    const guardado = localStorage.getItem('carrito');
    if (guardado) {
      this.carrito = JSON.parse(guardado);
      this.carrito$.next(this.carrito);
    }

    // Si hay token, intenta cargar el carrito guardado en el backend
    const token = localStorage.getItem('token');
    if (token) {
      this.cargarCarritoDelServidor();
    }
  }

  // ===========================
  // MÉTODOS PÚBLICOS
  // ===========================

  /**
   * Devuelve el observable del carrito para poder suscribirse a sus cambios.
   */
  getCarritoObservable() {
    return this.carrito$.asObservable();
  }

  /**
   * Añade un producto al carrito. Si ya existe, incrementa su cantidad.
   */
  aniadir(producto: any) {
    const existente = this.carrito.find(p => p.id === producto.id);
    if (existente) {
      existente.cantidad++;
    } else {
      this.carrito.push({ ...producto, cantidad: 1 });
    }
    this.actualizar();
  }

  /**
   * Quita una unidad del producto indicado. Si llega a 0, lo elimina del carrito.
   */
  quitar(id: number) {
    const index = this.carrito.findIndex(p => p.id === id);
    if (index !== -1) {
      this.carrito[index].cantidad--;
      if (this.carrito[index].cantidad <= 0) {
        this.carrito.splice(index, 1);
      }
    }
    this.actualizar();
  }

  /**
   * Elimina completamente un producto del carrito.
   */
  eliminar(id: number) {
    this.carrito = this.carrito.filter(p => p.id !== id);
    this.actualizar();
  }

  /**
   * Vacía completamente el carrito.
   */
  vaciar() {
    this.carrito = [];
    this.actualizar();
  }

  /**
   * Calcula el total en euros del contenido del carrito.
   */
  getTotal(): number {
    return this.carrito.reduce((acc, item) => acc + (item.precio * item.cantidad), 0);
  }

  /**
   * Devuelve una copia del array de productos en el carrito.
   */
  getCarrito(): any[] {
    return this.carrito;
  }

  // ===========================
  // SINCRONIZACIÓN CON BACKEND
  // ===========================

  /**
   * Guarda el estado actual del carrito en el backend, si el usuario está autenticado.
   */
  guardarCarritoEnServidor() {
    const id = localStorage.getItem('idUsuario');
    if (!id) return of(null); // Si no hay sesión, no se guarda

    const datosActualizados = {
      carrito: JSON.stringify(this.carrito)
    };
    const rawBody = JSON.stringify(datosActualizados);

    console.log('🛒 Guardando carrito en servidor:', rawBody);

    return this.apiService.actualizarUsuarioConRawBody(+id, rawBody).pipe(
      catchError(err => {
        console.error('❌ Error guardando carrito en servidor:', err);
        return of(null);
      })
    );
  }

  /**
   * Carga el carrito almacenado en el backend para el usuario logueado.
   */
  cargarCarritoDelServidor() {
    const id = localStorage.getItem('idUsuario');
    if (!id) return;

    this.http.get<any>(`${this.base}/api/usuarios/${id}`).subscribe({
      next: usuario => {
        try {
          const carrito = JSON.parse(usuario.carrito || '[]');
          if (Array.isArray(carrito)) {
            this.carrito = carrito;
            this.carrito$.next(this.carrito);
            localStorage.setItem('carrito', JSON.stringify(this.carrito));
            console.log('🛒 Carrito cargado del backend');
          }
        } catch (e) {
          console.error('❌ Error parseando carrito:', e);
        }
      },
      error: err => console.error('❌ Error al cargar carrito:', err)
    });
  }

  // ===========================
  // ACTUALIZACIÓN INTERNA
  // ===========================

  /**
   * Actualiza el observable, guarda en localStorage y sincroniza con el servidor si hay sesión.
   */
  private actualizar() {
    this.carrito$.next(this.carrito);
    localStorage.setItem('carrito', JSON.stringify(this.carrito));

    if (localStorage.getItem('token')) {
      this.guardarCarritoEnServidor().subscribe();
    }
  }
}
