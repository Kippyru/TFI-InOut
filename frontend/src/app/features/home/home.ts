import { Component, signal, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MatCardModule, MatIconModule, MatButtonModule, MatProgressBarModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class Home implements OnInit {
  // --- Métricas Generales ---
  totalEmpleadosActivos = signal<number>(0);
  turnosTrabajandoActualmente = signal<number>(0);
  turnosInactivos = signal<number>(0);

  // --- Métricas por Turno ---
  empleadosPorTurno = signal<{turno: string, cantidad: number}[]>([]);
  turnoConMasEmpleados = signal<string>('');

  // --- Rankings de Empleados (Carga Horaria) ---
  empleadoMasCarga = signal<{nombre: string, horas: number} | null>(null);
  empleadoMenosCarga = signal<{nombre: string, horas: number} | null>(null);

  // --- Rankings de Puntualidad ---
  empleadosMasPuntuales = signal<{nombre: string, porcentaje: number}[]>([]);
  empleadosMasTardanzas = signal<{nombre: string, cantidad: number}[]>([]);

  ngOnInit() {
    this.cargarDatosDashboard();
  }

  cargarDatosDashboard() {
    // Aquí harías la llamada a tu API (ej: this.dashboardService.getMetrics().subscribe(...))
    // Datos simulados para visualizar el diseño:
    this.totalEmpleadosActivos.set(120);
    this.turnosTrabajandoActualmente.set(3);
    this.turnosInactivos.set(1);
    
    this.empleadosPorTurno.set([
      { turno: 'Mañana', cantidad: 50 },
      { turno: 'Tarde', cantidad: 45 },
      { turno: 'Noche', cantidad: 25 }
    ]);
    this.turnoConMasEmpleados.set('Mañana');

    this.empleadoMasCarga.set({ nombre: 'Carlos Ruiz', horas: 48 });
    this.empleadoMenosCarga.set({ nombre: 'Ana Gómez', horas: 20 });

    this.empleadosMasPuntuales.set([
      { nombre: 'Laura Martínez', porcentaje: 100 },
      { nombre: 'Diego Fernández', porcentaje: 98 },
      { nombre: 'Sofía Castro', porcentaje: 95 }
    ]);

    this.empleadosMasTardanzas.set([
      { nombre: 'Juan Pérez', cantidad: 5 },
      { nombre: 'Miguel Silva', cantidad: 3 }
    ]);
  }
}