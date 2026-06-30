import { Routes } from '@angular/router';
import { Login } from './features/auth/pages/login/login.component';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [

  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: Login },

  {
    path: 'dashboard',
    loadChildren: () =>
      import('./features/dashboard/dashboard.routes')
        .then(m => m.dashboardRoutes),
    canActivate: [authGuard]
  },

  { path: '**', redirectTo: 'dashboard', pathMatch: 'full' }

];