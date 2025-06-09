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

  // Lista de usuarios registrados
  usuarios: any[] = [];

  // Filtros de búsqueda
  filtro: string = '';
  filtroBloqueados: string = '';

  // Control de edición y formulario
  editandoId: number | null = null;
  userEdit: any = {};
  mostrarFormularioCrear = false;

  // Roles permitidos
  rolesDisponibles = ['ADMINISTRADOR', 'EMPLEADO', 'CLIENTE', 'MODERADOR'];

  // Gestión de errores de formularios
  erroresFormulario: { [key: string]: string } = {};
  erroresEdicion: { [key: string]: string } = {};

  // Gestión de bloqueos
  bloqueos: any[] = [];
  mostrarModalBloqueo: boolean = false;
  usuarioParaBloquear: any = null;
  motivoBloqueo: string = '';
  errorMotivoBloqueo: string = '';

  // Modelo para crear nuevo usuario
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

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.cargarUsuarios();
    this.cargarBloqueos();
  }

  // Carga usuarios desde la API
  cargarUsuarios() {
    this.api.getUsuarios().subscribe(data => {
      this.usuarios = data;
    });
  }

  // Carga bloqueos activos desde la API
  cargarBloqueos() {
    this.api.getBloqueosForo().subscribe(data => {
      this.bloqueos = data;
    });
  }

  // Verifica si un usuario está bloqueado
  estaBloqueado(user: any): boolean {
    return this.bloqueos.some(b => b.usuario?.idUsuario === user.idUsuario);
  }

  // Cambia el estado de bloqueo de un usuario
  toggleBloqueo(user: any) {
    if (this.estaBloqueado(user)) {
      this.desbloquearUsuario(user);
    } else {
      this.abrirModalBloqueo(user);
    }
  }

  // Elimina el bloqueo del usuario
  desbloquearUsuario(user: any) {
    const bloqueo = this.bloqueos.find(b => b.usuario?.idUsuario === user.idUsuario);
    if (bloqueo) {
      this.api.eliminarBloqueo(bloqueo.id).subscribe(() => this.cargarBloqueos());
    }
  }

  // Muestra el modal para bloquear un usuario
  abrirModalBloqueo(user: any) {
    this.usuarioParaBloquear = user;
    this.motivoBloqueo = '';
    this.mostrarModalBloqueo = true;
  }

  // Cierra el modal de bloqueo
  cerrarModalBloqueo() {
    this.mostrarModalBloqueo = false;
    this.usuarioParaBloquear = null;
    this.motivoBloqueo = '';
  }

  // Confirma el bloqueo de un usuario con motivo
  confirmarBloqueo() {
    if (!this.motivoBloqueo.trim()) {
      this.errorMotivoBloqueo = 'Debes indicar un motivo para el bloqueo.';
      return;
    }

    this.errorMotivoBloqueo = ''; // Limpia el error si está todo bien

    const bloqueo = {
      idUsuario: this.usuarioParaBloquear.idUsuario,
      idForo: 1,
      fechaBloqueo: new Date().toISOString().split('.')[0],
      estado: 'BLOQUEADO',
      motivo: this.motivoBloqueo
    };

    this.api.crearBloqueo(bloqueo).subscribe(() => {
      this.cerrarModalBloqueo();
      this.cargarBloqueos();
    });
  }

  // Lista filtrada de bloqueos por nombre, usuario o motivo
  get bloqueadosFiltrados() {
    const query = this.filtroBloqueados.toLowerCase();
    return this.bloqueos.filter(b =>
      (b.usuario?.nombreUsuario && b.usuario.nombreUsuario.toLowerCase().includes(query)) ||
      (b.usuario?.nombre && b.usuario.nombre.toLowerCase().includes(query)) ||
      (b.usuario?.apellidos && b.usuario.apellidos.toLowerCase().includes(query)) ||
      (b.motivo && b.motivo.toLowerCase().includes(query))
    );
  }

  // Lista filtrada de usuarios por campos relevantes
  get usuariosFiltrados() {
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

  // Inicia el proceso de creación de un nuevo usuario
  crearUsuario() {
  if (!this.validarFormulario(this.nuevoUsuario)) return;

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
    error: (err) => {
      console.error('Error al registrar usuario', err);

      const mensaje = err.error?.message?.toLowerCase?.() || '';

      if (mensaje.includes('usuario') || mensaje.includes('nombreusuario')) {
        this.erroresFormulario['nombreUsuario'] = 'El nombre de usuario ya está en uso.';
      }

      if (mensaje.includes('email')) {
        this.erroresFormulario['email'] = 'El email ya está registrado.';
      }

      if (!mensaje) {
        alert('Error inesperado al registrar el usuario.');
      }
    }
  });
  }

  // Valida los campos de un formulario de usuario
  validarFormulario(usuario: any, esEdicion = false): boolean {
    const errores: { [key: string]: string } = {};

    if (!usuario.nombreUsuario) errores['nombreUsuario'] = 'El nombre de usuario es obligatorio.';
    if (!usuario.nombre) errores['nombre'] = 'El nombre es obligatorio.';
    if (!usuario.apellidos) errores['apellidos'] = 'Los apellidos son obligatorios.';
    if (!usuario.email?.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) errores['email'] = 'Introduce un email válido.';
    if (!esEdicion && (!usuario.password || usuario.password.length < 6)) errores['password'] = 'La contraseña debe tener al menos 6 caracteres.';
    if (!usuario.telefono?.match(/^[6789]\d{8}$/)) errores['telefono'] = 'Introduce un teléfono válido (España).';
    if (!usuario.dni?.match(/^\d{8}[A-Z]$/)) errores['dni'] = 'Introduce un DNI válido.';

    if (esEdicion) {
      this.erroresEdicion = errores;
    } else {
      this.erroresFormulario = errores;
    }

    return Object.keys(errores).length === 0;
  }

  // Activa el modo edición para un usuario específico
  activarEdicion(user: any) {
    this.editandoId = user.idUsuario;
    this.userEdit = { ...user };
  }

  // Guarda los cambios editados para un usuario
  guardarEdicion(user: any) {
    if (!this.validarFormulario(this.userEdit, true)) return;

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

  // Elimina un usuario tras confirmación
  eliminarUsuario(id: number) {
    this.api.eliminarUsuario(id).subscribe(() => {
      this.usuarios = this.usuarios.filter(u => u.idUsuario !== id);
    });
    
  }

  // Validación rápida de estructura de usuario
  validarUsuario(usuario: any): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const telefonoRegex = /^[6789]\d{8}$/;
    const dniRegex = /^\d{8}[A-Z]$/;

    return emailRegex.test(usuario.email)
      && telefonoRegex.test(usuario.telefono)
      && dniRegex.test(usuario.dni)
      && this.rolesDisponibles.includes(usuario.rol);
  }

  // Desplaza la vista a la sección de bloqueados
  scrollABloqueados() {
    setTimeout(() => {
      const bloqueadosSection = document.getElementById('bloqueados-section');
      if (bloqueadosSection) {
        bloqueadosSection.scrollIntoView({ behavior: 'smooth' });
      }
    }, 50);
  }
}
