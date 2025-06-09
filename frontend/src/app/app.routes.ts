import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';


export const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
  { path: 'registro', loadComponent: () => import('./pages/registro/registro.component').then(m => m.RegistroComponent) },
  { path: 'catalogo', loadComponent: () => import('./pages/catalogo/catalogo.component').then(m => m.CatalogoComponent) },
  { path: 'vinilo/:id', loadComponent: () => import('./pages/vinilo-detalle/vinilo-detalle.component').then(m => m.ViniloDetalleComponent)},
  { path: 'perfil', loadComponent: () => import('./pages/perfil/perfil.component').then(m => m.PerfilComponent), canActivate: [AuthGuard] },
  { path: 'pedidos', loadComponent: () => import('./pages/pedidos/pedidos.component').then(m => m.PedidosComponent), canActivate: [AuthGuard] },
  { path: 'eventos', loadComponent: () => import('./pages/eventos/eventos.component').then(m => m.EventosComponent), canActivate: [AuthGuard] },
  { path: 'foros', loadComponent: () => import('./pages/foros/foros.component').then(m => m.ForosComponent), canActivate: [AuthGuard] },
  { path: 'temas/:id', loadComponent: () => import('./pages/tema-detalle/tema-detalle.component').then(m => m.TemaDetalleComponent), canActivate: [AuthGuard] },
  { path: 'pago', loadComponent: () => import('./pages/pago/pago.component').then(m => m.PagoComponent), canActivate: [AuthGuard] },
  { path: 'admin/usuarios', loadComponent: () => import('./components/admin/usuarios-admin/usuarios-admin.component').then(m => m.UsuariosAdminComponent), canActivate: [AuthGuard], data: { roles: ['ADMINISTRADOR', 'EMPLEADO'] } },
  { path: 'admin/pedidos', loadComponent: () => import('./components/admin/pedidos-admin/pedidos-admin.component').then(m => m.PedidosAdminComponent), canActivate: [AuthGuard], data: { roles: ['ADMINISTRADOR', 'EMPLEADO'] } },
  { path: 'admin/eventos', loadComponent: () => import('./components/admin/eventos-admin/eventos-admin.component').then(m => m.EventosAdminComponent), canActivate: [AuthGuard], data: { roles: ['ADMINISTRADOR', 'EMPLEADO'] } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
