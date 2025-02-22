import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { isNotAuthenticatedGuard } from './core/guards/is-not-authenticated.guard';
import { isAuthenticatedGuard } from './core/guards/is-authenticated.guard';

const routes: Routes = [
  {
    path: '',
    canActivate:[isAuthenticatedGuard],
    loadChildren: () => import('./modules/layout/layout.module').then((m) => m.LayoutModule),
  },
  {
    path:'planner',
    loadChildren: () => import('./modules/front-office/front-office.module').then((m) => m.FrontOfficeModule),
  },
  {
    path: 'auth',
    canActivate:[isNotAuthenticatedGuard],
    loadChildren: () => import('./modules/auth/auth.module').then((m) => m.AuthModule),
  },

  {
    path: 'errors',
    loadChildren: () => import('./modules/error/error.module').then((m) => m.ErrorModule),
  },
  { path: '**', redirectTo: 'errors/404' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
