import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

/**
 * Servicio de interfaz de usuario que gestiona el estado visual del carrito.
 * Utiliza un BehaviorSubject para controlar si el carrito lateral está visible o no.
 */
@Injectable({ providedIn: 'root' })
export class UiService {

  // Indica si el carrito está visible
  private carritoVisibleSubject = new BehaviorSubject<boolean>(false);

  // Observable expuesto públicamente para que los componentes se suscriban a los cambios de visibilidad
  carritoVisible$ = this.carritoVisibleSubject.asObservable();

  /**
   * Muestra el carrito lateral.
   */
  mostrarCarrito() {
    this.carritoVisibleSubject.next(true);
  }

  /**
   * Oculta el carrito lateral.
   */
  ocultarCarrito() {
    this.carritoVisibleSubject.next(false);
  }

  /**
   * Alterna el estado de visibilidad del carrito lateral.
   */
  toggleCarrito() {
    this.carritoVisibleSubject.next(!this.carritoVisibleSubject.value);
  }
}
