import { switchMap, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class CarritoService {
  private base = 'http://localhost:8080';
  private carrito: any[] = [];
  private carrito$ = new BehaviorSubject<any[]>([]);
  mostrarSlide = false;

  constructor(private http: HttpClient, private apiService: ApiService) {
    const guardado = localStorage.getItem('carrito');
    if (guardado) {
      this.carrito = JSON.parse(guardado);
      this.carrito$.next(this.carrito);
    }

    const token = localStorage.getItem('token');
    if (token) {
      this.cargarCarritoDelServidor();
    }
  }

  getCarritoObservable() {
    return this.carrito$.asObservable();
  }

  aniadir(producto: any) {
    const existente = this.carrito.find(p => p.id === producto.id);
    if (existente) {
      existente.cantidad++;
    } else {
      this.carrito.push({ ...producto, cantidad: 1 });
    }
    this.actualizar();
  }

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

  eliminar(id: number) {
    this.carrito = this.carrito.filter(p => p.id !== id);
    this.actualizar();
  }

  getTotal(): number {
    return this.carrito.reduce((acc, item) => acc + (item.precio * item.cantidad), 0);
  }  

  getCarrito(): any[] {
    return this.carrito;
  }  

  vaciar() {
    this.carrito = [];
    this.actualizar();
  }

  private actualizar() {
    this.carrito$.next(this.carrito);
    localStorage.setItem('carrito', JSON.stringify(this.carrito));
    if (localStorage.getItem('token')) {
      this.guardarCarritoEnServidor();
    }
  }

  guardarCarritoEnServidor() {
    const id = localStorage.getItem('idUsuario');
    if (!id) return of(null);
  
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

}
