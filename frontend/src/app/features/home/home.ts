import { Component, signal, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { TurnoConteo, EmpleadoCarga, EmpleadoPuntualidad, EmpleadoTardanza } from './model/home.model';
import { DashboardService } from './home..service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatButtonModule, MatProgressBarModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class Home implements OnInit {
  private dashboardService = inject(DashboardService);

  // --- Signals ---
  totalEmpleadosActivos = signal<number>(0);
  turnosTrabajandoActualmente = signal<number>(0);
  turnosInactivos = signal<number>(0);
  empleadosPorTurno = signal<TurnoConteo[]>([]);
  turnoConMasEmpleados = signal<string>('N/A');
  
  // Nulos por defecto hasta que cargue el HTTP
  empleadoMasCarga = signal<EmpleadoCarga | null>(null);
  empleadoMenosCarga = signal<EmpleadoCarga | null>(null);
  empleadosMasPuntuales = signal<EmpleadoPuntualidad[]>([]);
  empleadosMasTardanzas = signal<EmpleadoTardanza[]>([]);

  ngOnInit() {
    this.cargarDatosDashboard();
  }

  cargarDatosDashboard() {
    this.dashboardService.getMetrics().subscribe({
      next: (data) => {
        // Actualizamos todos los signals con la respuesta de Spring Boot
        this.totalEmpleadosActivos.set(data.totalEmpleadosActivos);
        this.turnosTrabajandoActualmente.set(data.turnosTrabajandoActualmente);
        this.turnosInactivos.set(data.turnosInactivos);
        this.empleadosPorTurno.set(data.empleadosPorTurno);
        this.turnoConMasEmpleados.set(data.turnoConMasEmpleados);
        this.empleadoMasCarga.set(data.empleadoMasCarga);
        this.empleadoMenosCarga.set(data.empleadoMenosCarga);
        this.empleadosMasPuntuales.set(data.empleadosMasPuntuales);
        this.empleadosMasTardanzas.set(data.empleadosMasTardanzas);
      },
      error: (err) => {
        console.error('Error al cargar métricas del dashboard:', err);
      }
    });
  }
}