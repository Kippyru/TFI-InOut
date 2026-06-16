import { Routes } from '@angular/router';
import { adminGuard } from '../../core/guards/admin-guards';

export const attendanceRoutes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/attendance-clock/attendance-clock.component').then(
        (m) => m.AttendanceClockComponent
      ),
    title: 'Marcar Asistencia'
  },
  {
    path: 'audit',
    loadComponent: () =>
      import('./pages/audit-attendance/audit-attendance.component').then(
        (m) => m.AuditAttendanceComponent
      ),
    title: 'Auditoría de Asistencia',
    canActivate: [adminGuard]
  }
];
