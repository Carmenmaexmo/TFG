import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  dropdownOpen: boolean = false; 
  constructor(private router: Router) {}
  lastUser = '';

  ngDoCheck(): void {
    const currentUser = localStorage.getItem('nombreUsuario');
    if (this.lastUser !== currentUser) {
      // Cambió el usuario (ej: acaba de loguearse o desloguearse)
      this.dropdownOpen = false;
      this.lastUser = currentUser || '';
    }
  }

  isLogged(): boolean {
    return !!localStorage.getItem('token');
  }

  getUsername(): string {
    return localStorage.getItem('nombreUsuario') || '';
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  searchVisible: boolean = false;
  searchTerm: string = '';

  toggleSearch() {
    this.searchVisible = !this.searchVisible;
  }

  onSearch() {
    if (this.searchTerm.trim()) {
      console.log('Buscando:', this.searchTerm);
      // Aquí puedes redirigir o lanzar búsqueda real
      this.searchVisible = false;
    }
  }

  
}
