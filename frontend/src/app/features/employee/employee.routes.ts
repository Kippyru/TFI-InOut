import { Routes } from '@angular/router';

export const employeeRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/employee-list/employee-list.component').then(m => m.EmployeeListComponent),
    title: 'Employee List'
  },
  {
    path: 'create',
    loadComponent: () => import('./pages/employee-form/employee-form.component').then(m => m.EmployeeFormComponent),
    title: 'Create Employee'
  },
  {
    path: ':id',
    loadComponent: () => import('./pages/employee-detail/employee-detail.component').then(m => m.EmployeeDetailComponent),
    title: 'Employee Details'
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./pages/employee-form/employee-form.component').then(m => m.EmployeeFormComponent),
    title: 'Edit Employee'
  }
];
