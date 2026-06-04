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
                path: '**',
                pathMatch: 'full',
                redirectTo: 'home',
            },
        ],
    },
];