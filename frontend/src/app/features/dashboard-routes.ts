import { Dashboard } from "./dashboard/dashboard";
import { Routes } from "@angular/router";
import { Home } from './home/home';

export const dashboardRoutes: Routes = [
    {
        path: '',
        component: Dashboard,
        children: [
            { path: '', redirectTo: 'home', pathMatch: 'full' },
            {
                path: 'home',
                component: Home,
                title: 'Home',
            },
            {
                path: 'employee',
                loadChildren: () => import('./employee/employee.routes').then(m => m.employeeRoutes)
            },
            {
                path: 'schedule',
                loadChildren: () => import('./schedule/schedule.routes').then(m => m.scheduleRoutes)
            },
            {
                path: '**',
                pathMatch: 'full',
                redirectTo: 'home',
            },
        ],
    },
];