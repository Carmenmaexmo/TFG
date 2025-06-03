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
  filtroBloqueados: string = '';
  editandoId: number | null = null;
  userEdit: any = {};
  rolesDisponibles = ['ADMINISTRADOR', 'EMPLEADO', 'CLIENTE', 'MODERADOR'];
  apiService: any;
  erroresFormulario: { [key: string]: string } = {};
  erroresEdicion: { [key: string]: string } = {};  
  bloqueos: any[] = [];
  mostrarModalBloqueo: boolean = false;
  usuarioParaBloquear: any = null;
  motivoBloqueo: string = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.cargarUsuarios();
    this.cargarBloqueos();
  }

  cargarBloqueos() {
    this.api.getBloqueosForo().subscribe(data => {
      this.bloqueos = data;
    });
  }

  estaBloqueado(user: any): boolean {
    return this.bloqueos.some(b => b.usuario?.idUsuario === user.idUsuario);
  }

  toggleBloqueo(user: any) {
    if (this.estaBloqueado(user)) {
      this.desbloquearUsuario(user);
    } else {
      this.abrirModalBloqueo(user);
    }
  }

  desbloquearUsuario(user: any) {
    const bloqueo = this.bloqueos.find(b => b.usuario?.idUsuario === user.idUsuario);
    if (bloqueo) {
      this.api.eliminarBloqueo(bloqueo.id).subscribe({
        next: () => {
          this.cargarBloqueos();
        },
        error: (err) => {
          console.error('❌ Error al desbloquear usuario', err);
        }
      });
    } else {
      console.warn('⚠ No se encontró bloqueo para este usuario');
    }
  }

  abrirModalBloqueo(user: any) {
    this.usuarioParaBloquear = user;
    this.motivoBloqueo = '';
    this.mostrarModalBloqueo = true;
  }

  cerrarModalBloqueo() {
    this.mostrarModalBloqueo = false;
    this.usuarioParaBloquear = null;
    this.motivoBloqueo = '';
  }

  confirmarBloqueo() {
    if (!this.motivoBloqueo.trim()) {
      alert('❗ Debes indicar un motivo para el bloqueo.');
      return;
    }

    const bloqueo = {
      idUsuario: this.usuarioParaBloquear.idUsuario,
      idForo: 1,
      fechaBloqueo: new Date().toISOString().split('.')[0],
      estado: 'BLOQUEADO',
      motivo: this.motivoBloqueo
    };

    this.api.crearBloqueo(bloqueo).subscribe({
      next: () => {
        alert('✅ Usuario bloqueado');
        this.cerrarModalBloqueo();
        this.cargarBloqueos();
      },
      error: (err) => {
        console.error('Error bloqueando usuario:', err);
        alert('❌ Error al bloquear usuario');
      }
    });
  }

  scrollABloqueados() {
    setTimeout(() => {
      const bloqueadosSection = document.getElementById('bloqueados-section');
      if (bloqueadosSection) {
        bloqueadosSection.scrollIntoView({ behavior: 'smooth' });
      }
    }, 50);
  }

  get bloqueadosFiltrados() {
    if (!this.filtroBloqueados.trim()) {
      return this.bloqueos;
    }

    const query = this.filtroBloqueados.toLowerCase();

    return this.bloqueos.filter(b =>
      (b.usuario?.nombreUsuario && b.usuario.nombreUsuario.toLowerCase().includes(query)) ||
      (b.usuario?.nombre && b.usuario.nombre.toLowerCase().includes(query)) ||
      (b.usuario?.apellidos && b.usuario.apellidos.toLowerCase().includes(query)) ||
      (b.motivo && b.motivo.toLowerCase().includes(query))
    );
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
    rol: 'CLIENTE'
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
      this.erroresEdicion = {};  
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
