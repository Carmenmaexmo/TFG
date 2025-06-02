import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../services/api.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-usuarios-admin',
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios-admin.component.html',
  styleUrls: ['./usuarios-admin.component.css']
})
export class UsuariosAdminComponent implements OnInit {
  usuarios: any[] = [];
  filtro: string = '';
  editandoId: number | null = null;
  userEdit: any = {};
  rolesDisponibles = ['ADMINISTRADOR', 'EMPLEADO', 'CLIENTE', 'MODERADOR'];
  apiService: any;
  erroresFormulario: { [key: string]: string } = {};
  erroresEdicion: { [key: string]: string } = {};  

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.api.getUsuarios().subscribe(data => {
      this.usuarios = data;
    });
  }

  get usuariosFiltrados() {
    if (!this.filtro.trim()) {
      return this.usuarios;
    }
  
    const query = this.filtro.toLowerCase();
  
    return this.usuarios.filter(u =>
      (u.nombreUsuario && u.nombreUsuario.toLowerCase().includes(query)) ||
      (u.nombre && u.nombre.toLowerCase().includes(query)) ||
      (u.apellidos && u.apellidos.toLowerCase().includes(query)) ||
      (u.email && u.email.toLowerCase().includes(query)) ||
      (u.telefono && u.telefono.toLowerCase().includes(query)) ||
      (u.dni && u.dni.toLowerCase().includes(query)) ||
      (u.rol && u.rol.toLowerCase().includes(query))
    );
  }
  
mostrarFormularioCrear = false;

nuevoUsuario = {
  nombreUsuario: '',
  nombre: '',
  apellidos: '',
  email: '',
  password: '',
  telefono: '',
  dni: '',
  rol: 'CLIENTE' // valor por defecto
};

crearUsuario() {
  if (!this.validarFormulario(this.nuevoUsuario)) {
    return;
  }

  const existe = this.usuarios.some(u => u.nombreUsuario === this.nuevoUsuario.nombreUsuario);
  if (existe) {
    this.erroresFormulario['nombreUsuario'] = 'El nombre de usuario ya existe.';
    return;
  }

  this.api.registrar(this.nuevoUsuario).subscribe({
    next: () => {
      this.mostrarFormularioCrear = false;
      this.nuevoUsuario = {
        nombreUsuario: '',
        nombre: '',
        apellidos: '',
        email: '',
        password: '',
        telefono: '',
        dni: '',
        rol: 'CLIENTE'
      };
      this.erroresFormulario = {};  
      this.cargarUsuarios();
    },
    error: (err: any) => {
      console.error('❌ Error creando usuario', err);
    }
  });  
}
validarFormulario(usuario: any, esEdicion = false): boolean {
  const errores: { [key: string]: string } = {};

  if (!usuario.nombreUsuario || usuario.nombreUsuario.trim() === '') {
    errores['nombreUsuario'] = 'El nombre de usuario es obligatorio.';
  }
  if (!usuario.nombre || usuario.nombre.trim() === '') {
    errores['nombre'] = 'El nombre es obligatorio.';
  }
  if (!usuario.apellidos || usuario.apellidos.trim() === '') {
    errores['apellidos'] = 'Los apellidos son obligatorios.';
  }
  if (!usuario.email || !usuario.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
    errores['email'] = 'Introduce un email válido.';
  }
  if (!esEdicion && (!usuario.password || usuario.password.length < 6)) {
    errores['password'] = 'La contraseña debe tener al menos 6 caracteres.';
  }
  if (!usuario.telefono || !usuario.telefono.match(/^[6789]\d{8}$/)) {
    errores['telefono'] = 'Introduce un teléfono válido (España).';
  }
  if (!usuario.dni || !usuario.dni.match(/^\d{8}[A-Z]$/)) {
    errores['dni'] = 'Introduce un DNI válido.';
  }
  if (!this.rolesDisponibles.includes(usuario.rol)) {
    errores['rol'] = 'Selecciona un rol válido.';
  }

  if (esEdicion) {
    this.erroresEdicion = errores;
  } else {
    this.erroresFormulario = errores;
  }

  return Object.keys(errores).length === 0;
}


  activarEdicion(user: any) {
    this.editandoId = user.idUsuario;
    this.userEdit = { ...user };
  }

  guardarEdicion(user: any) {
    if (!this.validarFormulario(this.userEdit, true)) {
      return;
    }

     // ✅ Comprobamos si otro usuario ya tiene ese nombre
    const duplicado = this.usuarios.some(u =>
      u.idUsuario !== user.idUsuario && u.nombreUsuario === this.userEdit.nombreUsuario
    );
    if (duplicado) {
      this.erroresEdicion['nombreUsuario'] = 'El nombre de usuario ya existe.';
      return;
    }
  
    this.api.actualizarUsuario(user.idUsuario, this.userEdit).subscribe(() => {
      Object.assign(user, this.userEdit);
      this.editandoId = null;
      this.erroresEdicion = {};  // limpia errores
      this.cargarUsuarios();
    });
  }
  

  eliminarUsuario(id: number) {
    if (confirm('¿Seguro que deseas eliminar este usuario?')) {
      this.api.eliminarUsuario(id).subscribe(() => {
        this.usuarios = this.usuarios.filter(u => u.idUsuario !== id);
        alert('✅ Usuario eliminado');
      });
    }
  }

  validarUsuario(usuario: any): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const telefonoRegex = /^[6789]\d{8}$/;
    const dniRegex = /^\d{8}[A-Z]$/;

    if (!emailRegex.test(usuario.email)) return false;
    if (!telefonoRegex.test(usuario.telefono)) return false;
    if (!dniRegex.test(usuario.dni)) return false;
    if (!this.rolesDisponibles.includes(usuario.rol)) return false;

    return true;
  }
}
