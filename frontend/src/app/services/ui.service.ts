import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UiService {
  private carritoVisibleSubject = new BehaviorSubject<boolean>(false);
  carritoVisible$ = this.carritoVisibleSubject.asObservable();

  mostrarCarrito() {
    this.carritoVisibleSubject.next(true);
  }

  ocultarCarrito() {
    this.carritoVisibleSubject.next(false);
  }

  toggleCarrito() {
    this.carritoVisibleSubject.next(!this.carritoVisibleSubject.value);
  }
}
