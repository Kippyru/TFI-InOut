import { Routes } from '@angular/router';

export const scheduleRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/schedule-list/schedule-list.component').then(m => m.ScheduleListComponent),
    title: 'Schedule List'
  },
  {
    path: 'create',
    loadComponent: () => import('./pages/schedule-form/schedule-form.component').then(m => m.ScheduleFormComponent),
    title: 'Create Schedule'
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./pages/schedule-form/schedule-form.component').then(m => m.ScheduleFormComponent),
    title: 'Edit Schedule'
  }
];
