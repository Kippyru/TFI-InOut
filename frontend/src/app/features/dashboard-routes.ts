import { Dashboard } from "./dashboard/dashboard";
import { Routes } from "@angular/router";
import { Home } from './home/home';
import { adminGuard } from "../core/guards/admin-guards";

export const dashboardRoutes: Routes = [
    {
        path: '',
        component: Dashboard,
        children: [
            { path: '', redirectTo: 'home', pathMatch: 'full' },
            {
                path: 'home',
                canActivate: [adminGuard],
                component: Home,
                title: 'Home',
            },
            {
                path: 'employee',
                canActivate: [adminGuard],
                loadChildren: () => import('./employee/employee.routes').then(m => m.employeeRoutes)
            },
            {
                path: 'schedule',
                canActivate: [adminGuard],
                loadChildren: () => import('./schedule/schedule.routes').then(m => m.scheduleRoutes)
            },
            {
                path: 'attendance',
                loadChildren: () => import('./attendance/attendance.routes').then(m => m.attendanceRoutes)
            },
            {
                path: '**',
                pathMatch: 'full',
                redirectTo: 'home',
            },
        ],
    },
];